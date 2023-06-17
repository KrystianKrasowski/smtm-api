Feature: Categories

  Background:
    * user categories version is 1
    * user has categories defined
      | id | name           | icon       |
      | 1  | Rent           | HOUSE      |
      | 2  | Savings        | PIGGY_BANK |
      | 3  | House services | LIGHTENING |


  Scenario: The one where category is successfully added
    When user saves category
      | name      | icon   |
      | Groceries | FOLDER |
    Then user categories contains
      | id | name      | icon   |
      | 4  | Groceries | FOLDER |


  Scenario Outline: The one where provided category is not valid
    When user saves category
      | name   | icon   |
      | <name> | FOLDER |
    Then constraint violations set contains
      | path | code   | parameters   |
      | name | <code> | <parameters> |

    Examples:
      | name | code       |
      |      | EMPTY      |
      | Rent | NON_UNIQUE |
