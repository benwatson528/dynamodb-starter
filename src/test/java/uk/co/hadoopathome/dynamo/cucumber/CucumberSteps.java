package uk.co.hadoopathome.dynamo.cucumber;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.hadoopathome.dynamo.service.EmbeddedDynamoService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CucumberSteps {
  private static final String TABLE_NAME = "test-table";
  private static Logger LOGGER = LoggerFactory.getLogger(CucumberSteps.class);
  private DynamoDB dynamoDB;

  private Table table;
  private Item retrievedItem;
  private EmbeddedDynamoService embeddedDynamoService;

  @Before
  public void before() {
    System.setProperty("sqlite4java.library.path", "libs/sqlite4java-392");
    this.embeddedDynamoService = new EmbeddedDynamoService();
    this.dynamoDB = this.embeddedDynamoService.getDynamoDBClient();
  }

  @Given("the embedded DynamoDB instance is running")
  public void theEmbeddedDynamoDBInstanceIsRunning() {
    try {
      this.table = this.dynamoDB.getTable(TABLE_NAME);
      LOGGER.info(
          "The embedded DynamoDB instance is running, and the test table is available with the following"
              + " settings {}",
          table.describe());
    } catch (AmazonClientException e) {
      LOGGER.error("The DynamoDB instance is not running");
      fail();
    }
  }

  @When("I insert data into DynamoDB")
  public void iInsertDataIntoDynamoDB() {
    Item item = new Item().withPrimaryKey("Id", 1).withString("PetName", "Bob");
    this.table.putItem(item);
    LOGGER.info("Put item {}", item);
  }

  @And("I retrieve that data")
  public void iRetrieveThatData() {
    this.retrievedItem = this.table.getItem("Id", 1);
    LOGGER.info("Retrieved item {}", this.retrievedItem);
  }

  @Then("I can confirm the data was correctly stored")
  public void iCanConfirmTheDataWasCorrectlyStored() {
    assertEquals("Bob", this.retrievedItem.getString("PetName"));
  }

  @After
  public void after() {
    this.embeddedDynamoService.shutdown();
  }
}
