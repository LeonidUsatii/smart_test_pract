package de.ait.smarttestit;

import de.ait.smarttestit.dto.exam_task.ExamTaskDto;
import de.ait.smarttestit.dto.exam_task.NewExamTaskDto;
import de.ait.smarttestit.dto.exam_task.UpdateExamTaskDto;
import de.ait.smarttestit.dto.question.QuestionDto;
import de.ait.smarttestit.dto.test_type.NewTestTypeDto;
import de.ait.smarttestit.dto.test_type.TestTypeDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.ExamTask;
import de.ait.smarttestit.models.Question;
import de.ait.smarttestit.models.TestType;
import de.ait.smarttestit.repositories.ExamTasksRepository;
import de.ait.smarttestit.repositories.TestTypesRepository;
import de.ait.smarttestit.services.TestTypeService;
import de.ait.smarttestit.services.impl.ExamTasksServiceImpl;
import de.ait.smarttestit.services.impl.TestTypeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import java.util.stream.Collectors;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("examTask CRUD:")
public class ExamTasksServiceUnitTests {

    private static final Long EXAM_TASK_Id = 1L;

    private static final Long TEST_TYPE_ID = 1L;

    private static final Long QUESTION_ID = 1L;

    private static final String EXAM_TASK_TITLE = "Java beginner level";

    private static final ExamTask EXAM_TASK = new ExamTask(EXAM_TASK_Id, EXAM_TASK_TITLE);

    private static final NewExamTaskDto NEW_EXAM_TASK = new NewExamTaskDto(EXAM_TASK_TITLE);

    private static final String NEW_QUESTION_TEXT = "This is a new Question";

    private static final String TEST_TYPE_TITLE = "Java beginner level";

    private static final Set<Question> QUESTION_SET = new HashSet<>();

    private static final TestType TEST_TYPE = new TestType(TEST_TYPE_ID, TEST_TYPE_TITLE, QUESTION_SET, EXAM_TASK);

    private static final QuestionDto QUESTION_DTO = new QuestionDto(QUESTION_ID, NEW_QUESTION_TEXT, 5, TEST_TYPE_ID);

    private static final NewTestTypeDto NEW_TEST_TYPE_DTO = new NewTestTypeDto(TEST_TYPE_TITLE);

    private static final TestTypeDto TEST_TYPE_DTO = new TestTypeDto(1L, TEST_TYPE_TITLE, EXAM_TASK_Id);

    @Mock
    private ExamTasksRepository examTasksRepository;

    @Mock
    private TestTypesRepository testTypesRepository;

    @Mock
    private TestTypeService typeService;

    @InjectMocks
    private ExamTasksServiceImpl examTasksServices;

    @InjectMocks
    private TestTypeServiceImpl testTypeService;

    @Nested
    @DisplayName("get examTask or trow")
    class GetExamTaskOrThrow {

        @Test
        void getByIdOrThrowPositive() {

            when(examTasksRepository.findById(EXAM_TASK_Id)).thenReturn(Optional.of(EXAM_TASK));

            ExamTask foundExamTask = examTasksServices.getByIdOrThrow(EXAM_TASK_Id);

            assertNotNull(foundExamTask, "ExamTask should not be null");
            assertEquals(EXAM_TASK_Id, foundExamTask.getId(), "ExamTask ID should match the requested ID");
        }

        @Test
        void getByIdOrThrowNegative() {

            when(examTasksRepository.findById(EXAM_TASK_Id)).thenReturn(Optional.empty());

            RestException thrown = assertThrows(RestException.class, () -> examTasksServices.getByIdOrThrow(EXAM_TASK_Id),
                    "Expected getExamTaskOrThrow to throw, but it didn't");

            assertTrue(thrown.getMessage().contains("ExamTask with id <" + EXAM_TASK_Id + "> not found"), "Exception message should contain the correct examTask ID");
        }
    }

    @Nested
    @DisplayName("is exam task title unique")
    class isExamTaskTitleUnique {
        @Test
        void testFindByExamTaskTitlePositive() {

            when(examTasksRepository.findByExamTaskTitle("UniqueTitle")).thenReturn(Optional.empty());

            boolean result = examTasksServices.isExamTaskTitleUnique("UniqueTitle");

            assertTrue(result, "Method should return true when the exam task title is unique");
        }

