Feature: Authorization


  Background:
    * valid tokens are
      | eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjA5MzcyODAwfQ.DL9vK3JwQ9Pj8f2GI4lw8RaH0E-tShNBWXTOkZNj_fGClD322vG5DrzZZ5OnXy0HPEjujjaWu8EglC9G-GDLQA |


  Scenario: Client performs an authorized request
    Given request headers are
      | Authorization | Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjA5MzcyODAwfQ.DL9vK3JwQ9Pj8f2GI4lw8RaH0E-tShNBWXTOkZNj_fGClD322vG5DrzZZ5OnXy0HPEjujjaWu8EglC9G-GDLQA |
    When client performs a GET "/test/protected/resource" request
    Then response status code is 200


  Scenario: Authorization header is not bearer type
    Given request headers are
      | Authorization | Basic eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjA5MzcyODAwfQ.DL9vK3JwQ9Pj8f2GI4lw8RaH0E-tShNBWXTOkZNj_fGClD322vG5DrzZZ5OnXy0HPEjujjaWu8EglC9G-GDLQA |
    When client performs a GET "/test/protected/resource" request
    Then response status code is 403


  Scenario: Authorization header contains invalid token
    Given request headers are
      | Authorization | Bearer qwerty123 |
    When client performs a GET "/test/protected/resource" request
    Then response status code is 403
