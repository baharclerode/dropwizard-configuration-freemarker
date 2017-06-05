package zone.dragon.dropwizard.freemarker;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.junit.DropwizardAppRule;
import lombok.Data;
import org.junit.ClassRule;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author Darth Android
 * @date 6/3/2017
 */
public class FreemarkerConfigurationBundleTest {

    public static class TestApp extends Application<TestConfig> {
        @Override
        public void initialize(Bootstrap<TestConfig> bootstrap) {
            FreemarkerConfigurationBundle bundle = FreemarkerConfigurationBundle.addTo(bootstrap);
        }

        @Override
        public void run(TestConfig configuration, Environment environment) throws Exception {

        }
    }

    @Data
    public static class TestConfig extends Configuration {

        private String testProperty;

        private String testQuotedProperty;

        private String testUnquotedProperty;

        private int testIntegerProperty;
    }

    @ClassRule
    public static final DropwizardAppRule<TestConfig> APP_RULE = new DropwizardAppRule<>(TestApp.class, "src/test/resources/config.yaml");

    @Test
    public void testQuotedProperty() {
        TestConfig config = APP_RULE.getConfiguration();
        assertThat(config.getTestQuotedProperty()).isEqualTo("C:\\Users\\someuser\\testVar");
    }

    @Test
    public void testUnquotedProperty() {
        TestConfig config = APP_RULE.getConfiguration();
        assertThat(config.getTestUnquotedProperty()).isEqualTo("C:\\Users\\someuser\\testVar");
    }
}