        @Test
        void testFindByExamTaskTitleNegative() {

            ExamTask mockExamTask = new ExamTask();

            mockExamTask.setExamTaskTitle("ExistingTitle");

            when(examTasksRepository.findByExamTaskTitle("ExistingTitle")).thenReturn(Optional.of(mockExamTask));

            boolean result = examTasksServices.isExamTaskTitleUnique("ExistingTitle");

            assertFalse(result, "Method should return false when the exam task title is not unique");
        }
    }

    @Nested
    @DisplayName("add examTask")
    class AddExamTask {

        @Test
        void testAddExamTaskPositive() {

            when(examTasksRepository.findByExamTaskTitle(anyString())).thenReturn(Optional.empty());
            when(examTasksRepository.save(any(ExamTask.class))).thenReturn(EXAM_TASK);

            ExamTaskDto result = examTasksServices.addExamTask(NEW_EXAM_TASK);

            assertNotNull(result, "The result should not be null");
            assertEquals(NEW_EXAM_TASK.examTaskTitle(), result.examTaskTitle(), "The exam task titles should match");

            verify(examTasksRepository).findByExamTaskTitle(NEW_EXAM_TASK.examTaskTitle());
            verify(examTasksRepository).save(any(ExamTask.class));
        }

        @Test
        void testAddExamTaskNegative() {

            when(examTasksRepository.findByExamTaskTitle(anyString())).thenReturn(Optional.of(EXAM_TASK));

            assertThrows(IllegalArgumentException.class, () -> examTasksServices.addExamTask(NEW_EXAM_TASK), "Exam task title must be unique");
        }
    }

    @Nested
    @DisplayName("get examTask")
    class GetExam {

        @Test
        void testGetExamTaskPositive() {

            when(examTasksRepository.findById(EXAM_TASK_Id)).thenReturn(Optional.of(EXAM_TASK));

            ExamTaskDto result = examTasksServices.getExamTask(EXAM_TASK_Id);

            assertNotNull(result);
            assertEquals(EXAM_TASK.getId(), result.id());
            assertEquals(EXAM_TASK.getExamTaskTitle(), result.examTaskTitle());
        }

        @Test
        void testGetExamTaskNegative() {

            Long examId = 5L;

            RestException exception = assertThrows(RestException.class,
                    () -> examTasksServices.getExamTask(examId));

            assertThat(exception.getMessage().trim(),
                    containsString("ExamTask with id <" + examId + "> not found"));
        }
    }

    @Nested
    @DisplayName("get list examTasks")
    class GetListExams {

        @Test
        void testGetListExamTasksPositive() {

            ExamTask examTask1 = new ExamTask(1L, "Exception handling in Java");
            ExamTask examTask2 = new ExamTask(2L, "Control operators in Java");
            List<ExamTask> examTasks = Arrays.asList(examTask1, examTask2);

            when(examTasksRepository.findAll()).thenReturn(examTasks);

            List<ExamTaskDto> result = examTasksServices.getListExamTasks();

            assertNotNull(result);
            assertEquals(2, result.size());
            List<Long> ids = result.stream().map(ExamTaskDto::id).collect(Collectors.toList());
            assertTrue(ids.containsAll(Arrays.asList(1L, 2L)));
        }

