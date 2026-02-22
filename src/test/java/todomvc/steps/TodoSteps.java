package todomvc.steps;

import com.microsoft.playwright.Page;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.playwright.PlaywrightSerenity;
import todomvc.pages.TodoMvcPage;

import java.util.List;

/**
 * Step library for TodoMVC interactions.
 * <p>
 * This class provides @Step annotated methods that appear in Serenity reports.
 * Methods either perform actions or return data for assertions in tests.
 * Assertions themselves belong in the test class, not here.
 * </p>
 * <p>
 * When using {@code @UsePlaywright}, call {@link #setPage(Page)} in your
 * {@code @BeforeEach} method with the injected Page. The page object is
 * lazily initialized from {@code PlaywrightSerenity.getCurrentPage()} if
 * not explicitly set.
 * </p>
 */
public class TodoSteps {

    private TodoMvcPage todoMvcPage;

    public void setPage(Page page) {
        this.todoMvcPage = new TodoMvcPage(page);
    }

    private TodoMvcPage page() {
        if (todoMvcPage == null) {
            Page currentPage = PlaywrightSerenity.getCurrentPage();
            if (currentPage != null) {
                todoMvcPage = new TodoMvcPage(currentPage);
            }
        }
        return todoMvcPage;
    }

    // ========== Navigation Steps ==========

    @Step("Open the TodoMVC application")
    public void openApplication() {
        page().open();
    }

    // ========== Adding Todo Steps ==========

    @Step("Add a todo: '{0}'")
    public void addTodo(String todoText) {
        page().addTodo(todoText);
    }

    @Step("Add todos: {0}")
    public void addTodos(String... todoTexts) {
        page().addTodos(todoTexts);
    }

    // ========== Completing Todo Steps ==========

    @Step("Complete the todo: '{0}'")
    public void completeTodo(String todoText) {
        page().completeTodo(todoText);
    }

    @Step("Toggle all todos")
    public void toggleAll() {
        page().toggleAll();
    }

    // ========== Editing Todo Steps ==========

    @Step("Edit todo '{0}' to '{1}'")
    public void editTodo(String oldText, String newText) {
        page().editTodo(oldText, newText);
    }

    @Step("Cancel editing the todo: '{0}'")
    public void cancelEdit(String todoText) {
        page().cancelEdit(todoText);
    }

    // ========== Deleting Todo Steps ==========

    @Step("Delete the todo: '{0}'")
    public void deleteTodo(String todoText) {
        page().deleteTodo(todoText);
    }

    @Step("Clear all completed todos")
    public void clearCompleted() {
        page().clearCompleted();
    }

    // ========== Filtering Steps ==========

    @Step("Filter to show all todos")
    public void filterAll() {
        page().filterAll();
    }

    @Step("Filter to show active todos only")
    public void filterActive() {
        page().filterActive();
    }

    @Step("Filter to show completed todos only")
    public void filterCompleted() {
        page().filterCompleted();
    }

    // ========== Query Methods (return data for assertions in tests) ==========

    @Step("Get the number of visible todos")
    public int visibleTodoCount() {
        return todoMvcPage.getVisibleTodoCount();
    }

    @Step("Get the visible todo items")
    public List<String> visibleTodos() {
        return todoMvcPage.getVisibleTodoTexts();
    }

    @Step("Get the remaining items count")
    public int remainingCount() {
        return todoMvcPage.getRemainingCount();
    }

    @Step("Check if todo '{0}' exists")
    public boolean todoExists(String todoText) {
        return todoMvcPage.hasTodo(todoText);
    }

    @Step("Check if todo '{0}' is completed")
    public boolean todoIsCompleted(String todoText) {
        return todoMvcPage.isCompleted(todoText);
    }

    @Step("Check if main section is visible")
    public boolean mainSectionIsVisible() {
        return todoMvcPage.isMainSectionVisible();
    }

    @Step("Check if footer is visible")
    public boolean footerIsVisible() {
        return todoMvcPage.isFooterVisible();
    }

    @Step("Check if 'Clear completed' button is visible")
    public boolean clearCompletedIsVisible() {
        return todoMvcPage.isClearCompletedVisible();
    }

    @Step("Get the selected filter")
    public String selectedFilter() {
        return todoMvcPage.getSelectedFilter();
    }
}