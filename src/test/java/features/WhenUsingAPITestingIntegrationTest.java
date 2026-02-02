package features;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.ensure.Ensure;
import net.serenitybdd.screenplay.playwright.abilities.BrowseTheWebWithPlaywright;
import net.serenitybdd.screenplay.playwright.interactions.Open;
import net.serenitybdd.screenplay.playwright.interactions.api.APIRequest;
import net.serenitybdd.screenplay.playwright.questions.api.LastAPIResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Demonstrates API Testing Integration feature.
 *
 * <p>This feature allows you to:</p>
 * <ul>
 *   <li>Make API calls that share the browser's session (cookies, auth)</li>
 *   <li>Perform hybrid UI + API testing scenarios</li>
 *   <li>Query API responses (status, headers, body, JSON)</li>
 *   <li>Record API calls in Serenity reports (like RestAssured)</li>
 * </ul>
 *
 * <p>Use cases:</p>
 * <ul>
 *   <li>Set up test data via API before UI testing</li>
 *   <li>Verify backend state after UI actions</li>
 *   <li>Test API endpoints with browser authentication</li>
 *   <li>Speed up tests by using API for setup/teardown</li>
 * </ul>
 *
 * <p>This example uses https://jsonplaceholder.typicode.com for demonstration.</p>
 */
@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("API Testing Integration")
class WhenUsingAPITestingIntegrationTest {

    Actor tester;

    @BeforeEach
    void setUp() {
        tester = Actor.named("API Tester")
            .whoCan(BrowseTheWebWithPlaywright.usingTheDefaultConfiguration());

        // Initialize a page to get the browser context (required for API calls)
        tester.attemptsTo(Open.url("about:blank"));
    }

    @Nested
    @DisplayName("Making GET requests")
    class MakingGetRequests {

        @Test
        @DisplayName("Can fetch a single resource")
        void canFetchSingleResource() {
            tester.attemptsTo(
                APIRequest.get("https://jsonplaceholder.typicode.com/posts/1")
            );

            // Query the response using Questions
            int statusCode = tester.asksFor(LastAPIResponse.statusCode());
            boolean isOk = tester.asksFor(LastAPIResponse.ok());
            Map<String, Object> post = tester.asksFor(LastAPIResponse.jsonBody());

            assertThat(statusCode).isEqualTo(200);
            assertThat(isOk).isTrue();
            assertThat(post).containsKeys("userId", "id", "title", "body");
            assertThat(post.get("id")).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Can fetch a collection of resources")
        void canFetchCollection() {
            tester.attemptsTo(
                APIRequest.get("https://jsonplaceholder.typicode.com/posts")
                    .withQueryParam("userId", "1")
            );

            List<Map<String, Object>> posts = tester.asksFor(LastAPIResponse.jsonBodyAsList());

            assertThat(posts).isNotEmpty();
            assertThat(posts).allMatch(post -> post.get("userId").equals(1.0));
        }

        @Test
        @DisplayName("Can add custom headers to requests")
        void canAddCustomHeaders() {
            tester.attemptsTo(
                APIRequest.get("https://httpbin.org/headers")
                    .withHeader("X-Custom-Header", "my-value")
                    .withHeader("Accept-Language", "en-US")
            );

            Map<String, Object> response = tester.asksFor(LastAPIResponse.jsonBody());
            @SuppressWarnings("unchecked")
            Map<String, String> headers = (Map<String, String>) response.get("headers");

            // httpbin returns headers with normalized names
            assertThat(headers.get("X-Custom-Header")).isEqualTo("my-value");
            assertThat(headers.get("Accept-Language")).isEqualTo("en-US");
        }
    }

    @Nested
    @DisplayName("Making POST requests")
    class MakingPostRequests {

        @Test
        @DisplayName("Can create a resource with JSON body")
        void canCreateResourceWithJsonBody() {
            tester.attemptsTo(
                APIRequest.post("https://jsonplaceholder.typicode.com/posts")
                    .withJsonBody(Map.of(
                        "title", "My New Post",
                        "body", "This is the content of my post",
                        "userId", 1
                    ))
            );

            // Verify the response
            tester.attemptsTo(
                Ensure.that(LastAPIResponse.statusCode()).isEqualTo(201)
            );

            Map<String, Object> createdPost = tester.asksFor(LastAPIResponse.jsonBody());
            assertThat(createdPost.get("title")).isEqualTo("My New Post");
            assertThat(createdPost.get("id")).isNotNull();
        }
    }

