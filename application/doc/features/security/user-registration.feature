Feature: User registration


  Background:
    * request headers are
      | Content-Type | application/smtm.credentials.v1+json  |
      | Accept       | application/smtm.user-profile.v1+json |


  Scenario: User registers successfully
    When client calls "/security/users" with body
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
      | property | violation             |
      | email    | NotAnEmailAddress     |
      | password | NotEnoughSpecialChars |
    When client calls "/security/users" with body
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
            "violation": "NotAnEmailAddress"
          },
          {
            "property": "password",
            "violation": "NotEnoughSpecialChars"
          }
        ]
      }
      """
