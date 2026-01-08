package todomvc;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import net.serenitybdd.playwright.PlaywrightSerenity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class SerenityPlaywrightTest {

    protected Playwright playwright;
    protected Browser browser;
    protected Page page;

    @BeforeEach
    void setUpPlaywright() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );
        page = browser.newPage();
        PlaywrightSerenity.registerPage(page);
    }

    @AfterEach
    void tearDownPlaywright() {
        PlaywrightSerenity.unregisterPage(page);
        if (page != null) page.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
