package todomvc.screenplay.questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import net.serenitybdd.annotations.Step;
import todomvc.screenplay.ui.TodoList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Question about the remaining (active) todo count shown in the footer.
 *
 * Usage:
 *   actor.asksFor(TheRemainingCount.value());
 */
public class TheRemainingCount implements Question<Integer> {

    private static final Pattern COUNT_PATTERN = Pattern.compile("(\\d+)");

    @Override
    @Step("{0} checks the remaining todo count")
    public Integer answeredBy(Actor actor) {
        String countText = BrowseTheWebWithPlaywright.as(actor)
            .getCurrentPage()
            .locator(TodoList.TODO_COUNT.asSelector())
            .textContent();

        Matcher matcher = COUNT_PATTERN.matcher(countText);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    public static TheRemainingCount value() {
        return new TheRemainingCount();
    }
}
