#language: en

Feature: User registration

  Background:
    * users repository contains
      | id | email                  |
      | 1  | john.doe@smtm.com      |
      | 2  | samuel.smith@smtm.com  |
      | 3  | nick.soapdish@smtm.com |


  Scenario: User registers successfully
    When user registers as "todd.smith@mail.com" with password "S3cr3t!"
    Then user "todd.smith@mail.com" is registered
