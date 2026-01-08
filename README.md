# Serenity BDD Playwright TodoMVC Demo

A comprehensive demonstration of using Serenity BDD with Playwright to test the [TodoMVC React application](https://todomvc.com/examples/react/dist/).

This project showcases the recommended three-layer architecture for Serenity Playwright tests:
- **Tests** - JUnit 5 test classes that orchestrate steps and make assertions
- **Step Libraries** - `@Step` annotated methods that provide business-readable actions
- **Page Objects** - Encapsulate page interactions and locator strategies

## Project Structure

```
src/test/java/todomvc/
├── pages/
│   └── TodoMvcPage.java       # Page Object with locators and interactions
├── steps/
│   └── TodoSteps.java         # Step library with @Step annotated methods
└── tests/
    ├── WhenAddingTodosTest.java
    ├── WhenCompletingTodosTest.java
    ├── WhenDeletingTodosTest.java
    ├── WhenEditingTodosTest.java
    └── WhenFilteringTodosTest.java
```

## Key Patterns Demonstrated

### 1. Assertions Belong in Tests

Step libraries return data, tests make assertions:

```java
// In TodoSteps.java - returns data
@Step("Check if todo '{0}' is completed")
public boolean todoIsCompleted(String todoText) {
    return todoMvcPage.isCompleted(todoText);
}

// In test class - makes assertion
@Test
void shouldMarkTodoAsCompleted() {
    todo.openApplication();
    todo.addTodo("Buy milk");
    todo.completeTodo("Buy milk");

    assertThat(todo.todoIsCompleted("Buy milk")).isTrue();
}
```

### 2. Page Object as Step Library Field

The Page Object is a field in the step library, initialized via `setPage()`:

```java
public class TodoSteps {
    private TodoMvcPage todoMvcPage;

    public void setPage(Page page) {
        this.todoMvcPage = new TodoMvcPage(page);
    }

    // Steps use the page object...
}
```

### 3. Playwright Page Registration

Register Playwright pages for automatic screenshot capture:

```java
@BeforeEach
void setUp() {
    page = browser.newPage();
    PlaywrightSerenity.registerPage(page);
    todo.setPage(page);
}

@AfterEach
void tearDown() {
    PlaywrightSerenity.unregisterPage(page);
    page.close();
}
```

## Running the Tests

Run all tests and generate Serenity reports:

```bash
mvn clean verify
```

Run a specific test class:

```bash
mvn clean verify -Dtest=WhenAddingTodosTest
```

## Viewing Reports

After running tests, open the Serenity report:

```bash
open target/site/serenity/index.html
```

## Requirements

- Java 17 or higher
- Maven 3.8+
- Playwright browsers (installed automatically on first run)

## Dependencies

| Dependency | Version |
|------------|---------|
| Serenity BDD | 5.1.0 |
| Playwright | 1.57.0 |
| JUnit 5 | 6.0.1 |
| AssertJ | 3.27.3 |

## Learn More

- [Serenity BDD Documentation](https://serenity-bdd.github.io)
- [Serenity Playwright Guide](https://serenity-bdd.github.io/docs/playwright/playwright_intro)
- [TodoMVC](https://todomvc.com)