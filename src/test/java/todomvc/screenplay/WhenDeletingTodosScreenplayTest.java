package todomvc.screenplay;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.ensure.Ensure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import todomvc.screenplay.questions.TheRemainingCount;
import todomvc.screenplay.questions.TheTodoItem;
import todomvc.screenplay.questions.TheVisibleTodos;
import todomvc.screenplay.tasks.AddATodoItem;
import todomvc.screenplay.tasks.Delete;
import todomvc.screenplay.tasks.OpenTodoMvcApp;

/**
 * Screenplay-based tests for deleting todo items.
 *
 * Demonstrates using the Screenplay pattern with Playwright for:
 * - Deleting individual todos
 * - Verifying todo removal
 */
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("When deleting todos (Screenplay)")
class WhenDeletingTodosScreenplayTest extends ScreenplayPlaywrightTest {

    @BeforeEach
    void setupTodos() {
        toby.attemptsTo(
            OpenTodoMvcApp.onTheTodoMvcHomePage(),
            AddATodoItem.withItems("Buy milk", "Walk the dog", "Do laundry")
        );
    }

    @Test
    @DisplayName("should delete a todo item")
    void shouldDeleteTodoItem() {
        toby.attemptsTo(
            Delete.theTodoItem("Walk the dog"),
            Ensure.that(TheTodoItem.called("Walk the dog").exists()).isFalse(),
            Ensure.that(TheVisibleTodos.displayed())
                .containsExactly("Buy milk", "Do laundry")
        );
    }

    @Test
    @DisplayName("should update the remaining count after deleting")
    void shouldUpdateRemainingCountAfterDeleting() {
        toby.attemptsTo(
            Ensure.that(TheRemainingCount.value()).isEqualTo(3),
            Delete.theTodoItem("Buy milk"),
            Ensure.that(TheRemainingCount.value()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("should delete multiple todo items")
    void shouldDeleteMultipleTodoItems() {
        toby.attemptsTo(
            Delete.theTodoItem("Buy milk"),
            Delete.theTodoItem("Do laundry"),
            Ensure.that(TheVisibleTodos.displayed()).containsExactly("Walk the dog"),
            Ensure.that(TheVisibleTodos.count()).isEqualTo(1)
        );
    }
}
