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
 * Tests for adding todo items in the TodoMVC application.
 */
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("When adding todos")
class WhenAddingTodosTest extends SerenityPlaywrightTest {

    @Steps
    TodoSteps todo;

    @BeforeEach
    void preparePages() {
        todo.setPage(page);
    }

    @Test
    @DisplayName("should add a single todo item")
    void shouldAddSingleTodoItem() {
        todo.openApplication();
        todo.addTodo("Buy milk");

        assertThat(todo.visibleTodoCount()).isEqualTo(1);
        assertThat(todo.todoExists("Buy milk")).isTrue();
    }

    @Test
    @DisplayName("should add multiple todo items")
    void shouldAddMultipleTodoItems() {
        todo.openApplication();
        todo.addTodos("Buy milk", "Walk the dog", "Do laundry");

        assertThat(todo.visibleTodos()).containsExactly(
                "Buy milk",
                "Walk the dog",
                "Do laundry");
    }

    @Test
    @DisplayName("should update remaining count when adding todos")
    void shouldUpdateRemainingCountWhenAddingTodos() {
        todo.openApplication();
        todo.addTodo("Task 1");

        assertThat(todo.remainingCount()).isEqualTo(1);

        todo.addTodo("Task 2");

        assertThat(todo.remainingCount()).isEqualTo(2);

        todo.addTodo("Task 3");

        assertThat(todo.remainingCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("should preserve order of added todos")
    void shouldPreserveOrderOfAddedTodos() {
        todo.openApplication();
        todo.addTodo("First");
        todo.addTodo("Second");
        todo.addTodo("Third");

        assertThat(todo.visibleTodos()).containsExactly("First", "Second", "Third");
    }
}