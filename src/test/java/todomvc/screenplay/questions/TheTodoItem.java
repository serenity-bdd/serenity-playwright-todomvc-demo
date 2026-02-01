package todomvc.screenplay.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import todomvc.screenplay.ui.TodoList;

/**
 * Questions about a specific todo item.
 *
 * Usage:
 *   actor.asksFor(TheTodoItem.called("Buy milk").isVisible());
 *   actor.asksFor(TheTodoItem.called("Buy milk").isCompleted());
 */
public class TheTodoItem {

    private final String todoItem;

    public TheTodoItem(String todoItem) {
        this.todoItem = todoItem;
    }

    public static TheTodoItem called(String todoItem) {
        return new TheTodoItem(todoItem);
    }

    public Question<Boolean> isVisible() {
        String item = todoItem;
        return Question.about("whether '" + item + "' is visible").answeredBy(
            actor -> BrowseTheWebWithPlaywright.as(actor)
                .getCurrentPage()
                .locator(TodoList.todoItemCalled(item).asSelector())
                .isVisible()
        );
    }

    public Question<Boolean> isCompleted() {
        return TodoCompletion.of(todoItem);
    }

    public Question<Boolean> exists() {
        String item = todoItem;
        return Question.about("whether '" + item + "' exists").answeredBy(
            actor -> BrowseTheWebWithPlaywright.as(actor)
                .getCurrentPage()
                .locator(TodoList.todoItemCalled(item).asSelector())
                .count() > 0
        );
    }
}
