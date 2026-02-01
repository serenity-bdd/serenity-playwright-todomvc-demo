package todomvc.screenplay;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.ensure.Ensure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import todomvc.screenplay.questions.TheRemainingCount;
import todomvc.screenplay.questions.TheTodoItem;
import todomvc.screenplay.questions.TheVisibleTodos;
import todomvc.screenplay.tasks.AddATodoItem;
import todomvc.screenplay.tasks.OpenTodoMvcApp;

/**
 * Screenplay-based tests for adding todo items.
 *
 * Demonstrates using the Screenplay pattern with Playwright for:
 * - Adding single and multiple todos
 * - Verifying todo counts
 * - Checking todo visibility
 */
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("When adding todos (Screenplay)")
class WhenAddingTodosScreenplayTest extends ScreenplayPlaywrightTest {

    @Test
    @DisplayName("should add a single todo item")
    void shouldAddSingleTodoItem() {
        toby.attemptsTo(
            OpenTodoMvcApp.onTheTodoMvcHomePage(),
            AddATodoItem.called("Buy milk"),
            Ensure.that(TheVisibleTodos.count()).isEqualTo(1),
            Ensure.that(TheTodoItem.called("Buy milk").exists()).isTrue()
        );
    }

    @Test
    @DisplayName("should add multiple todo items")
    void shouldAddMultipleTodoItems() {
        toby.attemptsTo(
            OpenTodoMvcApp.onTheTodoMvcHomePage(),
            AddATodoItem.withItems("Buy milk", "Walk the dog", "Do laundry"),
            Ensure.that(TheVisibleTodos.displayed())
                .containsExactly("Buy milk", "Walk the dog", "Do laundry")
        );
    }

    @Test
    @DisplayName("should update remaining count when adding todos")
    void shouldUpdateRemainingCountWhenAddingTodos() {
        toby.attemptsTo(
            OpenTodoMvcApp.onTheTodoMvcHomePage(),
            AddATodoItem.called("Task 1"),
            Ensure.that(TheRemainingCount.value()).isEqualTo(1),

            AddATodoItem.called("Task 2"),
            Ensure.that(TheRemainingCount.value()).isEqualTo(2),

            AddATodoItem.called("Task 3"),
            Ensure.that(TheRemainingCount.value()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("should preserve order of added todos")
    void shouldPreserveOrderOfAddedTodos() {
        toby.attemptsTo(
            OpenTodoMvcApp.onTheTodoMvcHomePage(),
            AddATodoItem.called("First"),
            AddATodoItem.called("Second"),
            AddATodoItem.called("Third"),
            Ensure.that(TheVisibleTodos.displayed())
                .containsExactly("First", "Second", "Third")
        );
    }
}
