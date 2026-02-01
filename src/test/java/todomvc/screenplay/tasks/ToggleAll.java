package todomvc.screenplay.tasks;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import net.serenitybdd.annotations.Step;

/**
 * Toggle all todo items to completed or active state.
 *
 * Usage:
 *   actor.attemptsTo(ToggleAll.todos());
 */
public class ToggleAll implements Task {

    @Override
    @Step("{0} toggles all todos")
    public <T extends Actor> void performAs(T actor) {
        // The toggle-all checkbox is visually hidden, so we need to use force click
        Page page = BrowseTheWebWithPlaywright.as(actor).getCurrentPage();
        page.locator("#toggle-all").click(new Locator.ClickOptions().setForce(true));
        BrowseTheWebWithPlaywright.as(actor).notifyScreenChange();
    }

    public static ToggleAll todos() {
        return new ToggleAll();
    }

    public static ToggleAll todoItems() {
        return new ToggleAll();
    }
}
