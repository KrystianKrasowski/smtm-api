Feature: Authentication


  Background:
    * users repository contains
      | id | email              | password    |
      | 11 | john.doe@gmail.com | Sup3r$ecret |
    * access token validity time is 10 minutes
    * refresh token validity time is 30 minutes
    * current time is 2021-07-03 12:00:00
    * next GUID is "4e5471b7-87da-4790-a25e-1851f65fa464"


  Scenario: User authenticates successfully by credentials
    When user authenticates with email "john.doe@gmail.com" and password "Sup3r$ecret"
    Then access token is
      | subject | expires at          |
      | 11      | 2021-07-03 12:10:00 |
    And refresh token is
      | subject | expires at          | id                                   |
      | 11      | 2021-07-03 12:30:00 | 4e5471b7-87da-4790-a25e-1851f65fa464 |


  Scenario Outline: User credentials authentication fails
    When user authenticates with email "<email>" and password "<password>"
    Then access token is empty
    And refresh token is empty

    Examples:
      | email              | password    |
      | john.doe@gmail.com | admin1      |
      | unknown@gmail.com  | Sup3r$ecret |


  Scenario: User authenticates successfully by refresh token
    Given current time is 2021-07-03 12:25:00
    When user authenticates with refresh token
      | subject | expires at          |
      | 11      | 2021-07-03 12:30:00 |
    Then access token is
      | subject | expires at          |
      | 11      | 2021-07-03 12:35:00 |
    And refresh token is
      | subject | expires at          | id                                   |
      | 11      | 2021-07-03 12:55:00 | 4e5471b7-87da-4790-a25e-1851f65fa464 |


  Scenario Outline: User refresh token authentication fails
    Given current time is <current time>
    When user authenticates with refresh token
      | subject   | expires at          |
      | <subject> | 2021-07-03 12:30:00 |
    Then access token is empty
    And refresh token is empty

    Examples:
      | current time        | subject |
      | 2021-07-03 12:30:01 | 11      |
      | 2021-07-03 12:00:00 | 99      |
