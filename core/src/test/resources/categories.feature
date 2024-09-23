Feature: Categories


  Background:
    * user categories version is 1
    * user categories are
      | id         | name    | icon       |
      | category-1 | Rent    | HOUSE      |
      | category-2 | Savings | PIGGY_BANK |


  Scenario: The one where category is created successfully
    When user creates new category
      | id         | name      | icon          |
      | category-3 | Groceries | SHOPPING_CART |
    Then user categories contains
      | id         | name      | icon          |
      | category-1 | Rent      | HOUSE         |
      | category-2 | Savings   | PIGGY_BANK    |
      | category-3 | Groceries | SHOPPING_CART |


  Scenario: The one where creating category has non unique name
    When user creates new category
      | id         | name | icon   |
      | category-3 | Rent | FOLDER |
    Then category is not saved due to constraint violation
      | path | code       |
      | name | NON_UNIQUE |


  Scenario: The one where creating category has empty name
    When user creates new category
      | id         | name | icon   |
      | category-3 |      | FOLDER |
    Then category is not saved due to constraint violation
      | path | code  |
      | name | EMPTY |


  Scenario Outline: The one where creating category has invalid name
    When user creates new category
      | id         | name   | icon   |
      | category-3 | <name> | FOLDER |
    Then category is not saved due to constraint violation
      | path | code               | illegal characters   |
      | name | ILLEGAL_CHARACTERS | <illegal characters> |

    Examples:
      | name      | illegal characters |
      | Rent<>    | <, >               |
      | Awesome!! | !                  |


  Scenario: The one where category is updated successfully
    When user updates the category
      | id         | name      | icon          |
      | category-2 | Groceries | SHOPPING_CART |
    Then user categories contains
      | id         | name      | icon          |
      | category-1 | Rent      | HOUSE         |
      | category-2 | Groceries | SHOPPING_CART |


  Scenario: The one where updating category has empty name
    When user updates the category
      | id         | name | icon       |
      | category-2 |      | PIGGY_BANK |
    Then category is not saved due to constraint violation
      | path | code  |
      | name | EMPTY |


  Scenario Outline: The one where updating category has invalid name
    When user updates the category
      | id         | name   | icon   |
      | category-2 | <name> | FOLDER |
    Then category is not saved due to constraint violation
      | path | code               | illegal characters   |
      | name | ILLEGAL_CHARACTERS | <illegal characters> |

    Examples:
      | name      | illegal characters |
      | Rent<>    | <, >               |
      | Awesome!! | !                  |


  Scenario: The one where updating category does not exist
    When user updates the category
      | id          | name    | icon   |
      | category-?? | Unknown | FOLDER |
    Then unknown category problem occurs


  Scenario: The one where category is deleted
    When user deletes category of id "category-2"
    Then user categories contains
      | id         | name      | icon          |
      | category-1 | Rent      | HOUSE         |


  Scenario: The one where deleting category does not exist
    When user deletes category of id "category-3"
    Then unknown category problem occurs
