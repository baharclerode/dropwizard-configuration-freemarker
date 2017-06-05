# Dropwizard Freemarker Templates Configuration

This bundle enables freemarker template processing of an applications `config.yaml` file, making it easy to interpolate environmental 
variables, system properties, and other boot-time properties (such as passwords and secrets) into your application config, as well as 
making structural changes and alterations to the configuration based on settings such as the OS type or other environmental cues.

To use this bundle, add it to your application in the initialize method:

    @Override
    public void initialize(Bootstrap<YourConfig> bootstrap) {
        FreemarkerConfigurationBundle freemarker = FreemarkerConfigurationBundle.addTo(bootstrap);
        // freemarker.getDataModel() and freemarker.getConfiguration() can be used to customize the templating engine
        // before the configuration is loaded
    }

The initial data model contains all environmental variables under the `env` key, such as `${env.PATH}` or `${env.HOME}`, and all system 
properties under the `system` key, such as `${system["user.dir"]}`

### Escaping

By default, any strings will be escaped, so interpolations of strings should be *double-quoted*:

    homeDir: "${env.HOME}"

to insert an unescaped string, use the `no_esc` operator:

    homeDir: ${env.HOME?no_esc}

### Custom DataModel

To add additional properties to the data model or alter existing ones, add them to the map returned by `getDataModel()` on the bundle.