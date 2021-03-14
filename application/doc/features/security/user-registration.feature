Feature: User registration


  Background:
    * request headers are
      | Content-Type | application/smtm.credentials.v1+json  |
      | Accept       | application/smtm.user-profile.v1+json |


  Scenario: User registers successfully
    When client performs a POST "/security/users" request with body
      """
      {
          "email": "john.doe@gmail.com",
          "password": "******"
      }
      """
    Then response status code is 201
    And response headers are
      | Content-Type | application/smtm.user-profile.v1+json |
    And response body is
      """
      {
          "id": 1,
          "email": "john.doe@gmail.com",
          "links": [
              {
                  "rel": "self",
                  "href": "http://localhost/security/users/1"
              }
          ]
      }
      """


  Scenario: User registration fails due to constraint violations
    Given constraint violations are
      | property | message                                                     | parameters               |
      | email    | %email% is not a valid e-mail address                       | email=john.doe#gmail.com |
      | password | Password should contain at least %num% special character(s) | num=1                    |
    When client performs a POST "/security/users" request with body
      """
      {
        "email": "john.doe@gmail.com",
        "password": "******"
      }
      """
    Then response status code is 400
    And response headers are
      | Content-Type | application/problem+json |
    And response body is
      """
      {
        "title": "Provided credentials violate some of the constraints",
        "violations": [
          {
            "property": "email",
            "message": {
              "pattern": "%email% is not a valid e-mail address",
              "parameters": {
                "email": "john.doe#gmail.com"
              }
            }
          },
          {
            "property": "password",
            "message": {
              "pattern": "Password should contain at least %num% special character(s)",
              "parameters": {
                "num": "1"
              }
            }
          }
        ]
      }
      """
