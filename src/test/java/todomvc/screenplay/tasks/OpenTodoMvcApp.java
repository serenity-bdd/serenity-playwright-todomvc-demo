package todomvc.screenplay.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import net.serenitybdd.screenplay.playwright.interactions.Open;
import net.serenitybdd.annotations.Step;

/**
 * Open the TodoMVC application.
 * Clears localStorage to ensure a clean state for each test.
 */
public class OpenTodoMvcApp implements Task {

    private static final String TODO_MVC_URL = "https://todomvc.com/examples/react/dist/";

    @Override
    @Step("{0} opens the TodoMVC application")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Open.url(TODO_MVC_URL)
        );

        // Clear localStorage to ensure a clean slate for each test
        // TodoMVC stores todos in localStorage, so this prevents state leakage between tests
        var page = BrowseTheWebWithPlaywright.as(actor).getCurrentPage();
        page.evaluate("() => localStorage.clear()");

        // Reload to apply the cleared state
        page.reload();
    }

    public static OpenTodoMvcApp onTheTodoMvcHomePage() {
        return new OpenTodoMvcApp();
    }
}
