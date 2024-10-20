package org.example.server;

import org.example.server.controller.TaskController;
import org.example.server.model.Task;
import org.example.server.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerApplicationTests {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTask() {
        Task task = new Task();
        task.setId(1L);
        task.setDescription("Test Task");
        when(taskService.getTaskById(anyLong())).thenReturn(Mono.just(task));

        Mono<Task> result = taskController.getTask(1L);
        Task resultTask = result.block();

        assertEquals(1L, resultTask.getId());
        assertEquals("Test Task", resultTask.getDescription());
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setDescription("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setDescription("Task 2");

        when(taskService.getAllTasks()).thenReturn(Flux.just(task1, task2));

        Flux<Task> result = taskController.getAllTasks();
        Task[] tasks = result.toStream().toArray(Task[]::new);

        assertEquals(2, tasks.length);
        assertEquals("Task 1", tasks[0].getDescription());
        assertEquals("Task 2", tasks[1].getDescription());
    }

    @Test
    void testSetAlert() {
        Task task = new Task();
        task.setDescription("New Task");

        when(taskService.createTask(anyString())).thenReturn(Mono.empty());

        Mono<Void> result = taskController.setAlert(Mono.just(task));
        result.block(); // Ensure that the Mono is executed

        verify(taskService).createTask("New Task");
    }

    @Test
    void testBatchCreate() {
        Task task1 = new Task();
        task1.setDescription("Task 1");

        Task task2 = new Task();
        task2.setDescription("Task 2");

        when(taskService.createTask(anyString())).thenReturn(Mono.just(new Task()));

        Flux<Task> tasks = Flux.just(task1, task2);
        Flux<Task> result = taskController.batchCreate(tasks);

        result.collectList().block(); // Ensure that the Flux is executed

        verify(taskService).createTask("Task 1");
        verify(taskService).createTask("Task 2");
    }
}
