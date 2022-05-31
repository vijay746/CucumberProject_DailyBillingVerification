Feature: LoginFeature
  This feature deals with the login functionality of the application

  Scenario: Check if right category is shown for a product
    Given user navigates to ABC product page
    When user search for a product
    Then right category is shown