Feature: Plans


  Background:
    * user categories version is 1
    * user categories are
      | id | name    | icon       |
      | 1  | Rent    | HOUSE      |
      | 2  | Savings | PIGGY_BANK |


  Scenario: The one where planned category does not exist
    When user saves a new plan
      | name      | start      | end        | entries                                                 |
      | July 2023 | 2023-07-01 | 2023-07-31 | Rent = PLN 300, Savings = PLN 5000, Groceries = PLN 750 |
    Then plan is not saved due to constraint violation
      | path                | code    |
      | /entries/2/category | UNKNOWN |


  Scenario: The one where plan is created successfully
    When user saves a new plan
      | name      | start      | end        | entries                            |
      | July 2023 | 2023-07-01 | 2023-07-31 | Rent = PLN 300, Savings = PLN 5000 |
    Then plan is saved