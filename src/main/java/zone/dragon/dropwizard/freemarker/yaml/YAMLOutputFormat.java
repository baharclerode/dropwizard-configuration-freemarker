package zone.dragon.dropwizard.freemarker.yaml;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import freemarker.core.CommonMarkupOutputFormat;
import freemarker.core.OutputFormat;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Darth Android
 * @date 6/3/2017
 */
public class YAMLOutputFormat extends CommonMarkupOutputFormat<TemplateYAMLOutputModel> {
    /**
     * The only instance (singleton) of this {@link OutputFormat}.
     */
    public static final YAMLOutputFormat INSTANCE = new YAMLOutputFormat();
    private static final JsonStringEncoder ENCODER = JsonStringEncoder.getInstance();

    private YAMLOutputFormat() {
        // Only to decrease visibility
    }

    @Override
    public String getName() {
        return "YAML";
    }

    @Override
    public String getMimeType() {
        return "text/vnd.yaml";
    }

    @Override
    public void output(String textToEsc, Writer out) throws IOException, TemplateModelException {
        out.write(ENCODER.quoteAsString(textToEsc));
    }

    @Override
    public String escapePlainText(String plainTextContent) {
        return new String(ENCODER.quoteAsString(plainTextContent));
    }

    @Override
    public boolean isLegacyBuiltInBypassed(String builtInName) {
        return false;
    }

    @Override
    protected TemplateYAMLOutputModel newTemplateMarkupOutputModel(String plainTextContent, String markupContent) {
        return new TemplateYAMLOutputModel(plainTextContent, markupContent);
    }

}
