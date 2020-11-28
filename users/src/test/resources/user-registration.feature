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


  Scenario: User already exists
    When user registers as "john.doe@smtm.com" with password "S3cr3t!"
    Then the uniqueness of the email address has been violated


  Scenario Outline: User's password does not meet security policy
    When user registers as "todd.smith@mail.com" with password "<password>"
    Then the security policy of the password has been violated

    Examples:
      | password              |
      | NoDigits!             |
      | N0SpecialChars        |
      | n0_upper_case_letters |
