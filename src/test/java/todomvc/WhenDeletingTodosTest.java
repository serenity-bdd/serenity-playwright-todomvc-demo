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
 * Tests for deleting todo items in the TodoMVC application.
 */
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("When deleting todos")
class WhenDeletingTodosTest extends SerenityPlaywrightTest {

    @Steps
    TodoSteps todo;

    @BeforeEach
    void setUp() {
        todo.setPage(page);
    }

    @Test
    @DisplayName("should delete a single todo")
    void shouldDeleteSingleTodo() {
        todo.openApplication();
        todo.addTodo("Delete me");
        todo.deleteTodo("Delete me");

        assertThat(todo.visibleTodoCount()).isEqualTo(0);
        assertThat(todo.todoExists("Delete me")).isFalse();
    }

    @Test
    @DisplayName("should delete one todo and keep others")
    void shouldDeleteOneTodoAndKeepOthers() {
        todo.openApplication();
        todo.addTodos("Keep me", "Delete me", "Keep me too");
        todo.deleteTodo("Delete me");

        assertThat(todo.visibleTodoCount()).isEqualTo(2);
        assertThat(todo.visibleTodos()).containsExactly("Keep me", "Keep me too");
    }

    @Test
    @DisplayName("should clear all completed todos")
    void shouldClearAllCompletedTodos() {
        todo.openApplication();
        todo.addTodos("Active 1", "Complete 1", "Active 2", "Complete 2");
        todo.completeTodo("Complete 1");
        todo.completeTodo("Complete 2");
        todo.clearCompleted();

        assertThat(todo.visibleTodoCount()).isEqualTo(2);
        assertThat(todo.visibleTodos()).containsExactly("Active 1", "Active 2");
    }

    @Test
    @DisplayName("should hide clear completed button after clearing")
    void shouldHideClearCompletedButtonAfterClearing() {
        todo.openApplication();
        todo.addTodo("Complete me");
        todo.completeTodo("Complete me");

        assertThat(todo.clearCompletedIsVisible()).isTrue();

        todo.clearCompleted();

        assertThat(todo.clearCompletedIsVisible()).isFalse();
    }

    @Test
    @DisplayName("should update remaining count after deleting")
    void shouldUpdateRemainingCountAfterDeleting() {
        todo.openApplication();
        todo.addTodos("Task 1", "Task 2", "Task 3");

        assertThat(todo.remainingCount()).isEqualTo(3);

        todo.deleteTodo("Task 2");

        assertThat(todo.remainingCount()).isEqualTo(2);
    }

}