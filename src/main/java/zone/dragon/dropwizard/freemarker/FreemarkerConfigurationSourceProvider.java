package zone.dragon.dropwizard.freemarker;

import freemarker.cache.ConditionalTemplateConfigurationFactory;
import freemarker.cache.FileExtensionMatcher;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.TemplateConfiguration;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import io.dropwizard.configuration.ConfigurationSourceProvider;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import zone.dragon.dropwizard.freemarker.yaml.YAMLOutputFormat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

@Slf4j
public class FreemarkerConfigurationSourceProvider extends Configuration implements ConfigurationSourceProvider {
    private final ConfigurationSourceProvider delegate;
    private final Object                      dataModel;

    public FreemarkerConfigurationSourceProvider(@NonNull ConfigurationSourceProvider delegate, @NonNull Object dataModel) {
        super(VERSION_2_3_23);
        this.delegate = delegate;
        this.dataModel = dataModel;
        setDefaultEncoding(StandardCharsets.UTF_8.name());
        setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        setLogTemplateExceptions(false);
        setTemplateConfigurations(new ConditionalTemplateConfigurationFactory(new FileExtensionMatcher("yaml"),
                                                                              createYamlTemplateConfiguration()
        ));
        try {
            setTemplateLoader(new FileTemplateLoader(Paths.get("").toAbsolutePath().toFile()));
        } catch (IOException e) {
            log.error("Failed to configure template loading from working directory (ignored)", e);
        }
    }

    protected TemplateLoader createTemplateLoader(File configYaml, InputStream configStream) {
        try {
            StringTemplateLoader rootTemplate = new StringTemplateLoader();
            rootTemplate.putTemplate(configYaml.getName(), IOUtils.toString(configStream, StandardCharsets.UTF_8));
            FileTemplateLoader templateDir = new FileTemplateLoader(Paths.get("").toAbsolutePath().toFile());
            return new MultiTemplateLoader(new TemplateLoader[]{rootTemplate, templateDir});
        } catch (IOException e) {
            throw new RuntimeException("Failed to configure freemarker template input", e);
        }
    }

    protected TemplateConfiguration createYamlTemplateConfiguration() {
        TemplateConfiguration yamlConfig = new TemplateConfiguration();
        yamlConfig.setParentConfiguration(this);
        yamlConfig.setOutputFormat(YAMLOutputFormat.INSTANCE);
        yamlConfig.setNumberFormat("computer");
        return yamlConfig;
    }

    @Override
    public InputStream open(String path) throws IOException {
        InputStream           originalStream        = delegate.open(path);
        TemplateConfiguration templateConfiguration = createYamlTemplateConfiguration();
        Template template = new Template(null,
                                         null,
                                         new InputStreamReader(originalStream),
                                         this,
                                         templateConfiguration,
                                         StandardCharsets.UTF_8.name()
        );
        templateConfiguration.apply(template);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            template.process(dataModel, new OutputStreamWriter(baos));
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (TemplateException e) {
            throw new RuntimeException("Failed to process template", e);
        }
    }
}
