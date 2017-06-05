package zone.dragon.dropwizard.freemarker.yaml;

import freemarker.core.CommonTemplateMarkupOutputModel;

/**
 * Stores RTF markup to be printed; used with {@link YAMLOutputFormat}.
 */
public class TemplateYAMLOutputModel extends CommonTemplateMarkupOutputModel<TemplateYAMLOutputModel> {
    /**
     * See {@link CommonTemplateMarkupOutputModel#CommonTemplateMarkupOutputModel(String, String)}.
     */
    TemplateYAMLOutputModel(String plainTextContent, String markupContent) {
        super(plainTextContent, markupContent);
    }

    @Override
    public YAMLOutputFormat getOutputFormat() {
        return YAMLOutputFormat.INSTANCE;
    }
}
