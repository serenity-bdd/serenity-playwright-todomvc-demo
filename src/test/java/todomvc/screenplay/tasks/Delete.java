package todomvc.screenplay.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.playwright.interactions.Click;
import net.serenitybdd.screenplay.playwright.interactions.Hover;
import net.serenitybdd.annotations.Step;
import todomvc.screenplay.ui.TodoList;

/**
 * Delete a todo item by clicking the destroy button.
 *
 * Usage:
 *   actor.attemptsTo(Delete.theTodoItem("Buy milk"));
 */
public class Delete implements Task {

    private final String todoItem;

    public Delete(String todoItem) {
        this.todoItem = todoItem;
    }

    @Override
    @Step("{0} deletes the todo item '#todoItem'")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Hover.over(TodoList.todoItemCalled(todoItem)),
            Click.on(TodoList.deleteButtonFor(todoItem))
        );
    }

    public static Delete theTodoItem(String todoItem) {
        return new Delete(todoItem);
    }

    public static Delete todoItem(String todoItem) {
        return new Delete(todoItem);
    }
}
