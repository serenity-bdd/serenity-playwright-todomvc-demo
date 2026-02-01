package todomvc.screenplay.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.playwright.interactions.Enter;
import net.serenitybdd.screenplay.playwright.interactions.Press;
import net.serenitybdd.annotations.Step;
import todomvc.screenplay.ui.TodoList;

import java.util.Arrays;
import java.util.List;

/**
 * Add one or more todo items to the list.
 *
 * Usage:
 *   actor.attemptsTo(AddATodoItem.called("Buy milk"));
 *   actor.attemptsTo(AddATodoItem.withItems("Buy milk", "Walk the dog"));
 */
public class AddATodoItem implements Task {

    private final List<String> todoItems;

    public AddATodoItem(List<String> todoItems) {
        this.todoItems = todoItems;
    }

    @Override
    @Step("{0} adds todo items: #todoItems")
    public <T extends Actor> void performAs(T actor) {
        for (String item : todoItems) {
            actor.attemptsTo(
                Enter.theValue(item).into(TodoList.NEW_TODO_INPUT),
                Press.keys("Enter")
            );
        }
    }

    /**
     * Add a single todo item.
     */
    public static AddATodoItem called(String todoItem) {
        return new AddATodoItem(List.of(todoItem));
    }

    /**
     * Add multiple todo items.
     */
    public static AddATodoItem withItems(String... todoItems) {
        return new AddATodoItem(Arrays.asList(todoItems));
    }
}
