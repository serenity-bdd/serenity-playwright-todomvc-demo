package todomvc.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.List;

/**
 * Page Object for the TodoMVC React application.
 * <p>
 * This class encapsulates all locator strategies and page interactions.
 * Tests and step libraries interact with the page through this class's
 * public methods, never directly with locators.
 * </p>
 * <p>
 * The TodoMVC app allows users to:
 * <ul>
 *   <li>Add new todo items</li>
 *   <li>Mark items as complete</li>
 *   <li>Edit existing items</li>
 *   <li>Delete items</li>
 *   <li>Filter by status (All, Active, Completed)</li>
 *   <li>Clear all completed items</li>
 * </ul>
 * </p>
 */
public class TodoMvcPage {

    private final Page page;
    private static final String URL = "https://todomvc.com/examples/react/dist/";

    public TodoMvcPage(Page page) {
        this.page = page;
    }

    // ========== Locators (private) ==========

    private Locator newTodoInput() {
        return page.getByPlaceholder("What needs to be done?");
    }

    private Locator todoItems() {
        return page.locator(".todo-list li");
    }

    private Locator todoItemByText(String text) {
        return page.locator(".todo-list li").filter(
                new Locator.FilterOptions().setHasText(text)
        );
    }

    private Locator todoLabel(String text) {
        return todoItemByText(text).locator("label");
    }

    private Locator todoCheckbox(String text) {
        return todoItemByText(text).locator(".toggle");
    }

    private Locator todoDestroyButton(String text) {
        return todoItemByText(text).locator(".destroy");
    }

    private Locator todoEditInput(String text) {
        return todoItemByText(text).locator(".edit");
    }

    private Locator todoCount() {
        return page.locator(".todo-count");
    }

    private Locator filterLink(String filterName) {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(filterName));
    }

    private Locator clearCompletedButton() {
        return page.locator(".clear-completed");
    }

    private Locator toggleAllCheckbox() {
        return page.locator("#toggle-all");
    }

    private Locator mainSection() {
        return page.locator(".main");
    }

    private Locator footerSection() {
        return page.locator(".footer");
    }

    // ========== Navigation ==========

    /**
     * Open the TodoMVC application.
     */
    public void open() {
        page.navigate(URL);
        page.waitForLoadState();
    }

    // ========== Adding Todos ==========

    /**
     * Add a new todo item.
     */
    public void addTodo(String todoText) {
        newTodoInput().fill(todoText);
        newTodoInput().press("Enter");
    }

    /**
     * Add multiple todo items.
     */
    public void addTodos(String... todoTexts) {
        for (String text : todoTexts) {
            addTodo(text);
        }
    }

    // ========== Completing Todos ==========

    /**
     * Mark a todo item as complete.
     */
    public void completeTodo(String todoText) {
        todoCheckbox(todoText).click();
    }

    /**
     * Toggle all todos to complete or active.
     */
    public void toggleAll() {
        // Use force click since the checkbox may be visually hidden
        toggleAllCheckbox().click(new Locator.ClickOptions().setForce(true));
    }

    // ========== Editing Todos ==========

    /**
     * Edit an existing todo item by double-clicking and typing new text.
     */
    public void editTodo(String oldText, String newText) {
        // Double-click to enter edit mode
        todoLabel(oldText).dblclick();

        // Get the edit input (which is now in editing mode on the li)
        Locator editInput = page.locator(".todo-list li.editing .edit");
        editInput.waitFor();

        // Clear the input and type new text
        editInput.clear();
        editInput.type(newText);
        editInput.press("Enter");
    }

    /**
     * Start editing a todo but cancel by pressing Escape.
     */
    public void cancelEdit(String todoText) {
        todoLabel(todoText).dblclick();
        page.locator(".todo-list li.editing .edit").press("Escape");
    }

    // ========== Deleting Todos ==========

    /**
     * Delete a todo item by clicking the destroy button.
     */
    public void deleteTodo(String todoText) {
        // Hover to reveal the delete button
        todoItemByText(todoText).hover();
        todoDestroyButton(todoText).click();
    }

    /**
     * Clear all completed todos.
     */
    public void clearCompleted() {
        clearCompletedButton().click();
    }

    // ========== Filtering ==========

    /**
     * Show all todos.
     */
    public void filterAll() {
        filterLink("All").click();
    }

    /**
     * Show only active (incomplete) todos.
     */
    public void filterActive() {
        filterLink("Active").click();
    }

    /**
     * Show only completed todos.
     */
    public void filterCompleted() {
        filterLink("Completed").click();
    }

    // ========== Queries ==========

    /**
     * Get the count of visible todo items.
     */
    public int getVisibleTodoCount() {
        return todoItems().count();
    }

    /**
     * Get the text of all visible todo items.
     */
    public List<String> getVisibleTodoTexts() {
        return todoItems().locator("label").allTextContents();
    }

    /**
     * Get the remaining items count text (e.g., "3 items left").
     */
    public String getRemainingCountText() {
        return todoCount().textContent();
    }

    /**
     * Get the number of remaining (active) items.
     */
    public int getRemainingCount() {
        String text = getRemainingCountText();
        // Extract number from text like "3 items left" or "1 item left"
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }

    /**
     * Check if a specific todo exists.
     */
    public boolean hasTodo(String todoText) {
        return todoItemByText(todoText).count() > 0;
    }

    /**
     * Check if a todo is marked as completed.
     */
    public boolean isCompleted(String todoText) {
        String classAttr = todoItemByText(todoText).getAttribute("class");
        return classAttr != null && classAttr.contains("completed");
    }

    /**
     * Check if the main section (todo list) is visible.
     */
    public boolean isMainSectionVisible() {
        return mainSection().count() > 0 && mainSection().isVisible();
    }

    /**
     * Check if the footer is visible.
     */
    public boolean isFooterVisible() {
        return footerSection().count() > 0 && footerSection().isVisible();
    }

    /**
     * Check if the "Clear completed" button is visible.
     */
    public boolean isClearCompletedVisible() {
        // Button is only rendered when there are completed items
        return clearCompletedButton().isVisible();
    }

    /**
     * Check if a todo is in editing mode.
     */
    public boolean isEditing(String todoText) {
        String classAttr = todoItemByText(todoText).getAttribute("class");
        return classAttr != null && classAttr.contains("editing");
    }

    /**
     * Get the currently selected filter.
     */
    public String getSelectedFilter() {
        return page.locator(".filters a.selected").textContent();
    }
}