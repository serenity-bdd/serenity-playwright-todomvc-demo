package todomvc.cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.ensure.Ensure;
import todomvc.screenplay.questions.TheClearCompletedButton;
import todomvc.screenplay.questions.TheRemainingCount;
import todomvc.screenplay.questions.TheVisibleTodos;
import todomvc.screenplay.tasks.*;

import java.util.List;

/**
 * Cucumber step definitions for TodoMVC scenarios using Screenplay pattern with Playwright.
 */
public class TodoStepDefinitions {


    @Given("{actor} is on the TodoMVC application")
    public void actorIsOnTheTodoMvcApplication(Actor actor) {
        actor.attemptsTo(
            OpenTodoMvcApp.onTheTodoMvcHomePage()
        );
    }

    @When("{actor} adds a todo item called {string}")
    public void actorAddsTodoItemCalled(Actor actor, String todoItem) {
        actor.attemptsTo(
            AddATodoItem.called(todoItem)
        );
    }

    @When("{actor} adds the following todo items:")
    public void actorAddsTheFollowingTodoItems(Actor actor, List<String> todoItems) {
        for (String item : todoItems) {
            actor.attemptsTo(
                AddATodoItem.called(item)
            );
        }
    }

    @Given("{actor} has added the following todo items:")
    public void actorHasAddedTheFollowingTodoItems(Actor actor, List<String> todoItems) {
        actorIsOnTheTodoMvcApplication(actor);
        actorAddsTheFollowingTodoItems(actor, todoItems);
    }

    @When("{actor} completes the todo item {string}")
    public void actorCompletesTheTodoItem(Actor actor, String todoItem) {
        actor.attemptsTo(
            Complete.todoItem(todoItem)
        );
    }

    @Given("{actor} has completed the todo item {string}")
    public void actorHasCompletedTheTodoItem(Actor actor, String todoItem) {
        actorCompletesTheTodoItem(actor, todoItem);
    }

    @When("{actor} toggles all todos")
    public void actorTogglesAllTodos(Actor actor) {
        actor.attemptsTo(
            ToggleAll.todos()
        );
    }

    @When("{actor} deletes the todo item {string}")
    public void actorDeletesTheTodoItem(Actor actor, String todoItem) {
        actor.attemptsTo(
            Delete.theTodoItem(todoItem)
        );
    }

    @When("{actor} filters to show {string} todos")
    public void actorFiltersToShow(Actor actor, String filterType) {
        actor.attemptsTo(
            FilterTodos.toShow(filterType)
        );
    }

    @When("{actor} clears all completed todos")
    public void actorClearsAllCompletedTodos(Actor actor) {
        actor.attemptsTo(
            ClearCompletedTodos.fromTheList()
        );
    }

    @Then("the todo list should contain {string}")
    public void theTodoListShouldContain(String expectedItem) {
        Actor actor = OnStage.theActorInTheSpotlight();
        actor.attemptsTo(
            Ensure.that(TheVisibleTodos.displayed()).contains(expectedItem)
        );
    }

    @Then("the todo list should contain:")
    public void theTodoListShouldContainItems(List<String> expectedItems) {
        Actor actor = OnStage.theActorInTheSpotlight();
        actor.attemptsTo(
            Ensure.that(TheVisibleTodos.displayed()).containsExactlyElementsFrom(expectedItems)
        );
    }

    @Then("the visible todo list should contain:")
    public void theVisibleTodoListShouldContain(List<String> expectedItems) {
        Actor actor = OnStage.theActorInTheSpotlight();
        actor.attemptsTo(
            Ensure.that(TheVisibleTodos.displayed()).containsExactlyElementsFrom(expectedItems)
        );
    }

    @Then("the todo list should not contain {string}")
    public void theTodoListShouldNotContain(String unexpectedItem) {
        Actor actor = OnStage.theActorInTheSpotlight();
        actor.attemptsTo(
            Ensure.that(TheVisibleTodos.displayed()).doesNotContain(unexpectedItem)
        );
    }

    @Then("the remaining item count should be {int}")
    public void theRemainingItemCountShouldBe(int expectedCount) {
        Actor actor = OnStage.theActorInTheSpotlight();
        actor.attemptsTo(
            Ensure.that(TheRemainingCount.value()).isEqualTo(expectedCount)
        );
    }

    @And("the Clear Completed button should be visible")
    public void theClearCompletedButtonShouldBeVisible() {
        Actor actor = OnStage.theActorInTheSpotlight();
        actor.attemptsTo(
            Ensure.that(TheClearCompletedButton.isVisible()).isTrue()
        );
    }
}
