/*
 * Sakuli - Testing and Monitoring-Tool for Websites and common UIs.
 *
 * Copyright 2013 - 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sakuli.services.forwarder;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.spaceless.SpacelessExtension;
import org.jtwig.spaceless.configuration.SpacelessConfiguration;
import org.sakuli.services.forwarder.checkmk.CheckMKResultServiceImpl;
import org.sakuli.services.forwarder.configuration.ExtractScreenshotFunction;
import org.sakuli.services.forwarder.configuration.GetOutputStateFunction;
import org.sakuli.services.forwarder.configuration.LeadingWhitespaceRemover;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author Georgi Todorov
 */
public abstract class AbstractTemplateOutputBuilder extends AbstractOutputBuilder {

    private static final Logger logger = LoggerFactory.getLogger(CheckMKResultServiceImpl.class);

    public static final String TEMPLATE_EXTENSION = ".twig";
    public static final String GLOBAL_TEST_CASE_ID_FORMAT = "%s_%s";
    public static final String GLOBAL_TEST_STEP_ID_FORMAT = "%s_%s_%s";

    /**
     * Returns the name of the converter. The name is used to dynamically retrieve the template for the converter.
     * @return the name of the converter
     */
    public abstract String getConverterName();

    /**
     * Converts the current test suite to a string based on the template for the concrete converter.
     * @return
     */
    public String createOutput() {
        JtwigModel model = createModel();
        String templatePath = getTemplatePath();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Load JTwig template from following location: '%s'", templatePath));
        }
        EnvironmentConfiguration configuration = EnvironmentConfigurationBuilder.configuration()
                .extensions()
                    .add(new SpacelessExtension(new SpacelessConfiguration(new LeadingWhitespaceRemover())))
                .and()
                .functions()
                    .add(new GetOutputStateFunction())
                    .add(new ExtractScreenshotFunction(screenshotDivConverter))
                .and()
                .build();
        JtwigTemplate template = JtwigTemplate.fileTemplate(new File(templatePath), configuration);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Render model into JTwig template. Model: '%s'", model));
        }
        return template.render(model);
    }

    protected JtwigModel createModel() {
        JtwigModel model = JtwigModel.newModel()
                .with("testsuite", testSuite);
        return model;
    }

    protected String getTemplatePath() {
        String templateFolder = sakuliProperties.getForwarderTemplateFolder();
        String templatePath =
                templateFolder
                        + System.getProperty("file.separator")
                        + getConverterName().toLowerCase()
                        + TEMPLATE_EXTENSION;
        return templatePath;
    }

    @Override
    protected int getSummaryMaxLength() {
        // operation is not used for the template output builder
        return 0;
    }

    @Override
    protected String getOutputScreenshotDivWidth() {
        // operation is not used for the template output builder
        return null;
    }

}