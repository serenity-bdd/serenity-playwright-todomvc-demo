package todomvc.screenplay;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.ensure.Ensure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import todomvc.screenplay.questions.TheClearCompletedButton;
import todomvc.screenplay.questions.TheRemainingCount;
import todomvc.screenplay.questions.TheTodoItem;
import todomvc.screenplay.questions.TheVisibleTodos;
import todomvc.screenplay.tasks.AddATodoItem;
import todomvc.screenplay.tasks.ClearCompletedTodos;
import todomvc.screenplay.tasks.Complete;
import todomvc.screenplay.tasks.OpenTodoMvcApp;
import todomvc.screenplay.tasks.ToggleAll;

/**
 * Screenplay-based tests for completing todo items.
 *
 * Demonstrates using the Screenplay pattern with Playwright for:
 * - Completing individual todos
 * - Toggling all todos
 * - Clearing completed todos
 */
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("When completing todos (Screenplay)")
class WhenCompletingTodosScreenplayTest extends ScreenplayPlaywrightTest {

    @BeforeEach
    void setupTodos() {
        toby.attemptsTo(
            OpenTodoMvcApp.onTheTodoMvcHomePage(),
            AddATodoItem.withItems("Buy milk", "Walk the dog", "Do laundry")
        );
    }

    @Test
    @DisplayName("should mark a todo as completed")
    void shouldMarkTodoAsCompleted() {
        toby.attemptsTo(
            Complete.todoItem("Buy milk"),
            Ensure.that(TheTodoItem.called("Buy milk").isCompleted()).isTrue(),
            Ensure.that(TheRemainingCount.value()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("should show the Clear Completed button when todos are completed")
    void shouldShowClearCompletedButton() {
        toby.attemptsTo(
            Complete.todoItem("Buy milk"),
            Ensure.that(TheClearCompletedButton.isVisible()).isTrue()
        );
    }

    @Test
    @DisplayName("should toggle all todos to completed")
    void shouldToggleAllTodosToCompleted() {
        toby.attemptsTo(
            ToggleAll.todos(),
            Ensure.that(TheTodoItem.called("Buy milk").isCompleted()).isTrue(),
            Ensure.that(TheTodoItem.called("Walk the dog").isCompleted()).isTrue(),
            Ensure.that(TheTodoItem.called("Do laundry").isCompleted()).isTrue(),
            Ensure.that(TheRemainingCount.value()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("should toggle all completed todos back to active")
    void shouldToggleCompletedTodosBackToActive() {
        toby.attemptsTo(
            ToggleAll.todos(),  // Complete all
            ToggleAll.todos(),  // Activate all
            Ensure.that(TheTodoItem.called("Buy milk").isCompleted()).isFalse(),
            Ensure.that(TheTodoItem.called("Walk the dog").isCompleted()).isFalse(),
            Ensure.that(TheTodoItem.called("Do laundry").isCompleted()).isFalse(),
            Ensure.that(TheRemainingCount.value()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("should clear completed todos")
    void shouldClearCompletedTodos() {
        toby.attemptsTo(
            Complete.todoItem("Buy milk"),
            Complete.todoItem("Walk the dog"),
            ClearCompletedTodos.fromTheList(),
            Ensure.that(TheVisibleTodos.displayed()).containsExactly("Do laundry"),
            Ensure.that(TheVisibleTodos.count()).isEqualTo(1)
        );
    }
}
