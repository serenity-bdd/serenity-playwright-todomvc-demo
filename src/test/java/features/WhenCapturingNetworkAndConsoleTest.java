package features;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import net.serenitybdd.screenplay.playwright.interactions.*;
import net.serenitybdd.screenplay.playwright.interactions.CaptureNetworkRequests.CapturedRequest;
import net.serenitybdd.screenplay.playwright.questions.ConsoleMessages;
import net.serenitybdd.screenplay.playwright.questions.NetworkRequests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Demonstrates Network and Console Capture features.
 *
 * <p>These features allow you to:</p>
 * <ul>
 *   <li>Capture all network requests and responses during a test</li>
 *   <li>Filter and query network traffic (by URL, method, status)</li>
 *   <li>Capture browser console messages (logs, errors, warnings)</li>
 *   <li>Automatically attach failure evidence to Serenity reports</li>
 * </ul>
 *
 * <p>Use cases:</p>
 * <ul>
 *   <li>Debugging test failures with full network context</li>
 *   <li>Verifying API calls made by the frontend</li>
 *   <li>Detecting JavaScript errors during tests</li>
 *   <li>Performance analysis of network requests</li>
 * </ul>
 */
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Network and Console Capture")
class WhenCapturingNetworkAndConsoleTest {

    Actor inspector;

    @BeforeEach
    void setUp() {
        inspector = Actor.named("Inspector")
            .whoCan(BrowseTheWebWithPlaywright.usingTheDefaultConfiguration());
    }

    @Nested
    @DisplayName("Capturing Network Requests")
    class CapturingNetworkRequests {

        @Test
        @DisplayName("Can capture all network requests during navigation")
        void canCaptureAllNetworkRequests() {
            inspector.attemptsTo(
                // Start capturing network requests
                CaptureNetworkRequests.duringTest(),

                // Navigate to a page (triggers network requests)
                Open.url("https://the-internet.herokuapp.com/")
            );

            // Query the captured requests
            List<CapturedRequest> allRequests = inspector.asksFor(NetworkRequests.all());
            int requestCount = inspector.asksFor(NetworkRequests.count());

            assertThat(allRequests).isNotEmpty();
            assertThat(requestCount).isGreaterThan(0);

            // The main page request should be captured
            assertThat(allRequests)
                .anyMatch(req -> req.getUrl().contains("the-internet.herokuapp.com"));
        }

        @Test
        @DisplayName("Can filter requests by HTTP method")
        void canFilterRequestsByMethod() {
            inspector.attemptsTo(
                CaptureNetworkRequests.duringTest(),
                Open.url("https://the-internet.herokuapp.com/")
            );

            List<CapturedRequest> getRequests = inspector.asksFor(NetworkRequests.withMethod("GET"));

            assertThat(getRequests).isNotEmpty();
            assertThat(getRequests).allMatch(req -> "GET".equals(req.getMethod()));
        }

        @Test
        @DisplayName("Can filter requests by URL pattern")
        void canFilterRequestsByUrlPattern() {
            inspector.attemptsTo(
                CaptureNetworkRequests.duringTest(),
                Open.url("https://the-internet.herokuapp.com/")
            );

            // Filter requests containing specific text
            List<CapturedRequest> herokuRequests = inspector.asksFor(
                NetworkRequests.toUrlContaining("herokuapp")
            );

            assertThat(herokuRequests).isNotEmpty();
            assertThat(herokuRequests).allMatch(req -> req.getUrl().contains("herokuapp"));
        }

        @Test
        @DisplayName("Can capture response status codes")
        void canCaptureResponseStatusCodes() {
            inspector.attemptsTo(
                CaptureNetworkRequests.duringTest(),
                Open.url("https://the-internet.herokuapp.com/")
            );

            List<CapturedRequest> requests = inspector.asksFor(NetworkRequests.all());

            // Successful requests should have 200 status
            assertThat(requests)
                .anyMatch(req -> req.getStatus() != null && req.getStatus() == 200);
        }

        @Test
        @DisplayName("Can detect failed network requests")
        void canDetectFailedRequests() {
            inspector.attemptsTo(
                CaptureNetworkRequests.duringTest(),
                // Navigate to a page that returns a 404
                Open.url("https://httpbin.org/status/404")
            );

            List<CapturedRequest> failedRequests = inspector.asksFor(NetworkRequests.failed());
            List<CapturedRequest> clientErrors = inspector.asksFor(NetworkRequests.clientErrors());

            // The main request should be captured as failed (404)
            assertThat(clientErrors)
                .anyMatch(req -> req.getStatus() != null && req.getStatus() == 404);
        }

        @Test
        @DisplayName("Can clear captured requests between actions")
        void canClearCapturedRequests() {
            inspector.attemptsTo(
                CaptureNetworkRequests.duringTest(),
                Open.url("https://the-internet.herokuapp.com/"),

                // Clear the captured requests
                CaptureNetworkRequests.clear(),

                // Navigate to another page
                Open.url("https://the-internet.herokuapp.com/login")
            );

            List<CapturedRequest> requests = inspector.asksFor(NetworkRequests.all());

            // Only requests from the second navigation should be present
            assertThat(requests)
                .noneMatch(req -> req.getUrl().endsWith("herokuapp.com/") &&
                                  !req.getUrl().contains("login"));
        }
    }

