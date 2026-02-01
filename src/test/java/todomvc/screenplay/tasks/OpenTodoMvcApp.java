package todomvc.screenplay.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.playwright.interactions.Open;
import net.serenitybdd.annotations.Step;

/**
 * Open the TodoMVC application.
 */
public class OpenTodoMvcApp implements Task {

    private static final String TODO_MVC_URL = "https://todomvc.com/examples/react/dist/";

    @Override
    @Step("{0} opens the TodoMVC application")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Open.url(TODO_MVC_URL)
        );
    }

    public static OpenTodoMvcApp onTheTodoMvcHomePage() {
        return new OpenTodoMvcApp();
    }
}
