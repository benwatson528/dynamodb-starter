package uk.co.hadoopathome.dynamo.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import uk.co.hadoopathome.dynamo.service.EmbeddedDynamoService;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = "pretty", features = "src/test/resources/features")
public class CucumberRunner {
  @ClassRule
  public static EmbeddedDynamoService EMBEDDED_DYNAMO_SERVER = new EmbeddedDynamoService();
}
