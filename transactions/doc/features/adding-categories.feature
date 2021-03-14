#language: en

Feature: Adding categories


  Background:
    * categories repository contains
      | id | name      | icon         |
      | 1  | Rent      | House        |
      | 2  | Groceries | ShoppingCart |
      | 3  | Savings   | PiggyBank    |


  Scenario: Category is added successfully
    When category is registering as
      | name                | icon      |
      | Health and Wellness | DrugStore |
    Then category is registered as
      | id | name                | icon      |
      | 4  | Health and Wellness | DrugStore |


  Scenario Outline: Category name is not valid
    When category is registering as
      | name   | icon      |
      | <name> | PiggyBank |
    Then category is not registered
    And constraint violations contain
      | property | message   | parameters   |
      | name     | <message> | <parameters> |

    Examples:
      | name       | message                                            | parameters  |
      | Rent       | Category %name% already exists                     | name=Rent   |
      | $aving$$!^ | Category name contains illegal characters: %chars% | chars=$,!,^ |
