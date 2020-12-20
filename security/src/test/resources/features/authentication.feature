Feature: Authentication


  Background:
    * users repository contains
      | id | email              | password    |
      | 11 | john.doe@gmail.com | Sup3r$ecret |


  Scenario: User authenticates successfully
    When user authenticates with email "john.doe@gmail.com" and password "Sup3r$ecret"
    Then authorization token is not empty
    And user id is 11


  Scenario Outline: User authentication fails
    When user authenticates with email "<email>" and password "<password>"
    Then authorization token is empty

    Examples:
      | email              | password    |
      | john.doe@gmail.com | admin1      |
      | unknown@gmail.com  | Sup3r$ecret |
