#language: en

Feature: User registration

  Background:
    * users repository contains
      | id | email                  | password |
      | 1  | john.doe@smtm.com      | ***      |
      | 2  | samuel.smith@smtm.com  | ***      |
      | 3  | nick.soapdish@smtm.com | ***      |


  Scenario: User registers successfully
    When user registers as "todd.smith@mail.com" with password "S3cr3t!@#"
    Then user "todd.smith@mail.com" is registered


  Scenario: User already exists
    When user registers as "john.doe@smtm.com" with password "S3cr3t!@#"
    Then message for "email" violation is "Email %email% already exists" with parameters email=john.doe@smtm.com


  Scenario: User's email is invalid
    When user registers as "not-an-email" with password "S3cr3t!@#"
    Then message for "email" violation is "%email% is not a valid email address" with parameters email=not-an-email


  Scenario Outline: User's password does not meet security policy
    When user registers as "todd.smith@mail.com" with password "<password>"
    Then message for "password" violation is "<message>" with parameters <parameters>

    Examples:
      | password      | message                                                       | parameters |
      | SuperS3cr3t   | Password should contain at least %num% special character(s)   | num=1      |
      | super_s3cr3t% | Password should contain at least %num% uppercase character(s) | num=1      |
      | Secret!!!!!!  | Password should contain at least %num% digit                  | num=1      |
      | S3cr3t!       | Password should be at least %num% characters long             | num=8      |
