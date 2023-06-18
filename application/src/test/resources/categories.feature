Feature: Categories

  Background:
    * user categories version is 1
    * user has categories defined
      | id | name           | icon       |
      | 1  | Rent           | HOUSE      |
      | 2  | Savings        | PIGGY_BANK |
      | 3  | House services | LIGHTENING |


  Scenario: The one where category is deleted
    When user deletes category of id 2
    Then user category is deleted successfully
    And user categories contain
      | id | name    | icon       | status  |
      | 2  | Savings | PIGGY_BANK | DELETED |
    And user categories version is incremented to 2


  Scenario: The one where category is successfully saved
    When user saves category
      | name      | icon   |
      | Groceries | FOLDER |
    Then user categories contain
      | name      | icon   | status |
      | Groceries | FOLDER | NEW    |
    And user categories version is incremented to 2


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