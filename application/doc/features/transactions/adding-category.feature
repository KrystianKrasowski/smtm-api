Feature: New category


  Background:
    * valid tokens are
      | 1234567890 |
    * request headers are
      | Content-Type  | application/smtm.category.v1+json |
      | Accept        | application/smtm.category.v1+json |
      | Authorization | Bearer 1234567890                 |


  Scenario: Category is created successfully
    When client performs a POST "/categories" request with body
      """
      {
        "name": "Groceries",
        "icon": "ShoppingCart"
      }
      """
    Then response status code is 201
    And response headers are
      | Content-Type | application/smtm.category.v1+json |
    And response body is
      """
      {
        "id": 1,
        "name": "Groceries",
        "icon": "ShoppingCart",
        "links": [
          {
            "rel": "self",
            "href": "http://localhost/categories/1"
          }
        ]
      }
      """


  Scenario Outline: Category violates some constraints
    Given category violates "<property>" property constraint
    And violation message pattern is "<message pattern>" with parameters
      | <parameter name> | <parameter value> |
    When client performs a POST "/categories" request with body
      """
      {
        "name": "Rent",
        "icon": "House"
      }
      """
    Then response status code is 400
    And response headers are
      | Content-Type | application/problem+json |
    And response body is
      """
      {
        "title": "Provided category violates some of the constraints",
        "violations": [
          {
            "property": "<property>",
            "message": {
              "pattern": "<message pattern>",
              "parameters": {
                "<parameter name>": "<parameter value>"
              }
            }
          }
        ]
      }
      """

    Examples:
      | property         | message pattern                                                 | parameter name     | parameter value |
      | name             | Category name contains illegal characters: %illegal-characters% | illegal-characters | !, @, #, $, %   |
      | awesome property | Just another message for %category%                             | category           | Rent            |
