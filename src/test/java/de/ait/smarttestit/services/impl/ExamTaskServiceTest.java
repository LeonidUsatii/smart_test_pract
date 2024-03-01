package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.exam_task.ExamTaskDto;
import de.ait.smarttestit.dto.exam_task.UpdateExamTaskDto;
import de.ait.smarttestit.dto.question.QuestionDto;
import de.ait.smarttestit.dto.test_type.NewTestTypeDto;
import de.ait.smarttestit.dto.test_type.TestTypeDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.ExamTask;
import de.ait.smarttestit.models.Question;
import de.ait.smarttestit.models.TestType;
import de.ait.smarttestit.repositories.ExamTaskRepository;
import de.ait.smarttestit.repositories.TestTypeRepository;
import de.ait.smarttestit.services.TestTypeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("examTask CRUD:")
public class ExamTaskServiceTest {

    private static final Long EXAM_TASK_Id = 1L;
    private static final Long TEST_TYPE_ID = 1L;
    private static final Long QUESTION_ID = 1L;
    private static final String EXAM_TASK_TITLE = "Java beginner level";
    private static final ExamTask EXAM_TASK = new ExamTask(EXAM_TASK_Id, EXAM_TASK_TITLE);
   // private static final NewTestsParamDto NEW_EXAM_TASK = new NewTestsParamDto(
    //        EXAM_TASK_TITLE, TEST_TYPE_ID, EXAM_TASK.getQuestionLevel(), EXAM_TASK.getQuestionCount());
    private static final String NEW_QUESTION_TEXT = "This is a new Question";
    private static final String TEST_TYPE_TITLE = "Java beginner level";
    private static final List<Question> QUESTION_SET = new ArrayList<>();
    private static final TestType TEST_TYPE = new TestType(TEST_TYPE_ID, TEST_TYPE_TITLE, QUESTION_SET, EXAM_TASK);
    private static final QuestionDto QUESTION_DTO = new QuestionDto(QUESTION_ID, NEW_QUESTION_TEXT, 5, TEST_TYPE_ID, null);
    private static final NewTestTypeDto NEW_TEST_TYPE_DTO = new NewTestTypeDto(TEST_TYPE_TITLE);
    private static final TestTypeDto TEST_TYPE_DTO = new TestTypeDto(1L, TEST_TYPE_TITLE, EXAM_TASK_Id);

    @Mock
    private ExamTaskRepository examTaskRepository;
    @Mock
    private TestTypeRepository testTypeRepository;

    @Mock
    private TestTypeService typeService;

    @InjectMocks
    private ExamTaskServiceImpl examTasksServices;
    @InjectMocks
    private TestTypeServiceImpl testTypeService;

    @Nested
    @DisplayName("get examTask or trow")
    class GetExamTaskOrThrow {

        @Test
        void getByIdOrThrowPositive() {

            when(examTaskRepository.findById(EXAM_TASK_Id)).thenReturn(Optional.of(EXAM_TASK));

            ExamTask foundExamTask = examTasksServices.getByIdOrThrow(EXAM_TASK_Id);

            assertNotNull(foundExamTask, "ExamTask should not be null");
            assertEquals(EXAM_TASK_Id, foundExamTask.getId(), "ExamTask ID should match the requested ID");
        }

        @Test
        void getByIdOrThrowNegative() {

            when(examTaskRepository.findById(EXAM_TASK_Id)).thenReturn(Optional.empty());

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

            when(examTaskRepository.findByExamTaskTitle("UniqueTitle")).thenReturn(Optional.empty());

            boolean result = examTasksServices.isExamTaskTitleUnique("UniqueTitle");

            assertTrue(result, "Method should return true when the exam task title is unique");
        }

        @Test
        void testFindByExamTaskTitleNegative() {

            ExamTask mockExamTask = new ExamTask();

            mockExamTask.setExamTaskTitle("ExistingTitle");

            when(examTaskRepository.findByExamTaskTitle("ExistingTitle")).thenReturn(Optional.of(mockExamTask));

            boolean result = examTasksServices.isExamTaskTitleUnique("ExistingTitle");

            assertFalse(result, "Method should return false when the exam task title is not unique");
        }
    }

  /*  @Nested
    @DisplayName("add examTask")
    class AddExamTask {

        @Test
        void testAddExamTaskPositive() {

            when(examTaskRepository.findByExamTaskTitle(anyString())).thenReturn(Optional.empty());
            when(examTaskRepository.save(any(ExamTask.class))).thenReturn(EXAM_TASK);

           // ExamTaskDto result = examTasksServices.addExamTask(NEW_EXAM_TASK);

           // assertNotNull(result, "The result should not be null");
           // assertEquals(NEW_EXAM_TASK.examTitle(), result.examTaskTitle(), "The exam task titles should match");

           // verify(examTaskRepository).findByExamTaskTitle(NEW_EXAM_TASK.examTitle());
            verify(examTaskRepository).save(any(ExamTask.class));
        }

        @Test
        void testAddExamTaskNegative() {

            when(examTaskRepository.findByExamTaskTitle(anyString())).thenReturn(Optional.of(EXAM_TASK));

         //   assertThrows(IllegalArgumentException.class, () -> examTasksServices.addExamTask(NEW_EXAM_TASK), "Exam task title must be unique");
        }
    }*/

    @Nested
    @DisplayName("get examTask")
    class GetExam {

