#language: en

Feature: User registration

  Background:
    * users repository contains
      | id | email                  |
      | 1  | john.doe@smtm.com      |
      | 2  | samuel.smith@smtm.com  |
      | 3  | nick.soapdish@smtm.com |


  Scenario: User registers successfully
    When user registers as "todd.smith@mail.com" with password "S3cr3t!@#"
    Then user "todd.smith@mail.com" is registered


  Scenario: User already exists
    When user registers as "john.doe@smtm.com" with password "S3cr3t!@#"
    Then the uniqueness of the email address has been violated


  Scenario: User's email is invalid
    When user registers as "not-an-email" with password "S3cr3t!@#"
    Then email is not valid

  Scenario: User's password does not meet security policy
    When user registers as "todd.smith@mail.com" with password "admin1"
    Then the security policy of the password has been violated