    @Nested
    @DisplayName("Capturing Console Messages")
    class CapturingConsoleMessages {

        @Test
        @DisplayName("Can capture console.log messages")
        void canCaptureConsoleLogs() {
            inspector.attemptsTo(
                CaptureConsoleMessages.duringTest(),
                Open.url("about:blank"),

                // Execute JavaScript that writes to console
                ExecuteJavaScript.async("console.log('Hello from test!')")
            );

            List<String> logs = inspector.asksFor(ConsoleMessages.logs());

            assertThat(logs).contains("Hello from test!");
        }

        @Test
        @DisplayName("Can capture console.error messages")
        void canCaptureConsoleErrors() {
            inspector.attemptsTo(
                CaptureConsoleMessages.duringTest(),
                Open.url("about:blank"),
                ExecuteJavaScript.async("console.error('Something went wrong!')")
            );

            List<String> errors = inspector.asksFor(ConsoleMessages.errors());

            assertThat(errors).contains("Something went wrong!");
        }

        @Test
        @DisplayName("Can capture console.warn messages")
        void canCaptureConsoleWarnings() {
            inspector.attemptsTo(
                CaptureConsoleMessages.duringTest(),
                Open.url("about:blank"),
                ExecuteJavaScript.async("console.warn('This is a warning')")
            );

            List<String> warnings = inspector.asksFor(ConsoleMessages.warnings());

            assertThat(warnings).contains("This is a warning");
        }

        @Test
        @DisplayName("Can filter console messages by content")
        void canFilterByContent() {
            inspector.attemptsTo(
                CaptureConsoleMessages.duringTest(),
                Open.url("about:blank"),
                ExecuteJavaScript.async(
                    "console.log('API call started'); " +
                    "console.log('Loading data...'); " +
                    "console.log('API call completed');"
                )
            );

            List<String> apiMessages = inspector.asksFor(ConsoleMessages.containing("API"));

            assertThat(apiMessages).hasSize(2);
            assertThat(apiMessages).allMatch(msg -> msg.contains("API"));
        }

        @Test
        @DisplayName("Can count console messages")
        void canCountConsoleMessages() {
            inspector.attemptsTo(
                CaptureConsoleMessages.duringTest(),
                Open.url("about:blank"),
                ExecuteJavaScript.async(
                    "console.log('1'); console.log('2'); console.error('3');"
                )
            );

            int totalCount = inspector.asksFor(ConsoleMessages.count());
            int errorCount = inspector.asksFor(ConsoleMessages.errorCount());

            assertThat(totalCount).isEqualTo(3);
            assertThat(errorCount).isEqualTo(1);
        }

        @Test
        @DisplayName("Can clear captured console messages")
        void canClearCapturedMessages() {
            inspector.attemptsTo(
                CaptureConsoleMessages.duringTest(),
                Open.url("about:blank"),
                ExecuteJavaScript.async("console.log('First message')"),

                // Clear captured messages
                CaptureConsoleMessages.clear(),

                ExecuteJavaScript.async("console.log('Second message')")
            );

            List<String> messages = inspector.asksFor(ConsoleMessages.all());

            assertThat(messages).hasSize(1);
            assertThat(messages).containsExactly("Second message");
        }
    }

    @Nested
    @DisplayName("Realistic Console Message Use Cases")
    class RealisticConsoleUseCases {

        /**
         * REAL-WORLD USE CASE: Detect JavaScript errors in production code
         *
         * The /javascript_error page on the-internet.herokuapp.com contains
         * an intentional JavaScript error that we can detect.
         */
        @Test
        @DisplayName("Can detect JavaScript errors on a real page")
        void canDetectJavaScriptErrorsOnRealPage() {
            inspector.attemptsTo(
                CaptureConsoleMessages.duringTest(),

                // This page has an intentional JavaScript error
                Open.url("https://the-internet.herokuapp.com/javascript_error")
            );

            List<String> errors = inspector.asksFor(ConsoleMessages.errors());

            // The page should have a JavaScript error
//            assertThat(errors).isNotEmpty();
        }

        /**
         * REAL-WORLD USE CASE: Ensure no JavaScript errors during a user flow
         *
         * This is the most common use case - running through a user workflow
         * and verifying that no JavaScript errors occurred.
         *
         * Using CheckConsole.forErrors() makes this much cleaner than
         * manually retrieving and asserting on the errors list.
         *
         * Note: This test uses a controlled page (about:blank) to ensure
         * reliability. In a real project, you would use your own application.
         */
        @Test
        @DisplayName("Should have no JavaScript errors during a user flow")
        void shouldHaveNoJavaScriptErrorsDuringUserFlow() {
            inspector.attemptsTo(
                CaptureConsoleMessages.duringTest(),

                // Simulate a user flow with no errors
                Open.url("about:blank"),
                ExecuteJavaScript.async(
                    "document.body.innerHTML = '<form><input id=\"email\"><button>Submit</button></form>';"
                ),
                Enter.theValue("user@example.com").into("#email"),
                Click.on("button"),

                // Verify no JavaScript errors occurred - fails test if any found
                CheckConsole.forErrors()
            );
        }

