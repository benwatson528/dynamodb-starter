package uk.co.hadoopathome.dynamo.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.*;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmbeddedDynamoService extends ExternalResource {
  private static Logger LOGGER = LoggerFactory.getLogger(EmbeddedDynamoService.class);
  private AmazonDynamoDB dynamoDBService;

  public DynamoDB getDynamoDBClient() {
    return new DynamoDB(this.dynamoDBService);
  }

  @Override
  protected void before() {
    LOGGER.info("Setting up embedded DynamoDB instance");
    this.dynamoDBService = DynamoDBEmbedded.create().amazonDynamoDB();
    createTable("test-table");
  }

  private void createTable(String tableName) {
    List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
    attributeDefinitions.add(
        new AttributeDefinition().withAttributeName("Id").withAttributeType("N"));
    List<KeySchemaElement> keySchema = new ArrayList<>();
    keySchema.add(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH));
    CreateTableRequest request =
        new CreateTableRequest()
            .withTableName(tableName)
            .withKeySchema(keySchema)
            .withAttributeDefinitions(attributeDefinitions)
            .withProvisionedThroughput(
                new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(6L));
    this.dynamoDBService.createTable(request);
    LOGGER.info("Created table {}", tableName);
  }

  @Override
  protected void after() {
    LOGGER.info("Closing embedded DynamoDB instance");
    if (this.dynamoDBService != null) {
      this.dynamoDBService.shutdown();
    }
  }
}
