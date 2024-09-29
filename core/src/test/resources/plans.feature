Feature: Plans


  Background:
    * user categories are
      | id           | name      | icon          |
      | ID-rent      | Rent      | HOUSE         |
      | ID-savings   | Savings   | PIGGY_BANK    |
      | ID-groceries | Groceries | SHOPPING_CART |
    * user plans are
      | id      | name           | start      | end        |
      | plan-10 | September 2024 | 2024-09-01 | 2024-09-30 |
      | plan-20 | September 2024 | 2024-10-01 | 2024-10-31 |


  Scenario: The one where plan is created successfully
    When plan is defined
      | id      | name          | start      | end        |
      | plan-30 | November 2024 | 2024-11-01 | 2024-11-30 |
    And plan entries are defined
      | category  | value       |
      | Groceries | PLN 1200.00 |
      | Rent      | PLN 385.79  |
      | Savings   | PLN 5500.00 |
    And user creates a plan
    Then plan "plan-30" is saved successfully


  Scenario: The one where plan has start after end
    When plan is defined
      | id      | name          | start      | end        |
      | plan-30 | November 2024 | 2024-12-01 | 2024-11-30 |
    And user creates a plan
    Then plan is not saved due to constraint violations
      | path   | code    |
      | period | INVALID |


  Scenario Outline: The one where plan has invalid name
    When plan is defined
      | id      | name   | start      | end        |
      | plan-30 | <name> | 2024-11-01 | 2024-11-30 |
    And user creates a plan
    Then plan is not saved due to constraint violations
      | path | code               | illegal characters   |
      | name | ILLEGAL_CHARACTERS | <illegal characters> |

    Examples:
      | name               | illegal characters |
      | <November 2024>    | <, >               |
      | Awesome November!! | !                  |


  Scenario: The one where plan has duplicated entries
    When plan is defined
      | id      | name          | start      | end        |
      | plan-30 | November 2024 | 2024-11-01 | 2024-11-30 |
    And plan entries are defined
      | category  | value       |
      | Groceries | PLN 1200.00 |
      | Rent      | PLN 385.79  |
      | Savings   | PLN 5500.00 |
      | Groceries | PLN 1200.00 |
      | Rent      | PLN 385.79  |
    And user creates a plan
    Then plan is not saved due to constraint violations
      | path               | code       |
      | entries/4/category | NON_UNIQUE |
      | entries/5/category | NON_UNIQUE |


  Scenario: The one where plan has an entry for unknown category
    When plan is defined
      | id      | name          | start      | end        |
      | plan-30 | November 2024 | 2024-11-01 | 2024-11-30 |
    And plan entries are defined
      | category  | value       |
      | Unknown 1 | PLN 1299.00 |
      | Rent      | PLN 385.79  |
      | Unknown 1 | PLN 1299.00 |
      | Unknown 2 | PLN 1299.00 |
    And user creates a plan
    Then plan is not saved due to constraint violations
      | path               | code    |
      | entries/1/category | UNKNOWN |
      | entries/3/category | UNKNOWN |
      | entries/4/category | UNKNOWN |


  Scenario: The one where plan is updated successfully
    When plan is defined
      | id      | name               | start      | end        |
      | plan-20 | Awesome OCTOBER!!! | 2024-10-01 | 2024-10-31 |
    And plan entries are defined
      | category  | value          |
      | Rent      | PLN 279.79     |
      | Savings   | PLN 1000000.00 |
      | Groceries | PLN 700.00     |
    When user updates a plan
    Then plan "plan-20" is saved successfully


  Scenario: The one where deleting plan does not exist
    When user deletes the plan of id "plan-950"
    Then unknown plan problem occurs
