package todomvc;

import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import todomvc.steps.TodoSteps;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for completing todo items in the TodoMVC application.
 */
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("When completing todos")
class WhenCompletingTodosTest extends SerenityPlaywrightTest {

    @Steps
    TodoSteps todo;

    @BeforeEach
    void setUp() {
        todo.setPage(page);
    }

    @Test
    @DisplayName("should mark a todo as completed")
    void shouldMarkTodoAsCompleted() {
        todo.openApplication();
        todo.addTodo("Buy milk");
        todo.completeTodo("Buy milk");

        assertThat(todo.todoIsCompleted("Buy milk")).isTrue();
    }

    @Test
    @DisplayName("should decrease remaining count when completing a todo")
    void shouldDecreaseRemainingCountWhenCompleting() {
        todo.openApplication();
        todo.addTodos("Task 1", "Task 2", "Task 3");

        assertThat(todo.remainingCount()).isEqualTo(3);

        todo.completeTodo("Task 2");

        assertThat(todo.remainingCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("should toggle all todos to completed")
    void shouldToggleAllTodosToCompleted() {
        todo.openApplication();
        todo.addTodos("Task 1", "Task 2", "Task 3");
        todo.toggleAll();

        assertThat(todo.todoIsCompleted("Task 1")).isTrue();
        assertThat(todo.todoIsCompleted("Task 2")).isTrue();
        assertThat(todo.todoIsCompleted("Task 3")).isTrue();
        assertThat(todo.remainingCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("should toggle all todos back to active")
    void shouldToggleAllTodosBackToActive() {
        todo.openApplication();
        todo.addTodos("Task 1", "Task 2");
        todo.toggleAll();

        assertThat(todo.remainingCount()).isEqualTo(0);

        todo.toggleAll();

        assertThat(todo.todoIsCompleted("Task 1")).isFalse();
        assertThat(todo.todoIsCompleted("Task 2")).isFalse();
        assertThat(todo.remainingCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("should uncomplete a completed todo when clicked again")
    void shouldUncompleteTodoWhenClickedAgain() {
        todo.openApplication();
        todo.addTodo("Toggle me");
        todo.completeTodo("Toggle me");

        assertThat(todo.todoIsCompleted("Toggle me")).isTrue();

        todo.completeTodo("Toggle me");

        assertThat(todo.todoIsCompleted("Toggle me")).isFalse();
    }
}