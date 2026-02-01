package todomvc.screenplay.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.playwright.interactions.Click;
import net.serenitybdd.annotations.Step;
import todomvc.screenplay.ui.TodoList;

/**
 * Clear all completed todo items.
 *
 * Usage:
 *   actor.attemptsTo(ClearCompletedTodos.fromTheList());
 */
public class ClearCompletedTodos implements Task {

    @Override
    @Step("{0} clears all completed todos")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Click.on(TodoList.CLEAR_COMPLETED_BUTTON)
        );
    }

    public static ClearCompletedTodos fromTheList() {
        return new ClearCompletedTodos();
    }
}
