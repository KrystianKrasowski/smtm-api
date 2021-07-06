Feature: Refresh token authentication


  Background:
    * users repository contains
      | id | email              | password    |
      | 11 | john.doe@gmail.com | Sup3r$ecret |
    * refresh token repository contains
      | subject | id                                   |
      | 11      | 35b0fd68-0cee-4a7c-8954-eaf6eb4ec78d |
    * current time is 2021-07-03 12:00:00


  Scenario: User authenticates successfully by refresh token
    Given access token validity time is 10 minutes
    And refresh token validity time is 30 minutes
    And next GUID is "4e5471b7-87da-4790-a25e-1851f65fa464"
    When user authenticates with refresh token
      | subject | expires at          | id                                   |
      | 11      | 2021-07-03 12:10:00 | 35b0fd68-0cee-4a7c-8954-eaf6eb4ec78d |
    Then access token is
      | subject | expires at          |
      | 11      | 2021-07-03 12:10:00 |
    And refresh token is
      | subject | expires at          | id                                   |
      | 11      | 2021-07-03 12:30:00 | 4e5471b7-87da-4790-a25e-1851f65fa464 |
    And refresh token "4e5471b7-87da-4790-a25e-1851f65fa464" is stored for subject 11


  Scenario: Refresh token expired
    When user authenticates with refresh token
      | subject | expires at          | id                                   |
      | 11      | 2021-07-03 11:30:00 | 35b0fd68-0cee-4a7c-8954-eaf6eb4ec78d |
    Then access token is empty
    And refresh token is empty


  Scenario: Refresh token contains unknown user
    When user authenticates with refresh token
      | subject | expires at          | id                                   |
      | 99      | 2021-07-03 12:30:00 | 35b0fd68-0cee-4a7c-8954-eaf6eb4ec78d |
    Then access token is empty
    And refresh token is empty


  Scenario: Refresh token is unknown
    When user authenticates with refresh token
      | subject | expires at          | id                                   |
      | 11      | 2021-07-03 12:30:00 | 090b1f65-e174-44ab-b8dd-5ae9e0436d85 |
    Then access token is empty
    And refresh token is empty
