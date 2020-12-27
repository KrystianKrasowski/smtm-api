Feature: Authentication


  Background:
    * request headers are
      | Content-Type | application/smtm.credentials.v1+json |
      | Accept       | application/smtm.token.v1+json       |
    * credentials produces a valid token
      | email              | password     |
      | john.doe@gmail.com | Qwerty123!@# |


  Scenario: User authenticates successfully
    When client performs a POST "/security/token" request with body
      """
      {
        "email": "john.doe@gmail.com",
        "password": "Qwerty123!@#"
      }
      """
    Then response status code is 200
    And response headers are
      | Content-Type | application/smtm.token.v1+json |
    And response body is
      """
      {
        "value": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjA5MzcyODAwfQ.DL9vK3JwQ9Pj8f2GI4lw8RaH0E-tShNBWXTOkZNj_fGClD322vG5DrzZZ5OnXy0HPEjujjaWu8EglC9G-GDLQA"
      }
      """


  Scenario: Provided email address is not registered
    When client performs a POST "/security/token" request with body
      """
      {
        "email": "todd.smith@gmail.com",
        "password": "Qwerty123!@#"
      }
      """
    Then response status code is 401


  Scenario: Provided password is invalid
    When client performs a POST "/security/token" request with body
      """
      {
        "email": "john.doe@gmail.com",
        "password": "admin1"
      }
      """
    Then response status code is 401
