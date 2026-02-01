package todomvc.screenplay.questions;

import com.microsoft.playwright.Locator;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import net.serenitybdd.annotations.Step;
import todomvc.screenplay.ui.TodoList;

/**
 * Question about the visibility of the "Clear completed" button.
 *
 * Usage:
 *   actor.asksFor(TheClearCompletedButton.isVisible());
 */
public class TheClearCompletedButton implements Question<Boolean> {

    @Override
    @Step("{0} checks if the Clear Completed button is visible")
    public Boolean answeredBy(Actor actor) {
        Locator button = BrowseTheWebWithPlaywright.as(actor)
            .getCurrentPage()
            .locator(TodoList.CLEAR_COMPLETED_BUTTON.asSelector());

        // Check if the button exists and is visible
        // The button is only rendered when there are completed items
        return button.count() > 0 && button.isVisible();
    }

    public static TheClearCompletedButton isVisible() {
        return new TheClearCompletedButton();
    }
}