        @Test
        void testGetExamTaskPositive() {

            when(examTaskRepository.findById(EXAM_TASK_Id)).thenReturn(Optional.of(EXAM_TASK));

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

            when(examTaskRepository.findAll()).thenReturn(examTasks);

            List<ExamTaskDto> result = examTasksServices.getListExamTasks();

            assertNotNull(result);
            assertEquals(2, result.size());
            List<Long> ids = result.stream().map(ExamTaskDto::id).toList();
            assertTrue(ids.containsAll(Arrays.asList(1L, 2L)));
        }

        @Test
        void testGetListExamTasksNegative() {

            when(examTaskRepository.findAll()).thenReturn(List.of());

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

            when(examTaskRepository.findById(examId)).thenReturn(Optional.of(examTask));
            doNothing().when(examTaskRepository).delete(any(ExamTask.class));

            ExamTaskDto result = examTasksServices.deleteExamTask(examId);

            assertNotNull(result);
            assertEquals(examId, result.id());
            assertEquals("Deletable exam task", result.examTaskTitle());

            verify(examTaskRepository).delete(examTask);
        }

        @Test
        void testDeleteExamTaskNegative() {

            Long examId = 1L;
            when(examTaskRepository.findById(examId)).thenReturn(Optional.empty());

            assertThrows(RestException.class, () -> examTasksServices.deleteExamTask(examId));

            verify(examTaskRepository, never()).delete(any(ExamTask.class));
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

            when(examTaskRepository.findById(examId)).thenReturn(Optional.of(examTask));
            when(examTaskRepository.save(any(ExamTask.class))).thenReturn(examTask);

            ExamTaskDto result = examTasksServices.updateExamTask(examId, updateDto);

            assertNotNull(result);
            assertEquals("Updated Title", result.examTaskTitle());

            verify(examTaskRepository).save(examTask);
        }

        @Test
        void testUpdateExamTaskNegative() {

            Long examId = 1L;
            UpdateExamTaskDto updateDto = new UpdateExamTaskDto("Updated Title");

            when(examTaskRepository.findById(examId)).thenReturn(Optional.empty());

            assertThrows(RestException.class, () -> examTasksServices.updateExamTask(examId, updateDto));

            verify(examTaskRepository, never()).save(any(ExamTask.class));
        }
    }

    @Nested
    @DisplayName("add testType to examTask by newTestType")
    class AddTestTypeToExamTaskByNewTestType {

        @Test
        void testAddTestTypeToExamTaskPositive() {

            when(examTaskRepository.findById(EXAM_TASK_Id)).thenReturn(Optional.of(EXAM_TASK));

            ExamTask examTask = examTasksServices.getByIdOrThrow(EXAM_TASK_Id);

            when(testTypeRepository.findById(TEST_TYPE_ID)).thenReturn(Optional.of(TEST_TYPE));

            TestType addedTestType = testTypeService.getByIdOrThrow(QUESTION_DTO.id());

            examTask.setTestTypes(new ArrayList<>());

            when(examTaskRepository.findById(1L)).thenReturn(Optional.of(EXAM_TASK));

            when(typeService.addTestType(any(NewTestTypeDto.class))).thenReturn(TEST_TYPE_DTO);

            when(typeService.getByIdOrThrow(TEST_TYPE_DTO.id())).thenReturn(addedTestType);

            List<TestType> updatedTestType = examTasksServices.addTestTypeToExamTask(EXAM_TASK_Id, NEW_TEST_TYPE_DTO);

            assertTrue(updatedTestType.contains(addedTestType));

            verify(examTaskRepository).save(EXAM_TASK);
        }

        @Test
        void testAddTestTypeToExamTaskNegative() {

            when(examTaskRepository.findById(anyLong())).thenThrow(new EntityNotFoundException());

            assertThrows(EntityNotFoundException.class, () ->
                    examTasksServices.addTestTypeToExamTask(EXAM_TASK_Id, NEW_TEST_TYPE_DTO));
        }
    }

    @Nested
    @DisplayName("add testType to examTask by testType_id")
    class AddTestTypeToExamTaskByTestTypeId {

        @Test
        void testAddTestTypeToExamTaskByTestTypeId_Positive() {

            when(examTaskRepository.findById(EXAM_TASK_Id)).thenReturn(Optional.of(EXAM_TASK));

            when(testTypeRepository.findById(TEST_TYPE_ID)).thenReturn(Optional.of(TEST_TYPE));

            ExamTask examTask = examTasksServices.getByIdOrThrow(EXAM_TASK_Id);

            examTask.setTestTypes(new ArrayList<>());

            TestType addedTestType = testTypeService.getByIdOrThrow(TEST_TYPE_ID);

            when(typeService.getByIdOrThrow(TEST_TYPE_ID)).thenReturn(addedTestType);

            List<TestType> updatedTestType = examTasksServices.addTestTypeToExamTask(EXAM_TASK_Id, TEST_TYPE_ID);

            assertTrue(updatedTestType.contains(addedTestType));

            verify(examTaskRepository, times(1)).save(examTask);
        }

        @Test
        void testAddTestTypeToExamTaskByTestTypeId_Negative() {

            Long invalidExamTaskId = 2L;
            Long testTypeId = 1L;

            when(examTaskRepository.findById(invalidExamTaskId))
                    .thenThrow(new EntityNotFoundException("Exam task not found"));

            assertThrows(EntityNotFoundException.class,
                    () -> examTasksServices.addTestTypeToExamTask(invalidExamTaskId, testTypeId));
        }
    }
}
