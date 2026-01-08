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
 * Tests for filtering todo items in the TodoMVC application.
 */
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("When filtering todos")
class WhenFilteringTodosTest extends SerenityPlaywrightTest {

    @Steps
    TodoSteps todo;

    @BeforeEach
    void setUp() {
        todo.setPage(page);
    }

    @Test
    @DisplayName("should show all todos by default")
    void shouldShowAllTodosByDefault() {
        todo.openApplication();
        todo.addTodos("Active task", "Completed task");
        todo.completeTodo("Completed task");

        assertThat(todo.selectedFilter()).isEqualTo("All");
        assertThat(todo.visibleTodoCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("should filter to show only active todos")
    void shouldFilterToShowOnlyActiveTodos() {
        todo.openApplication();
        todo.addTodos("Active 1", "Completed 1", "Active 2");
        todo.completeTodo("Completed 1");
        todo.filterActive();

        assertThat(todo.selectedFilter()).isEqualTo("Active");
        assertThat(todo.visibleTodoCount()).isEqualTo(2);
        assertThat(todo.visibleTodos()).containsExactly("Active 1", "Active 2");
    }

    @Test
    @DisplayName("should filter to show only completed todos")
    void shouldFilterToShowOnlyCompletedTodos() {
        todo.openApplication();
        todo.addTodos("Active 1", "Completed 1", "Completed 2");
        todo.completeTodo("Completed 1");
        todo.completeTodo("Completed 2");
        todo.filterCompleted();

        assertThat(todo.selectedFilter()).isEqualTo("Completed");
        assertThat(todo.visibleTodoCount()).isEqualTo(2);
        assertThat(todo.visibleTodos()).containsExactly("Completed 1", "Completed 2");
    }

    @Test
    @DisplayName("should switch back to all filter")
    void shouldSwitchBackToAllFilter() {
        todo.openApplication();
        todo.addTodos("Task 1", "Task 2");
        todo.completeTodo("Task 1");
        todo.filterActive();

        assertThat(todo.visibleTodoCount()).isEqualTo(1);

        todo.filterAll();

        assertThat(todo.selectedFilter()).isEqualTo("All");
        assertThat(todo.visibleTodoCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("should show no todos when filtering active with all completed")
    void shouldShowNoTodosWhenFilteringActiveWithAllCompleted() {
        todo.openApplication();
        todo.addTodos("Task 1", "Task 2");
        todo.toggleAll();
        todo.filterActive();

        assertThat(todo.visibleTodoCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("should show no todos when filtering completed with none completed")
    void shouldShowNoTodosWhenFilteringCompletedWithNoneCompleted() {
        todo.openApplication();
        todo.addTodos("Task 1", "Task 2");
        todo.filterCompleted();

        assertThat(todo.visibleTodoCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("should maintain remaining count regardless of filter")
    void shouldMaintainRemainingCountRegardlessOfFilter() {
        todo.openApplication();
        todo.addTodos("Active 1", "Completed 1", "Active 2");
        todo.completeTodo("Completed 1");

        assertThat(todo.remainingCount()).isEqualTo(2);

        todo.filterCompleted();
        assertThat(todo.remainingCount()).isEqualTo(2);

        todo.filterActive();
        assertThat(todo.remainingCount()).isEqualTo(2);
    }
}