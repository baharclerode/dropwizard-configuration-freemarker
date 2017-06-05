package zone.dragon.dropwizard.freemarker;

import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.Map;

/**
 * @author Darth Android
 * @date 6/2/2017
 */
public class FreemarkerConfigurationBundle implements Bundle {
    public static final String ENVIRONMENT_KEY = "env";
    public static final String SYSTEM_PROPERTY_KEY = "system";

    public static FreemarkerConfigurationBundle addTo(Bootstrap<?> bootstrap) {
        FreemarkerConfigurationBundle bundle = new FreemarkerConfigurationBundle();
        bootstrap.addBundle(bundle);
        return bundle;
    }

    private final Map<String, Object> dataModel = Maps.newHashMap();
    private FreemarkerConfigurationSourceProvider sourceProvider;

    public FreemarkerConfigurationBundle() {
        getDataModel().put(ENVIRONMENT_KEY, System.getenv());
        getDataModel().put(SYSTEM_PROPERTY_KEY, System.getProperties());
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        sourceProvider = new FreemarkerConfigurationSourceProvider(bootstrap.getConfigurationSourceProvider(), dataModel);
        bootstrap.setConfigurationSourceProvider(sourceProvider);
    }

    @Override
    public void run(Environment environment) {
        // not used
    }

    public Map<String, Object> getDataModel() {
        return dataModel;
    }

    public Configuration getFreemarkerConfig() {
        return sourceProvider;
    }
}
