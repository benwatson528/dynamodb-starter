Feature: Interacting with embedded DynamoDB
  Confirming that embedded DynamoDB works correctly

  Scenario: Reading and writing data to DynamoDB
    Given the embedded DynamoDB instance is running
    When I insert data into DynamoDB
    And I retrieve that data
    Then I can confirm the data was correctly stored