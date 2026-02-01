package todomvc.screenplay;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base test class for Screenplay-based Playwright tests.
 * <p>
 * This class sets up an Actor with the ability to browse the web using Playwright.
 * <p>
 * <b>No explicit teardown is required.</b> The BrowseTheWebWithPlaywright ability
 * automatically subscribes to Serenity's test lifecycle events and cleans up
 * browser resources when the test finishes.
 */
public abstract class ScreenplayPlaywrightTest {

    protected Actor toby;

    @BeforeEach
    void setUpPlaywright() {
        toby = Actor.named("Toby");
        toby.can(BrowseTheWebWithPlaywright.usingTheDefaultConfiguration());
    }

    // No @AfterEach needed - BrowseTheWebWithPlaywright automatically
    // cleans up when it receives the TestLifecycleEvents.TestFinished event
}
