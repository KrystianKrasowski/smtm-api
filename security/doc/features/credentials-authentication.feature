Feature: Authentication


  Background:
    * users repository contains
      | id | email              | password    |
      | 11 | john.doe@gmail.com | Sup3r$ecret |


  Scenario: User authenticates successfully by credentials
    Given current time is 2021-07-03 12:00:00
    And access token validity time is 10 minutes
    And refresh token validity time is 30 minutes
    And next GUID is "4e5471b7-87da-4790-a25e-1851f65fa464"
    When user authenticates with email "john.doe@gmail.com" and password "Sup3r$ecret"
    Then access token is
      | subject | expires at          |
      | 11      | 2021-07-03 12:10:00 |
    And refresh token is
      | subject | expires at          | id                                   |
      | 11      | 2021-07-03 12:30:00 | 4e5471b7-87da-4790-a25e-1851f65fa464 |
    And refresh token "4e5471b7-87da-4790-a25e-1851f65fa464" is stored for subject 11


  Scenario: User email is unknown
    When user authenticates with email "unknown@gmail.com" and password "Sup3r$ecret"
    Then access token is empty
    And refresh token is empty


  Scenario: User password is invalid
    When user authenticates with email "john.doe@gmail.com" and password "admin123"
    Then access token is empty
    And refresh token is empty
