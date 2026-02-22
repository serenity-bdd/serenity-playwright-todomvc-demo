package todomvc;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.playwright.junit5.SerenityPlaywrightExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;

/**
 * Base test class for Page Object-based Playwright tests with Serenity BDD.
 * <p>
 * {@code @UsePlaywright} manages the full browser lifecycle (Playwright, Browser,
 * BrowserContext, Page) and the {@code SerenityPlaywrightExtension} automatically
 * registers injected Page instances with Serenity for screenshot capture.
 * </p>
 * <p>
 * Subclasses receive a {@code Page} parameter in their {@code @BeforeEach} and
 * {@code @Test} methods — no manual setup or teardown is needed.
 * </p>
 */
@ExtendWith(SerenityJUnit5Extension.class)
@ExtendWith(SerenityPlaywrightExtension.class)
@UsePlaywright(SerenityPlaywrightTest.ChromeHeadlessOptions.class)
public abstract class SerenityPlaywrightTest {

    public static class ChromeHeadlessOptions implements OptionsFactory {
        @Override
        public Options getOptions() {
            return new Options()
                    .setHeadless(true)
                    .setLaunchOptions(
                            new BrowserType.LaunchOptions()
                                    .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
                    );
        }
    }
}
