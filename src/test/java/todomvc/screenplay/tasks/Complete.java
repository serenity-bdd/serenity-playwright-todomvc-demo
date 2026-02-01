package todomvc.screenplay.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.playwright.interactions.Click;
import net.serenitybdd.annotations.Step;
import todomvc.screenplay.ui.TodoList;

/**
 * Complete (check) a todo item.
 *
 * Usage:
 *   actor.attemptsTo(Complete.todoItem("Buy milk"));
 */
public class Complete implements Task {

    private final String todoItem;

    public Complete(String todoItem) {
        this.todoItem = todoItem;
    }

    @Override
    @Step("{0} completes the todo item '#todoItem'")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Click.on(TodoList.checkboxFor(todoItem))
        );
    }

    public static Complete todoItem(String todoItem) {
        return new Complete(todoItem);
    }

    public static Complete theTodoItem(String todoItem) {
        return new Complete(todoItem);
    }
}
