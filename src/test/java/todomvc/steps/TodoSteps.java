package todomvc.steps;

import com.microsoft.playwright.Page;
import net.serenitybdd.annotations.Step;
import todomvc.pages.TodoMvcPage;

import java.util.List;

/**
 * Step library for TodoMVC interactions.
 * <p>
 * This class provides @Step annotated methods that appear in Serenity reports.
 * Methods either perform actions or return data for assertions in tests.
 * Assertions themselves belong in the test class, not here.
 * </p>
 */
public class TodoSteps {

    private TodoMvcPage todoMvcPage;

    public void setPage(Page page) {
        this.todoMvcPage = new TodoMvcPage(page);
    }

    // ========== Navigation Steps ==========

    @Step("Open the TodoMVC application")
    public void openApplication() {
        todoMvcPage.open();
    }

    // ========== Adding Todo Steps ==========

    @Step("Add a todo: '{0}'")
    public void addTodo(String todoText) {
        todoMvcPage.addTodo(todoText);
    }

    @Step("Add todos: {0}")
    public void addTodos(String... todoTexts) {
        todoMvcPage.addTodos(todoTexts);
    }

    // ========== Completing Todo Steps ==========

    @Step("Complete the todo: '{0}'")
    public void completeTodo(String todoText) {
        todoMvcPage.completeTodo(todoText);
    }

    @Step("Toggle all todos")
    public void toggleAll() {
        todoMvcPage.toggleAll();
    }

    // ========== Editing Todo Steps ==========

    @Step("Edit todo '{0}' to '{1}'")
    public void editTodo(String oldText, String newText) {
        todoMvcPage.editTodo(oldText, newText);
    }

    @Step("Cancel editing the todo: '{0}'")
    public void cancelEdit(String todoText) {
        todoMvcPage.cancelEdit(todoText);
    }

    // ========== Deleting Todo Steps ==========

    @Step("Delete the todo: '{0}'")
    public void deleteTodo(String todoText) {
        todoMvcPage.deleteTodo(todoText);
    }

    @Step("Clear all completed todos")
    public void clearCompleted() {
        todoMvcPage.clearCompleted();
    }

    // ========== Filtering Steps ==========

    @Step("Filter to show all todos")
    public void filterAll() {
        todoMvcPage.filterAll();
    }

    @Step("Filter to show active todos only")
    public void filterActive() {
        todoMvcPage.filterActive();
    }

    @Step("Filter to show completed todos only")
    public void filterCompleted() {
        todoMvcPage.filterCompleted();
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