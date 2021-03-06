package com.littlesquare.crackerjack.services.common.config

import io.dropwizard.db.DataSourceFactory

/**
 * A data source factory that will load configuration from a DataSource.properties file that exists either on:
 * - the root classpath (classpath:///DataSource.properties)
 * or
 * - a conf directory (file://`pwd`/conf/DataSource.properties)
 *
 * @author Adam Jordens (adam@jordens.org)
 */
public class ClasspathPropertiesFileDataSourceFactory extends DataSourceFactory {
    private static String DEFAULT_PROPERTIES_FILE = "DataSource.properties"

    public ClasspathPropertiesFileDataSourceFactory(String configurationDirectory = "conf/") {
        String errorMessage = "Unable to load DataSource credentials from the " + DEFAULT_PROPERTIES_FILE + " file on the classpath"

        InputStream inputStream = getClass().getResourceAsStream("/" + configurationDirectory + DEFAULT_PROPERTIES_FILE)
        if (inputStream == null) {
            try {
                inputStream = new File(configurationDirectory, DEFAULT_PROPERTIES_FILE).newInputStream()
            } catch (FileNotFoundException e) {
                return
            }
        }

        try {
            Properties datasourceProperties = new Properties()
            try {
                datasourceProperties.load(inputStream);
            } finally {
                try {
                    inputStream.close()
                } catch (Exception e) {
                }
            }

            if (datasourceProperties.getProperty("user") == null || datasourceProperties.getProperty("password") == null) {
                throw new IllegalArgumentException(
                        "The specified properties data doesn't contain the expected properties 'user' and 'password'."
                );
            }


            String user = datasourceProperties.getProperty("user")
            String password = datasourceProperties.getProperty("password")

            super.setUser(user)
            super.setPassword(password)
        } catch (IOException e) {
            throw new IllegalStateException(errorMessage, e);
        }
    }
}