        @Test
        void testGetListExamTasksNegative() {

            when(examTasksRepository.findAll()).thenReturn(List.of());

            List<ExamTaskDto> result = examTasksServices.getListExamTasks();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("delete examTask")
    class deleteExamTask {

        @Test
        void testDeleteExamTaskPositive() {

            Long examId = 1L;
            ExamTask examTask = new ExamTask();
            examTask.setId(examId);
            examTask.setExamTaskTitle("Deletable exam task");

            when(examTasksRepository.findById(examId)).thenReturn(Optional.of(examTask));
            doNothing().when(examTasksRepository).delete(any(ExamTask.class));

            ExamTaskDto result = examTasksServices.deleteExamTask(examId);

            assertNotNull(result);
            assertEquals(examId, result.id());
            assertEquals("Deletable exam task", result.examTaskTitle());

            verify(examTasksRepository).delete(examTask);
        }

        @Test
        void testDeleteExamTaskNegative() {

            Long examId = 1L;
            when(examTasksRepository.findById(examId)).thenReturn(Optional.empty());

            assertThrows(RestException.class, () -> examTasksServices.deleteExamTask(examId));

            verify(examTasksRepository, never()).delete(any(ExamTask.class));
        }
    }

    @Nested
    @DisplayName("update examTask")
    class updateExamTask {

        @Test
        void testUpdateExamTaskPositive() {

            Long examId = 1L;
            UpdateExamTaskDto updateDto = new UpdateExamTaskDto("Updated Title");
            ExamTask examTask = new ExamTask();
            examTask.setId(examId);
            examTask.setExamTaskTitle("Original Title");

            when(examTasksRepository.findById(examId)).thenReturn(Optional.of(examTask));
            when(examTasksRepository.save(any(ExamTask.class))).thenReturn(examTask);

            ExamTaskDto result = examTasksServices.updateExamTask(examId, updateDto);

            assertNotNull(result);
            assertEquals("Updated Title", result.examTaskTitle());

            verify(examTasksRepository).save(examTask);
        }

        @Test
        void testUpdateExamTaskNegative() {

            Long examId = 1L;
            UpdateExamTaskDto updateDto = new UpdateExamTaskDto("Updated Title");

            when(examTasksRepository.findById(examId)).thenReturn(Optional.empty());

            assertThrows(RestException.class, () -> examTasksServices.updateExamTask(examId, updateDto));

            verify(examTasksRepository, never()).save(any(ExamTask.class));
        }
    }

    @Nested
    @DisplayName("add testType to examTask by newTestType")
    class AddTestTypeToExamTaskByNewTestType {

        @Test
        void testAddTestTypeToExamTaskPositive() {

            when(examTasksRepository.findById(EXAM_TASK_Id)).thenReturn(Optional.of(EXAM_TASK));

            ExamTask examTask = examTasksServices.getByIdOrThrow(EXAM_TASK_Id);

            when(testTypesRepository.findById(TEST_TYPE_ID)).thenReturn(Optional.of(TEST_TYPE));

            TestType addedTestType = testTypeService.getByIdOrThrow(QUESTION_DTO.id());

            examTask.setTestTypes(new HashSet<>());

            when(examTasksRepository.findById(1L)).thenReturn(Optional.of(EXAM_TASK));

            when(typeService.addTestType(any(NewTestTypeDto.class))).thenReturn(TEST_TYPE_DTO);

            when(typeService.getByIdOrThrow(TEST_TYPE_DTO.id())).thenReturn(addedTestType);

            Set<TestType> updatedTestType = examTasksServices.addTestTypeToExamTask(EXAM_TASK_Id, NEW_TEST_TYPE_DTO);

            assertTrue(updatedTestType.contains(addedTestType));

            verify(examTasksRepository).save(EXAM_TASK);
        }

        @Test
        void testAddTestTypeToExamTaskNegative() {

            when(examTasksRepository.findById(anyLong())).thenThrow(new EntityNotFoundException());

            assertThrows(EntityNotFoundException.class, () ->
                    examTasksServices.addTestTypeToExamTask(EXAM_TASK_Id, NEW_TEST_TYPE_DTO));
        }
    }

    @Nested
    @DisplayName("add testType to examTask by testType_id")
    class AddTestTypeToExamTaskByTestTypeId {

        @Test
        void testAddTestTypeToExamTaskByTestTypeId_Positive() {

            when(examTasksRepository.findById(EXAM_TASK_Id)).thenReturn(Optional.of(EXAM_TASK));

            when(testTypesRepository.findById(TEST_TYPE_ID)).thenReturn(Optional.of(TEST_TYPE));

            ExamTask examTask = examTasksServices.getByIdOrThrow(EXAM_TASK_Id);

            examTask.setTestTypes(new HashSet<>());

            TestType addedTestType = testTypeService.getByIdOrThrow(TEST_TYPE_ID);

            when(typeService.getByIdOrThrow(TEST_TYPE_ID)).thenReturn(addedTestType);

            Set<TestType> updatedTestType = examTasksServices.addTestTypeToExamTask(EXAM_TASK_Id, TEST_TYPE_ID);

            assertTrue(updatedTestType.contains(addedTestType));

            verify(examTasksRepository, times(1)).save(examTask);
        }

        @Test
        void testAddTestTypeToExamTaskByTestTypeId_Negative() {

            Long invalidExamTaskId = 2L;
            Long testTypeId = 1L;

            when(examTasksRepository.findById(invalidExamTaskId))
                    .thenThrow(new EntityNotFoundException("Exam task not found"));

            assertThrows(EntityNotFoundException.class, () -> {
                examTasksServices.addTestTypeToExamTask(invalidExamTaskId, testTypeId);
            });
        }
    }
}