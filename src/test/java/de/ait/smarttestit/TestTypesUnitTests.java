package de.ait.smarttestit;

import de.ait.smarttestit.dto.test_type.NewTestTypeDto;
import de.ait.smarttestit.dto.test_type.TestTypeDto;
import de.ait.smarttestit.dto.test_type.UpdateTestTypeDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.TestType;
import de.ait.smarttestit.repositories.TestTypesRepository;
import de.ait.smarttestit.services.TestTypeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class TestTypesUnitTests {

    @Autowired
    private TestTypeService testTypeService;

    @MockBean
    private TestTypesRepository testTypesRepository;

    @Nested
    @DisplayName("add test type")
    class addTestType {

        @Test
        void addTestType_PositiveTest() {

            NewTestTypeDto newTestTypeDto = new NewTestTypeDto("NewTestTypeName");
            when(testTypesRepository.existsByName(newTestTypeDto.name())).thenReturn(false);

            TestTypeDto result = testTypeService.addTestType(newTestTypeDto);

            assertNotNull(result);
            assertEquals("NewTestTypeName", result.name());
        }

        @Test
        void addTestType_NegativeTest() {

            NewTestTypeDto newTestTypeDto = new NewTestTypeDto("ExistingTestTypeName");
            when(testTypesRepository.existsByName(newTestTypeDto.name())).thenReturn(true);

            RestException exception = assertThrows(RestException.class,
                    () -> testTypeService.addTestType(newTestTypeDto));

            assertEquals(HttpStatus.CONFLICT, exception.getStatus());
            assertThat(exception.getMessage().trim(),
                    containsString("TestType <ExistingTestTypeName> already exists"));
        }
    }

    @Nested
    @DisplayName("add test type")
    class getAllTestType {

        @Test
        void testGetAllTests() {

            TestType testType1 = new TestType("TestType1");
            TestType testType2 = new TestType("TestType2");

            when(testTypesRepository.findAll()).thenReturn(Arrays.asList(testType1, testType2));

            List<TestTypeDto> result = testTypeService.getAll();

            assertEquals(2, result.size());

            assertEquals(TestTypeDto.from(testType1), result.get(0));
            assertEquals(TestTypeDto.from(testType2), result.get(1));
        }
    }

    @Nested
    @DisplayName("test get by id")
    class getById {
        @Test
        void testGetByIdOrThrow_returnsTestType_whenIdExists() {
            TestType testType = new TestType();
            testType.setId(1L);
            when(testTypesRepository.findById(anyLong())).thenReturn(Optional.of(testType));

            TestType result = testTypeService.getByIdOrThrow(1L);

            verify(testTypesRepository).findById(1L);
            assertEquals(testType, result);
        }

        @Test
        void testGetByIdOrThrow_throwsRestException_whenIdDoesNotExist() {
            long testTypeId = 1L;
            when(testTypesRepository.findById(testTypeId)).thenReturn(Optional.empty());

            RestException exception = assertThrows(RestException.class,
                    () -> testTypeService.getByIdOrThrow(testTypeId));

            verify(testTypesRepository).findById(1L);
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals("TestType with id <" + testTypeId + "> not found", exception.getMessage());
        }
    }


    @Nested
    @DisplayName("update test type")
    class updateTestType {

        @Test
        void updateTestType_PositiveTest() {

            Long testTypeId = 1L;
            TestType existsTestType = new TestType("OldTestType");
            UpdateTestTypeDto updateTestTypeDto = new UpdateTestTypeDto("UpdatedTestTypeName");

            when(testTypesRepository.findById(testTypeId)).thenReturn(Optional.of(existsTestType));

            testTypeService.updateTestType(testTypeId, updateTestTypeDto);

            verify(testTypesRepository).findById(1L);
            verify(testTypesRepository).save(argThat(saved -> saved.getName().equals(updateTestTypeDto.name())));
        }

        @Test
        void updateTestType_NegativeTest() {

            Long testTypeId = 1L;
            UpdateTestTypeDto updateTestTypeDto = new UpdateTestTypeDto("UpdatedTestTypeName");

            RestException exception = assertThrows(RestException.class,
                    () -> testTypeService.updateTestType(testTypeId, updateTestTypeDto));

            verify(testTypesRepository).findById(1L);
            verify(testTypesRepository, never()).save(any(TestType.class));

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals("TestType with id <" + testTypeId + "> not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("delete test type")
    class deleteTestType {

        @Test
        void deleteTestType_PositiveTest() {

            Long testTypeId = 1L;

            TestType existingTestType = new TestType();
            existingTestType.setId(testTypeId);
            existingTestType.setName("TestTypeName");

            when(testTypesRepository.findById(testTypeId)).thenReturn(Optional.of(existingTestType));

            TestTypeDto result = testTypeService.deleteTestType(testTypeId);

            assertEquals("TestTypeName", result.name());
            assertEquals(testTypeId, result.id());

            verify(testTypesRepository, times(1)).delete(existingTestType);
        }

        @Test
        void deleteTestType_NotFoundTest() {

            Long testTypeId = 1L;

            RestException exception = assertThrows(RestException.class,
                    () -> testTypeService.deleteTestType(testTypeId));

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals("TestType with id <" + testTypeId + "> not found", exception.getMessage());
        }
    }
}

