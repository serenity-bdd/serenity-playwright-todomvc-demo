@playwright
Feature: Managing Todo Items
  As a busy person
  I want to manage my todo list
  So that I can keep track of what I need to do

  Background:
    Given Toby is on the TodoMVC application

  @adding
  Scenario: Adding a single todo item
    When Toby adds a todo item called "Buy milk"
    Then the todo list should contain "Buy milk"
    And the remaining item count should be 1

  @adding
  Scenario: Adding multiple todo items
    When Toby adds the following todo items:
      | Buy milk      |
      | Walk the dog  |
      | Do laundry    |
    Then the todo list should contain:
      | Buy milk      |
      | Walk the dog  |
      | Do laundry    |
    And the remaining item count should be 3

  @completing
  Scenario: Completing a todo item
    Given Toby has added the following todo items:
      | Buy milk      |
      | Walk the dog  |
      | Do laundry    |
    When Toby completes the todo item "Walk the dog"
    Then the remaining item count should be 2

  @completing
  Scenario: Completing all todo items
    Given Toby has added the following todo items:
      | Buy milk      |
      | Walk the dog  |
    When Toby toggles all todos
    Then the remaining item count should be 0

  @deleting
  Scenario: Deleting a todo item
    Given Toby has added the following todo items:
      | Buy milk      |
      | Walk the dog  |
      | Do laundry    |
    When Toby deletes the todo item "Walk the dog"
    Then the todo list should contain:
      | Buy milk   |
      | Do laundry |
    And the todo list should not contain "Walk the dog"

  @filtering
  Scenario: Filtering to show only active todos
    Given Toby has added the following todo items:
      | Buy milk      |
      | Walk the dog  |
      | Do laundry    |
    And Toby has completed the todo item "Walk the dog"
    When Toby filters to show "Active" todos
    Then the visible todo list should contain:
      | Buy milk   |
      | Do laundry |

  @filtering
  Scenario: Filtering to show only completed todos
    Given Toby has added the following todo items:
      | Buy milk      |
      | Walk the dog  |
      | Do laundry    |
    And Toby has completed the todo item "Walk the dog"
    When Toby filters to show "Completed" todos
    Then the visible todo list should contain:
      | Walk the dog |

  @clearing
  Scenario: Clearing completed todos
    Given Toby has added the following todo items:
      | Buy milk      |
      | Walk the dog  |
      | Do laundry    |
    And Toby has completed the todo item "Walk the dog"
    And Toby has completed the todo item "Do laundry"
    When Toby clears all completed todos
    Then the todo list should contain:
      | Buy milk |
    And the remaining item count should be 1
