package de.ait.smarttestit.controllers;

import de.ait.smarttestit.controllers.api.TestTypesApi;
import de.ait.smarttestit.dto.test_type.NewTestTypeDto;
import de.ait.smarttestit.dto.test_type.TestTypeDto;
import de.ait.smarttestit.dto.test_type.UpdateTestTypeDto;
import de.ait.smarttestit.services.TestTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
//@PreAuthorize("hasAnyAuthority('Teacher')")
public class TestTypesController implements TestTypesApi {

    private final TestTypeService testTypesService;

    @Override
    public TestTypeDto addTestType(NewTestTypeDto newTestType) {

        return testTypesService.addTestType(newTestType);
    }
    @Override
    public List<TestTypeDto> getAllTests() {
        return testTypesService.getAll();
    }
    @Override
    public TestTypeDto updateTestType(Long testTypeId, UpdateTestTypeDto updateTestType) {
        return testTypesService.updateTestType(testTypeId, updateTestType);
    }
    @Override
    public TestTypeDto deleteTestType(Long testTypeId) {
        return testTypesService.deleteTestType(testTypeId);
    }
}