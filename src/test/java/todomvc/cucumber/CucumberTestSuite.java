package todomvc.cucumber;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.*;

/**
 * Cucumber test runner for the TodoMVC Playwright Screenplay tests.
 * <p>
 * This uses the JUnit Platform Suite to run Cucumber scenarios.
 * The glue code (step definitions and hooks) is in the todomvc.cucumber package.
 * Feature files are in src/test/resources/features.
 */
@Suite
@IncludeEngines("cucumber")
@SelectPackages("todomvc.cucumber")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "net.serenitybdd.cucumber.core.plugin.SerenityReporterParallel,pretty")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "todomvc.cucumber,net.serenitybdd.cucumber.actors")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "src/test/resources/features")
@ConfigurationParameter(key = FILTER_TAGS_PROPERTY_NAME, value = "@playwright")
public class CucumberTestSuite {
}