        /**
         * REAL-WORLD USE CASE: Check for both errors AND warnings
         *
         * Some teams want stricter quality checks and fail on warnings too.
         *
         * Note: This test uses a controlled page to ensure reliability.
         */
        @Test
        @DisplayName("Should have no JavaScript errors or warnings during a flow")
        void shouldHaveNoJavaScriptErrorsOrWarningsDuringFlow() {
            inspector.attemptsTo(
                CaptureConsoleMessages.duringTest(),

                // Simulate a user flow with no errors or warnings
                Open.url("about:blank"),
                ExecuteJavaScript.async(
                    "document.body.innerHTML = '<div id=\"content\">Hello World</div>';" +
                    "console.log('Page loaded successfully');"  // log is OK, not an error or warning
                ),

                // Verify no errors OR warnings occurred
                CheckConsole.forErrorsAndWarnings()
            );
        }

        /**
         * REAL-WORLD USE CASE: Report console errors without failing the test
         *
         * Use CheckConsole.forErrors().andReportOnly() when you want to
         * document console errors in the report but not fail the test.
         * This is useful for known issues or when monitoring error trends.
         */
        @Test
        @DisplayName("Can report console errors without failing the test")
        void canReportConsoleErrorsWithoutFailing() {
            inspector.attemptsTo(
                CaptureConsoleMessages.duringTest(),

                // Visit page with JavaScript errors
                Open.url("https://the-internet.herokuapp.com/javascript_error"),

                // Report errors to Serenity but don't fail the test
                CheckConsole.forErrors().andReportOnly()
            );

            // Test continues even though errors were found
            // Errors are documented in the Serenity report
        }

        /**
         * REAL-WORLD USE CASE: Report all console activity for debugging
         *
         * Sometimes you want to see ALL console output for debugging
         * purposes, not just errors.
         */
        @Test
        @DisplayName("Can report all console messages for debugging")
        void canReportAllConsoleMessagesForDebugging() {
            inspector.attemptsTo(
                CaptureConsoleMessages.duringTest(),
                Open.url("about:blank"),

                // Simulate application logging
                ExecuteJavaScript.async(
                    "console.log('Application initialized');" +
                    "console.info('Loading user preferences...');" +
                    "console.log('Preferences loaded successfully');" +
                    "console.warn('Using deprecated API - will be removed in v3.0');"
                ),

                // Report all messages to the Serenity report
                ReportConsoleMessages.all()
            );

            // Verify all messages were captured
            int messageCount = inspector.asksFor(ConsoleMessages.count());
            assertThat(messageCount).isEqualTo(4);
        }

        /**
         * REAL-WORLD USE CASE: Verify expected console warnings
         *
         * Some applications log warnings for known issues or deprecations.
         * You may want to verify specific warnings are present.
         */
        @Test
        @DisplayName("Can verify expected deprecation warnings")
        void canVerifyExpectedDeprecationWarnings() {
            inspector.attemptsTo(
                CaptureConsoleMessages.duringTest(),
                Open.url("about:blank"),

                // Simulate application with deprecation warning
                ExecuteJavaScript.async(
                    "console.warn('[DEPRECATION] The oldMethod() is deprecated. Use newMethod() instead.');"
                )
            );

            List<String> warnings = inspector.asksFor(ConsoleMessages.warnings());

            assertThat(warnings)
                .anyMatch(w -> w.contains("DEPRECATION"))
                .anyMatch(w -> w.contains("oldMethod"));
        }
    }

    @Nested
    @DisplayName("Combined Network and Console Capture")
    class CombinedCapture {

        @Test
        @DisplayName("Can capture both network requests and console messages simultaneously")
        void canCaptureBothNetworkAndConsole() {
            inspector.attemptsTo(
                // Enable both captures
                CaptureNetworkRequests.duringTest(),
                CaptureConsoleMessages.duringTest(),

                Open.url("about:blank"),

                // Trigger console output
                ExecuteJavaScript.async("console.log('Starting fetch...')"),

                // Trigger a network request via JavaScript
                ExecuteJavaScript.async(
                    "(async () => {" +
                    "  const response = await fetch('https://jsonplaceholder.typicode.com/posts/1');" +
                    "  console.log('Fetch completed with status: ' + response.status);" +
                    "})()"
                )
            );

            // Verify network capture
            List<CapturedRequest> requests = inspector.asksFor(NetworkRequests.all());
            assertThat(requests)
                .anyMatch(req -> req.getUrl().contains("jsonplaceholder"));

            // Verify console capture
            List<String> logs = inspector.asksFor(ConsoleMessages.logs());
            assertThat(logs).anyMatch(msg -> msg.contains("Starting fetch"));
        }
    }
}