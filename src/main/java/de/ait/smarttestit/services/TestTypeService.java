package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.test_type.NewTestTypeDto;
import de.ait.smarttestit.dto.test_type.TestTypeDto;
import de.ait.smarttestit.dto.test_type.UpdateTestTypeDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.TestType;
import lombok.NonNull;

import java.util.List;

public interface TestTypeService {
    /**
     * Add a new test type.
     *
     * @param newTestType The new test type to be added.
     * @return The created test type.
     */
    TestTypeDto addTestType(NewTestTypeDto newTestType);

    /**
     * Retrieves all the test types.
     *
     * @return A list of {@link TestTypeDto} objects representing the test types.
     */
    List<TestTypeDto> getAll();

    /**
     * Retrieves a {@link TestType} by its ID or throws a {@link RestException} with status NOT_FOUND if not found.
     *
     * @param id The ID of the test type.
     * @return The {@link TestType} object.
     * @throws RestException If the test type with the specified ID is not found.
     */
    TestType getByIdOrThrow(@NonNull Long id);

    /**
     * Updates a test type with the specified ID.
     *
     * @param testTypeId     The ID of the test type to update.
     * @param updateTestType The updated test type data.
     * @return The updated test type data.
     */
    TestTypeDto updateTestType(Long testTypeId, UpdateTestTypeDto updateTestType);

    /**
     * Deletes a test type by its ID.
     *
     * @param testTypeId The ID of the test type to delete.
     * @return The deleted test type represented by a {@link TestTypeDto} object.
     */
    TestTypeDto deleteTestType(Long testTypeId);
}
