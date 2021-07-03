Feature: Authentication


  Background:
    * new access token "foo" is produced by credentials
      | email              | password     |
      | john.doe@gmail.com | Qwerty123!@# |
    * new access token "foo" is produced by refresh token "baz"
    * new refresh token "bar" is produced by credentials
      | email              | password     |
      | john.doe@gmail.com | Qwerty123!@# |
    * new refresh token "bar" is produced by refresh token "baz"


  Scenario: User authenticates successfully
    Given request headers are
      | Content-Type | application/smtm.credentials.v1+json |
      | Accept       | application/smtm.tokens.v1+json      |
    When client performs a POST "/security/tokens" request with body
      """
      {
        "email": "john.doe@gmail.com",
        "password": "Qwerty123!@#"
      }
      """
    Then response status code is 200
    And response headers are
      | Content-Type | application/smtm.tokens.v1+json |
    And response body is
      """
      {
        "accessToken": "foo",
        "refreshToken": "bar"
      }
      """


  Scenario: Provided email address is not registered
    Given request headers are
      | Content-Type | application/smtm.credentials.v1+json |
      | Accept       | application/smtm.tokens.v1+json      |
    When client performs a POST "/security/tokens" request with body
      """
      {
        "email": "todd.smith@gmail.com",
        "password": "Qwerty123!@#"
      }
      """
    Then response status code is 401


  Scenario: Provided password is invalid
    Given request headers are
      | Content-Type | application/smtm.credentials.v1+json |
      | Accept       | application/smtm.tokens.v1+json      |
    When client performs a POST "/security/tokens" request with body
      """
      {
        "email": "john.doe@gmail.com",
        "password": "admin1"
      }
      """
    Then response status code is 401


  Scenario: User has their access token refreshed
    Given request headers are
      | Content-Type | application/smtm.refresh-token.v1+json |
      | Accept       | application/smtm.tokens.v1+json        |
    When client performs a POST "/security/tokens" request with body
      """
      {
        "token": "baz"
      }
      """
    Then response status code is 200
    And response headers are
      | Content-Type | application/smtm.tokens.v1+json |
    And response body is
      """
      {
        "accessToken": "foo",
        "refreshToken": "bar"
      }
      """


  Scenario: Refresh token is invalid
    Given request headers are
      | Content-Type | application/smtm.refresh-token.v1+json |
      | Accept       | application/smtm.tokens.v1+json        |
    And invalid refresh token is "bat"
    When client performs a POST "/security/tokens" request with body
      """
      {
        "token": "bat"
      }
      """
    Then response status code is 401
