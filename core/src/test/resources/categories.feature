Feature: Categories


  Background:
    * user categories version is 1
    * user categories are
      | id         | name    | icon       |
      | category-1 | Rent    | HOUSE      |
      | category-2 | Savings | PIGGY_BANK |


  Scenario: The one where saving category has non unique name
    When user creates new category
      | id         | name | icon   |
      | category-3 | Rent | FOLDER |
    Then category is not saved due to constraint violation
      | path | code       |
      | name | NON_UNIQUE |


  Scenario: The one where saving category has empty name
    When user creates new category
      | id         | name | icon   |
      | category-3 |      | FOLDER |
    Then category is not saved due to constraint violation
      | path | code  |
      | name | EMPTY |


  Scenario Outline: The one where saving category has invalid name
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


  Scenario: The one where deleting category does not exist
    When used deletes category of id "category-3"
    Then category is not deleted because it is unknown


  Scenario: The one where category is deleted successfully
    When used deletes category of id "category-2"
    Then user categories contains
      | id         | name | icon  |
      | category-1 | Rent | HOUSE |
