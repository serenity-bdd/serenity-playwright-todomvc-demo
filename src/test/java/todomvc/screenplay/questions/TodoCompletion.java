package todomvc.screenplay.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import net.serenitybdd.annotations.Step;
import todomvc.screenplay.ui.TodoList;

/**
 * Question about whether a specific todo item is completed.
 *
 * Usage:
 *   actor.asksFor(TodoCompletion.of("Buy milk"));
 */
public class TodoCompletion implements Question<Boolean> {

    private final String todoItem;

    public TodoCompletion(String todoItem) {
        this.todoItem = todoItem;
    }

    @Override
    @Step("{0} checks if '#todoItem' is completed")
    public Boolean answeredBy(Actor actor) {
        return BrowseTheWebWithPlaywright.as(actor)
            .getCurrentPage()
            .locator(TodoList.todoItemCalled(todoItem).asSelector())
            .getAttribute("class")
            .contains("completed");
    }

    public static TodoCompletion of(String todoItem) {
        return new TodoCompletion(todoItem);
    }
}
