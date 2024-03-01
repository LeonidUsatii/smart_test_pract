package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.test_type.NewTestTypeDto;
import de.ait.smarttestit.dto.test_type.TestTypeDto;
import de.ait.smarttestit.dto.test_type.UpdateTestTypeDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.TestType;
import de.ait.smarttestit.repositories.TestTypeRepository;
import de.ait.smarttestit.services.TestTypeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TestTypeServiceImpl implements TestTypeService {

    private final TestTypeRepository testTypeRepository;

    @Override
    @Transactional
    public TestTypeDto addTestType(@NonNull final NewTestTypeDto newTestType) {

        if (testTypeRepository.existsByName(newTestType.name())) {
            throw new RestException(HttpStatus.CONFLICT,
                    "TestType <" + newTestType.name() + "> already exists");
        }
        TestType testType = new TestType(newTestType.name());

        testTypeRepository.save(testType);
        return TestTypeDto.from(testType);
    }

    @Override
    public List<TestTypeDto> getAll() {
        List<TestType> tests = testTypeRepository.findAll();
        return TestTypeDto.from(tests);
    }

    @Override
    public TestType getByIdOrThrow(@NonNull final Long testTypeId) {
        return testTypeRepository.findById(testTypeId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND,
                        "TestType with id <" + testTypeId + "> not found"));
    }

    @Override
    public TestTypeDto updateTestType(@NonNull final Long testTypeId,
                                      @NonNull final UpdateTestTypeDto updateTestType) {
        TestType testType = getByIdOrThrow(testTypeId);

        testType.setName(updateTestType.name());

        testType = testTypeRepository.save(testType);
        return TestTypeDto.from(testType);
    }

    @Override
    public TestTypeDto deleteTestType(@NonNull final Long testTypeId) {
        TestType testType = getByIdOrThrow(testTypeId);

        testTypeRepository.delete(testType);
        return TestTypeDto.from(testType);
    }
}