    @Nested
    @DisplayName("Making PUT and PATCH requests")
    class MakingPutAndPatchRequests {

        @Test
        @DisplayName("Can update a resource with PUT")
        void canUpdateWithPut() {
            tester.attemptsTo(
                APIRequest.put("https://jsonplaceholder.typicode.com/posts/1")
                    .withJsonBody(Map.of(
                        "id", 1,
                        "title", "Updated Title",
                        "body", "Updated body content",
                        "userId", 1
                    ))
            );

            Map<String, Object> updatedPost = tester.asksFor(LastAPIResponse.jsonBody());

            assertThat(tester.asksFor(LastAPIResponse.statusCode())).isEqualTo(200);
            assertThat(updatedPost.get("title")).isEqualTo("Updated Title");
        }

        @Test
        @DisplayName("Can partially update a resource with PATCH")
        void canPartiallyUpdateWithPatch() {
            tester.attemptsTo(
                APIRequest.patch("https://jsonplaceholder.typicode.com/posts/1")
                    .withJsonBody(Map.of("title", "Only Title Changed"))
            );

            Map<String, Object> patchedPost = tester.asksFor(LastAPIResponse.jsonBody());

            assertThat(tester.asksFor(LastAPIResponse.statusCode())).isEqualTo(200);
            assertThat(patchedPost.get("title")).isEqualTo("Only Title Changed");
        }
    }

    @Nested
    @DisplayName("Making DELETE requests")
    class MakingDeleteRequests {

        @Test
        @DisplayName("Can delete a resource")
        void canDeleteResource() {
            tester.attemptsTo(
                APIRequest.delete("https://jsonplaceholder.typicode.com/posts/1")
            );

            assertThat(tester.asksFor(LastAPIResponse.statusCode())).isEqualTo(200);
        }
    }

    @Nested
    @DisplayName("Handling responses")
    class HandlingResponses {

        @Test
        @DisplayName("Can read response headers")
        void canReadResponseHeaders() {
            tester.attemptsTo(
                APIRequest.get("https://jsonplaceholder.typicode.com/posts/1")
            );

            String contentType = tester.asksFor(LastAPIResponse.header("Content-Type"));
            Map<String, String> allHeaders = tester.asksFor(LastAPIResponse.headers());

            assertThat(contentType).contains("application/json");
            assertThat(allHeaders).isNotEmpty();
        }

        @Test
        @DisplayName("Can handle error responses")
        void canHandleErrorResponses() {
            tester.attemptsTo(
                APIRequest.get("https://jsonplaceholder.typicode.com/posts/99999")
            );

            assertThat(tester.asksFor(LastAPIResponse.statusCode())).isEqualTo(404);
            assertThat(tester.asksFor(LastAPIResponse.ok())).isFalse();
        }

        @Test
        @DisplayName("Can get the final URL after redirects")
        void canGetFinalUrl() {
            tester.attemptsTo(
                APIRequest.get("https://jsonplaceholder.typicode.com/posts/1")
            );

            String url = tester.asksFor(LastAPIResponse.url());

            assertThat(url).contains("jsonplaceholder.typicode.com");
        }
    }

    @Nested
    @DisplayName("Hybrid UI + API testing")
    class HybridUiApiTesting {

        @Test
        @DisplayName("API calls share browser session cookies")
        void apiCallsShareBrowserCookies() {
            // First, set a cookie via the browser
            tester.attemptsTo(
                Open.url("https://httpbin.org/cookies/set/session_token/abc123")
            );

            // Now make an API call - it should include the cookie
            tester.attemptsTo(
                APIRequest.get("https://httpbin.org/cookies")
            );

            Map<String, Object> response = tester.asksFor(LastAPIResponse.jsonBody());
            @SuppressWarnings("unchecked")
            Map<String, String> cookies = (Map<String, String>) response.get("cookies");

            // The API call automatically includes cookies from the browser session
            assertThat(cookies).containsEntry("session_token", "abc123");
        }
    }
}