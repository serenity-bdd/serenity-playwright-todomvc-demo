package todomvc.screenplay.ui;

import net.serenitybdd.screenplay.playwright.Target;

/**
 * UI element locators for the TodoMVC application using Screenplay Targets.
 *
 * Targets provide a clean separation between element location strategies
 * and the interactions/questions that use them.
 */
public class TodoList {

    // ========== Input Elements ==========

    public static final Target NEW_TODO_INPUT =
        Target.the("new todo input")
              .locatedBy("[placeholder='What needs to be done?']");

    // ========== Todo Items ==========

    public static final Target TODO_ITEMS =
        Target.the("todo items")
              .locatedBy(".todo-list li");

    public static final Target TODO_ITEM_LABELS =
        Target.the("todo item labels")
              .locatedBy(".todo-list li label");

    public static Target todoItemCalled(String todoText) {
        return Target.the("todo item '" + todoText + "'")
                     .locatedBy(".todo-list li:has-text('" + escapeText(todoText) + "')");
    }

    public static Target checkboxFor(String todoText) {
        return Target.the("checkbox for '" + todoText + "'")
                     .locatedBy(".todo-list li:has-text('" + escapeText(todoText) + "') .toggle");
    }

    public static Target labelFor(String todoText) {
        return Target.the("label for '" + todoText + "'")
                     .locatedBy(".todo-list li:has-text('" + escapeText(todoText) + "') label");
    }

    public static Target deleteButtonFor(String todoText) {
        return Target.the("delete button for '" + todoText + "'")
                     .locatedBy(".todo-list li:has-text('" + escapeText(todoText) + "') .destroy");
    }

    public static final Target EDITING_INPUT =
        Target.the("editing input")
              .locatedBy(".todo-list li.editing .edit");

    // ========== Footer Elements ==========

    public static final Target TODO_COUNT =
        Target.the("todo count")
              .locatedBy(".todo-count");

    public static final Target CLEAR_COMPLETED_BUTTON =
        Target.the("clear completed button")
              .locatedBy(".clear-completed");

    public static final Target TOGGLE_ALL_CHECKBOX =
        Target.the("toggle all checkbox")
              .locatedBy("#toggle-all");

    // ========== Filter Links ==========

    public static final Target ALL_FILTER =
        Target.the("All filter")
              .locatedBy(".filters a:has-text('All')");

    public static final Target ACTIVE_FILTER =
        Target.the("Active filter")
              .locatedBy(".filters a:has-text('Active')");

    public static final Target COMPLETED_FILTER =
        Target.the("Completed filter")
              .locatedBy(".filters a:has-text('Completed')");

    public static final Target SELECTED_FILTER =
        Target.the("selected filter")
              .locatedBy(".filters a.selected");

    // ========== Sections ==========

    public static final Target MAIN_SECTION =
        Target.the("main section")
              .locatedBy(".main");

    public static final Target FOOTER_SECTION =
        Target.the("footer section")
              .locatedBy(".footer");

    // ========== Utility Methods ==========

    private static String escapeText(String text) {
        // Escape single quotes for CSS selector
        return text.replace("'", "\\'");
    }
}
