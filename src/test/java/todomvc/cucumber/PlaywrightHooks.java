package todomvc.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.Scenario;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.playwright.actors.PlaywrightCast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cucumber hooks for setting up the Playwright-enabled Screenplay stage.
 * <p>
 * These hooks ensure that:
 * <ul>
 *   <li>The stage is set with a PlaywrightCast before each scenario</li>
 *   <li>Actors automatically receive the BrowseTheWebWithPlaywright ability</li>
 *   <li>All browser resources are cleaned up after each scenario</li>
 * </ul>
 */
public class PlaywrightHooks {

    private static final Logger LOG = LoggerFactory.getLogger(PlaywrightHooks.class);

    /**
     * Set up the Playwright stage before each scenario.
     * The PlaywrightCast automatically provides actors with the
     * BrowseTheWebWithPlaywright ability.
     */
    @Before(order = 0)
    public void setTheStage(Scenario scenario) {
        LOG.info("Setting up Playwright stage for scenario: {}", scenario.getName());
        OnStage.setTheStage(new PlaywrightCast());
    }

    // Note: Cleanup is handled by Serenity's StageDirector @After hook
    // which automatically calls OnStage.drawTheCurtain()

    /**
     * Define the {actor} parameter type for Cucumber steps.
     * This allows steps like "Given Toby is on the application"
     * to automatically resolve "Toby" to an Actor instance.
     */
    @ParameterType(".*")
    public Actor actor(String actorName) {
        return OnStage.theActorCalled(actorName);
    }
}
