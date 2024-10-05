Feature: Wallets


  Background:
    * user wallets are
      | id       | name    | icon       |
      | wallet-1 | Rent    | HOUSE      |
      | wallet-2 | Savings | PIGGY_BANK |


  Scenario: The one where creating wallet has non unique name
    When user creates new wallet
      | id       | name | icon   |
      | wallet-3 | Rent | FOLDER |
    Then wallet is not saved due to constraint violation
      | path | code       |
      | name | NON_UNIQUE |


  Scenario: The one where creating wallet has empty name
    When user creates new wallet
      | id       | name | icon   |
      | wallet-3 |      | FOLDER |
    Then wallet is not saved due to constraint violation
      | path | code  |
      | name | EMPTY |


  Scenario Outline: The one where creating wallet has invalid name
    When user creates new wallet
      | id       | name   | icon   |
      | wallet-3 | <name> | FOLDER |
    Then wallet is not saved due to constraint violation
      | path | code               | illegal characters   |
      | name | ILLEGAL_CHARACTERS | <illegal characters> |

    Examples:
      | name      | illegal characters |
      | Rent<>    | <, >               |
      | Awesome!! | !                  |


  Scenario: The one where updating wallet has empty name
    When user updates the wallet
      | id       | name | icon       |
      | wallet-2 |      | PIGGY_BANK |
    Then wallet is not saved due to constraint violation
      | path | code  |
      | name | EMPTY |


  Scenario Outline: The one where updating wallet has invalid name
    When user updates the wallet
      | id       | name   | icon   |
      | wallet-2 | <name> | FOLDER |
    Then wallet is not saved due to constraint violation
      | path | code               | illegal characters   |
      | name | ILLEGAL_CHARACTERS | <illegal characters> |

    Examples:
      | name      | illegal characters |
      | Rent<>    | <, >               |
      | Awesome!! | !                  |


  Scenario: The one where updating wallet does not exist
    When user updates the wallet
      | id        | name    | icon   |
      | wallet-?? | Unknown | FOLDER |
    Then unknown wallet problem occurs


  Scenario: The one where deleting wallet does not exist
    When user deletes wallet of id "wallet-3"
    Then unknown wallet problem occurs
