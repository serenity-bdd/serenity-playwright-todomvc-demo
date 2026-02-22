package todomvc.screenplay;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.playwright.junit5.SerenityPlaywrightExtension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import todomvc.SerenityPlaywrightTest;

/**
 * Base test class for Screenplay-based Playwright tests.
 * <p>
 * This class uses {@code @UsePlaywright} to manage the browser lifecycle and wraps
 * the injected Page with {@link BrowseTheWebWithPlaywright#withPage(Page)} so that
 * the Screenplay actor reuses the same browser session.
 * <p>
 * {@code SerenityPlaywrightExtension} registers pages with Serenity for automatic
 * screenshot capture, and {@code SerenityJUnit5Extension} handles Serenity reporting.
 * <p>
 * <b>No explicit teardown is required.</b> {@code @UsePlaywright} manages the browser
 * lifecycle, and the ability unregisters cleanly without closing external resources.
 */
@ExtendWith(SerenityJUnit5Extension.class)
@ExtendWith(SerenityPlaywrightExtension.class)
@UsePlaywright(SerenityPlaywrightTest.ChromeHeadlessOptions.class)
public abstract class ScreenplayPlaywrightTest {

    protected Actor toby;

    @BeforeEach
    void setUpPlaywright(Page page) {
        toby = Actor.named("Toby");
        toby.can(BrowseTheWebWithPlaywright.withPage(page));
    }
}