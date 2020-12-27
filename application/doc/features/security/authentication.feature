Feature: Authentication


  Background:
    * request headers are
      | Content-Type | application/smtm.credentials.v1+json |
      | Accept       | application/smtm.token.v1+json       |
    * token "[super-secret-token]" is produced by credentials
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
        "value": "[super-secret-token]"
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
