Feature: Categories


  Background:
    * user categories version is 1
    * user categories are
      | id | name    | icon       |
      | 1  | Rent    | HOUSE      |
      | 2  | Savings | PIGGY_BANK |


  Scenario: The one where saving category has not unique name
    When user saves category with name "Rent"
    Then category is not saved due to constraint violation
      | path | code       |
      | name | NON_UNIQUE |


  Scenario: The one where saving category has empty name
    When user saves category with empty name
    Then category is not saved due to constraint violation
      | path | code  |
      | name | EMPTY |


  Scenario Outline: The one where saving category has invalid name
    When user saves category with name "<name>"
    Then category is not saved due to constraint violation
      | path | code               | illegal characters   |
      | name | ILLEGAL_CHARACTERS | <illegal characters> |

    Examples:
      | name      | illegal characters |
      | Rent<>    | <, >               |
      | Awesome!! | !                  |


  Scenario: The one where deleting category does not exist
    When used deletes category of id 3
    Then category is not deleted because it is unknown


  Scenario: The one where category is created successfully
    Given next category id is 4
    When user saves category
      | name      | icon          |
      | Groceries | SHOPPING_CART |
    Then saved category is
      | id | name      | icon          |
      | 4  | Groceries | SHOPPING_CART |
    And user categories version is updated to 2


  Scenario Outline: The one where category is updated successfully
    When user saves category
      | id | name   | icon   |
      | 1  | <name> | <icon> |
    Then saved category is
      | id | name   | icon   |
      | 1  | <name> | <icon> |

    Examples:
      | name         | icon       |
      | Awesome rent | HOUSE      |
      | Awesome rent | LIGHTENING |
      | Rent         | LIGHTENING |


  Scenario: The one where category is deleted successfully
    When used deletes category of id 2
    Then user categories contains
      | id | name | icon  |
      | 1  | Rent | HOUSE |
    And user categories version is updated to 2