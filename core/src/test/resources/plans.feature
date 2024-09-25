Feature: Plans


  Background:
    * user categories are
      | id         | name      | icon          |
      | category-1 | Rent      | HOUSE         |
      | category-2 | Savings   | PIGGY_BANK    |
      | category-2 | Groceries | SHOPPING_CART |


  Scenario: The one where plan is created successfully
    Given plan is defined
      | id      | name          | start      | end        |
      | plan-30 | November 2024 | 2024-11-01 | 2024-11-30 |
    And plan has category "Rent" with value PLN 385.79
    And plan has category "Savings" with value PLN 5500.00
    And plan has category "Groceries" with value PLN 1200.00
    When user creates a plan
    Then plan "plan-30" is saved successfully


  Scenario: The one where plan has start after end
    Given plan is defined
      | id      | name          | start      | end        |
      | plan-30 | November 2024 | 2024-12-01 | 2024-11-30 |
    When user creates a plan
    Then plan is not saved due to constraint violation
      | path   | code    |
      | period | INVALID |


  Scenario Outline: The one where plan has invalid name
    Given plan is defined
      | id      | name   | start      | end        |
      | plan-30 | <name> | 2024-11-01 | 2024-11-30 |
    When user creates a plan
    Then plan is not saved due to constraint violation
      | path | code               | illegal characters   |
      | name | ILLEGAL_CHARACTERS | <illegal characters> |

    Examples:
      | name               | illegal characters |
      | <November 2024>    | <, >               |
      | Awesome November!! | !                  |


  Scenario: The one where plan has duplicated entries
    Given plan is defined
      | id      | name          | start      | end        |
      | plan-30 | November 2024 | 2024-11-01 | 2024-11-30 |
    And plan has category "Groceries" with value PLN 1200.00
    And plan has category "Rent" with value PLN 385.79
    And plan has category "Savings" with value PLN 5500.00
    And plan has category "Groceries" with value PLN 1200.00
    When user creates a plan
    Then plan is not saved due to constraint violation
      | path               | code       |
      | entries/4/category | NON_UNIQUE |
