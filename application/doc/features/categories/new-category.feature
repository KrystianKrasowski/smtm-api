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
