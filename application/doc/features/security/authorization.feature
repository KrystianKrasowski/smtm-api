Feature: Authorization


  Background:
    * valid tokens are
      | 1234567890 |


  Scenario: Client performs an authorized request
    Given request headers are
      | Authorization | Bearer 1234567890 |
    When client performs a GET "/test/protected/resource" request
    Then response status code is 200


  Scenario: Authorization header is not bearer type
    Given request headers are
      | Authorization | Basic 1234567890 |
    When client performs a GET "/test/protected/resource" request
    Then response status code is 403


  Scenario: Authorization header contains invalid token
    Given request headers are
      | Authorization | Bearer qwerty123 |
    When client performs a GET "/test/protected/resource" request
    Then response status code is 403
