Feature: Categories

  Background:
    * user has categories defined
      | id | name           | icon       |
      | 1  | Rent           | HOUSE      |
      | 2  | Savings        | PIGGY_BANK |
      | 3  | House services | LIGHTENING |


  Scenario: The one where category is successfully added
    When user creates category
      | name      | icon   |
      | Groceries | FOLDER |
    Then user categories contains
      | name      | icon   |
      | Groceries | FOLDER |


  Scenario Outline: The one where provided category is not valid
    When user creates category
      | name   | icon   |
      | <name> | FOLDER |
    Then constraint violations set contains
      | path | code   | parameters   |
      | name | <code> | <parameters> |

    Examples:
      | name | code       |
      |      | EMPTY      |
      | Rent | NON_UNIQUE |
