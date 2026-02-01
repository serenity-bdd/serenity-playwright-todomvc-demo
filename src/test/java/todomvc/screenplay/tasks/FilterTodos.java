package todomvc.screenplay.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.playwright.interactions.Click;
import net.serenitybdd.annotations.Step;
import todomvc.screenplay.ui.TodoList;

/**
 * Filter the todo list by status.
 *
 * Usage:
 *   actor.attemptsTo(FilterTodos.toShowAll());
 *   actor.attemptsTo(FilterTodos.toShowActive());
 *   actor.attemptsTo(FilterTodos.toShowCompleted());
 *   actor.attemptsTo(FilterTodos.toShow("Active"));
 */
public class FilterTodos {

    public static Task toShowAll() {
        return new FilterToShowAll();
    }

    public static Task toShowActive() {
        return new FilterToShowActive();
    }

    public static Task toShowCompleted() {
        return new FilterToShowCompleted();
    }

    /**
     * Filter by name - useful for Cucumber scenarios.
     * @param filterType "All", "Active", or "Completed"
     */
    public static Task toShow(String filterType) {
        return switch (filterType.toLowerCase()) {
            case "all" -> toShowAll();
            case "active" -> toShowActive();
            case "completed" -> toShowCompleted();
            default -> throw new IllegalArgumentException("Unknown filter type: " + filterType);
        };
    }
}

class FilterToShowAll implements Task {
    @Override
    @Step("{0} filters to show all todos")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(TodoList.ALL_FILTER));
    }
}

class FilterToShowActive implements Task {
    @Override
    @Step("{0} filters to show active todos")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(TodoList.ACTIVE_FILTER));
    }
}

class FilterToShowCompleted implements Task {
    @Override
    @Step("{0} filters to show completed todos")
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Click.on(TodoList.COMPLETED_FILTER));
    }
}
