package todomvc.screenplay.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import net.serenitybdd.annotations.Step;
import todomvc.screenplay.ui.TodoList;

/**
 * Question about the currently selected filter.
 *
 * Usage:
 *   actor.asksFor(TheCurrentFilter.selected());
 */
public class TheCurrentFilter implements Question<String> {

    @Override
    @Step("{0} checks the current filter")
    public String answeredBy(Actor actor) {
        return BrowseTheWebWithPlaywright.as(actor)
            .getCurrentPage()
            .locator(TodoList.SELECTED_FILTER.asSelector())
            .textContent();
    }

    public static TheCurrentFilter selected() {
        return new TheCurrentFilter();
    }
}
