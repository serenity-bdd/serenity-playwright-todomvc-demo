package features;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import net.serenitybdd.screenplay.playwright.interactions.*;
import net.serenitybdd.screenplay.playwright.questions.Text;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

/**
 * Demonstrates Session State Persistence feature.
 *
 * <p>This feature allows you to:</p>
 * <ul>
 *   <li>Save browser session state (cookies, localStorage, sessionStorage) to a file</li>
 *   <li>Restore session state in a new browser context</li>
 *   <li>Share authenticated sessions across tests without re-logging in</li>
 * </ul>
 *
 * <p>Use cases:</p>
 * <ul>
 *   <li>Speed up test suites by logging in once and reusing the session</li>
 *   <li>Test authenticated user flows without repeating login steps</li>
 *   <li>Create test fixtures with pre-authenticated state</li>
 * </ul>
 *
 * <p>This example uses https://the-internet.herokuapp.com/login for demonstration.</p>
 */
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Session State Persistence")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WhenUsingSessionStatePersistenceTest {

    @TempDir
    static Path sharedTempDir;

    static Path savedSessionPath;

    @Test
    @Order(1)
    @DisplayName("Can save authenticated session state to a file")
    void canSaveAuthenticatedSessionState() {
        // Create an actor and log in
        Actor alice = Actor.named("Alice")
            .whoCan(BrowseTheWebWithPlaywright.usingTheDefaultConfiguration());

        savedSessionPath = sharedTempDir.resolve("authenticated-session.json");

        alice.attemptsTo(
            // Navigate to login page
            Open.url("https://the-internet.herokuapp.com/login"),

            // Perform login
            Enter.theValue("tomsmith").into("#username"),
            Enter.theValue("SuperSecretPassword!").into("#password"),
            Click.on("button[type='submit']"),

            // Verify we're logged in
            Ensure.that(Text.of("#flash")).containsIgnoringCase("You logged into a secure area"),

            // Save the authenticated session state
            SaveSessionState.toPath(savedSessionPath)
        );

        // Clean up this actor
        alice.wrapUp();
    }

    @Test
    @Order(2)
    @DisplayName("Can restore session state and access protected pages without logging in again")
    void canRestoreSessionAndAccessProtectedPages() {
        // Create a completely new actor (simulating a new test or browser session)
        Actor bob = Actor.named("Bob")
            .whoCan(BrowseTheWebWithPlaywright.usingTheDefaultConfiguration());

        bob.attemptsTo(
            // Restore the previously saved session state
            RestoreSessionState.fromPath(savedSessionPath),

            // Navigate directly to the secure area WITHOUT logging in
            Open.url("https://the-internet.herokuapp.com/secure"),

            // Verify we have access (session cookies were restored)
            Ensure.that(Text.of("h2")).containsIgnoringCase("Secure Area")
        );

        bob.wrapUp();
    }

    @Test
    @Order(3)
    @DisplayName("Can save session state to default location with a name")
    void canSaveSessionToDefaultLocation() {
        Actor charlie = Actor.named("Charlie")
            .whoCan(BrowseTheWebWithPlaywright.usingTheDefaultConfiguration());

        charlie.attemptsTo(
            Open.url("https://the-internet.herokuapp.com/login"),
            Enter.theValue("tomsmith").into("#username"),
            Enter.theValue("SuperSecretPassword!").into("#password"),
            Click.on("button[type='submit']"),

            // Save to default location: target/playwright/session-state/my-session.json
            SaveSessionState.toFile("my-session")
        );

        charlie.wrapUp();
    }
}
