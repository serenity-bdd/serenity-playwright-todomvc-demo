package todomvc.screenplay.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import net.serenitybdd.annotations.Step;
import todomvc.screenplay.ui.TodoList;

import java.util.Collection;

/**
 * Question about the currently visible todo items.
 *
 * Usage:
 *   actor.attemptsTo(Ensure.that(TheVisibleTodos.displayed()).containsExactly("a", "b"));
 *   actor.attemptsTo(Ensure.that(TheVisibleTodos.count()).isEqualTo(3));
 */
public class TheVisibleTodos {

    public static Question<Collection<String>> displayed() {
        return Question.about("the visible todos").answeredBy(
            actor -> BrowseTheWebWithPlaywright.as(actor)
                .getCurrentPage()
                .locator(TodoList.TODO_ITEM_LABELS.asSelector())
                .allTextContents()
        );
    }

    public static Question<Integer> count() {
        return Question.about("visible todo count").answeredBy(
            actor -> BrowseTheWebWithPlaywright.as(actor)
                .getCurrentPage()
                .locator(TodoList.TODO_ITEMS.asSelector())
                .count()
        );
    }
}
