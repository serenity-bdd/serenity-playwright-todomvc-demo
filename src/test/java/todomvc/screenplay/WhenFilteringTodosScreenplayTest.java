package todomvc.screenplay;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.ensure.Ensure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import todomvc.screenplay.questions.TheCurrentFilter;
import todomvc.screenplay.questions.TheVisibleTodos;
import todomvc.screenplay.tasks.AddATodoItem;
import todomvc.screenplay.tasks.Complete;
import todomvc.screenplay.tasks.FilterTodos;
import todomvc.screenplay.tasks.OpenTodoMvcApp;

/**
 * Screenplay-based tests for filtering todo items.
 *
 * Demonstrates using the Screenplay pattern with Playwright for:
 * - Filtering by All, Active, and Completed
 * - Verifying filter state
 */
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("When filtering todos (Screenplay)")
class WhenFilteringTodosScreenplayTest extends ScreenplayPlaywrightTest {

    @BeforeEach
    void setupTodos() {
        toby.attemptsTo(
            OpenTodoMvcApp.onTheTodoMvcHomePage(),
            AddATodoItem.withItems("Buy milk", "Walk the dog", "Do laundry"),
            Complete.todoItem("Walk the dog")
        );
    }

    @Test
    @DisplayName("should show all todos by default")
    void shouldShowAllTodosByDefault() {
        toby.attemptsTo(
            Ensure.that(TheCurrentFilter.selected()).isEqualTo("All"),
            Ensure.that(TheVisibleTodos.displayed())
                .containsExactly("Buy milk", "Walk the dog", "Do laundry")
        );
    }

    @Test
    @DisplayName("should filter to show only active todos")
    void shouldFilterToShowOnlyActiveTodos() {
        toby.attemptsTo(
            FilterTodos.toShowActive(),
            Ensure.that(TheCurrentFilter.selected()).isEqualTo("Active"),
            Ensure.that(TheVisibleTodos.displayed())
                .containsExactly("Buy milk", "Do laundry")
        );
    }

    @Test
    @DisplayName("should filter to show only completed todos")
    void shouldFilterToShowOnlyCompletedTodos() {
        toby.attemptsTo(
            FilterTodos.toShowCompleted(),
            Ensure.that(TheCurrentFilter.selected()).isEqualTo("Completed"),
            Ensure.that(TheVisibleTodos.displayed()).containsExactly("Walk the dog")
        );
    }

    @Test
    @DisplayName("should return to all todos from filtered view")
    void shouldReturnToAllTodosFromFilteredView() {
        toby.attemptsTo(
            FilterTodos.toShowActive(),
            FilterTodos.toShowAll(),
            Ensure.that(TheCurrentFilter.selected()).isEqualTo("All"),
            Ensure.that(TheVisibleTodos.displayed())
                .containsExactly("Buy milk", "Walk the dog", "Do laundry")
        );
    }

    @Test
    @DisplayName("should switch between filters")
    void shouldSwitchBetweenFilters() {
        toby.attemptsTo(
            // Filter to Active
            FilterTodos.toShowActive(),
            Ensure.that(TheVisibleTodos.count()).isEqualTo(2),

            // Filter to Completed
            FilterTodos.toShowCompleted(),
            Ensure.that(TheVisibleTodos.count()).isEqualTo(1),

            // Filter back to All
            FilterTodos.toShowAll(),
            Ensure.that(TheVisibleTodos.count()).isEqualTo(3)
        );
    }
}
