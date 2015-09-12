
package com.github.quiram.buildhotspots.drawingdata;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.github.quiram.buildhotspots.drawingdata package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.github.quiram.buildhotspots.drawingdata
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Root }
     *
     */
    public Root createRoot() {
        return new Root();
    }

    /**
     * Create an instance of {@link BuildConfigurationType }
     *
     */
    public BuildConfigurationType createBuildConfigurationType() {
        return new BuildConfigurationType();
    }

    /**
     * Create an instance of {@link Root.BuildConfigurations }
     *
     */
    public Root.BuildConfigurations createRootBuildConfigurations() {
        return new Root.BuildConfigurations();
    }

    /**
     * Create an instance of {@link DependencyType }
     *
     */
    public DependencyType createDependencyType() {
        return new DependencyType();
    }

    /**
     * Create an instance of {@link BuildConfigurationType.Dependencies }
     *
     */
    public BuildConfigurationType.Dependencies createBuildConfigurationTypeDependencies() {
        return new BuildConfigurationType.Dependencies();
    }

    /**
     * Create an instance of {@link BuildConfigurationType.BuildStats }
     *
     */
    public BuildConfigurationType.BuildStats createBuildConfigurationTypeBuildStats() {
        return new BuildConfigurationType.BuildStats();
    }

}
