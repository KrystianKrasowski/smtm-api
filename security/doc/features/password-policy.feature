#language: en

Feature: Password policy

  Background:
    * special characters are "!@#$%^&*()_+=-"
    * minimum length is 8


  Scenario: Password does not contain at least one special character
    When password "SuperS3cr3t" is validated
    Then password has not enough special characters


  Scenario: Password does not contain at least one uppercase letter
    When password "super_s3cr3t%" is validated
    Then password has not enough uppercase letters


  Scenario: Password does not contain at least one digit
    When password "Secret!!!!!!" is validated
    Then password has not enough digits


  Scenario: Password is too short
    When password "S3cr3t!" is validated
    Then password has not enough length


  Scenario Outline: Password meets security policy
    When password "<password>" is validated
    Then password has no violations

    Examples:
      | password                   |
      | S3cr3t!@#                  |
      | sUUperDOOp3r$3Cr3tP@$$wOrd |
