package todomvc.screenplay;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 * Base test class for Screenplay-based Playwright tests.
 *
 * This class sets up the Playwright browser and provides an Actor
 * with the ability to browse the web using Playwright.
 *
 * The BrowseTheWebWithPlaywright ability manages the Playwright lifecycle
 * automatically - it creates the browser, context, and page on demand
 * and cleans up resources when tearDown() is called.
 */
public abstract class ScreenplayPlaywrightTest {

    protected Actor toby;

    @BeforeEach
    void setUpPlaywright() {
        // Create an actor with the ability to browse the web using Playwright
        // The ability will lazily create browser/context/page when needed
        toby = Actor.named("Toby");
        toby.can(BrowseTheWebWithPlaywright.usingTheDefaultConfiguration());
    }

    @AfterEach
    void tearDownPlaywright() {
        // Clean up Playwright resources
        BrowseTheWebWithPlaywright.as(toby).tearDown();
    }
}
