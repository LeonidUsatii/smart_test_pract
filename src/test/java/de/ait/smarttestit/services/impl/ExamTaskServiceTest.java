package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.exam_task.ExamTaskDto;
import de.ait.smarttestit.dto.exam_task.UpdateExamTaskDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.ExamTask;
import de.ait.smarttestit.repositories.ExamTaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("examTask CRUD:")
public class ExamTaskServiceTest {

    private static final Long EXAM_TASK_Id = 1L;

    private static final String EXAM_TASK_TITLE = "Java beginner level";

    private static final ExamTask EXAM_TASK = new ExamTask(EXAM_TASK_Id, EXAM_TASK_TITLE);

    @Mock
    private ExamTaskRepository examTaskRepository;

    @InjectMocks
    private ExamTaskServiceImpl examTasksServices;

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
    @DisplayName("save examTask")
    class saveExamTask {

        @Test
        void testSaveExamTaskPositive() {

            when(examTaskRepository.existsById(any(Long.class))).thenReturn(false);
            when(examTaskRepository.save(any(ExamTask.class))).thenReturn(EXAM_TASK);

            ExamTask savedExamTask = examTasksServices.save(EXAM_TASK);

            assertNotNull(savedExamTask);
            assertEquals(EXAM_TASK.getId(), savedExamTask.getId());
            verify(examTaskRepository).save(any(ExamTask.class));
        }

        @Test
        void testSaveExamTaskNegative() {

            when(examTaskRepository.existsById(any(Long.class))).thenReturn(true);

            assertThrows(DataIntegrityViolationException.class, () -> examTasksServices.save(EXAM_TASK));

            verify(examTaskRepository, never()).save(any(ExamTask.class));
        }
    }
}
