
package com.github.quiram.buildhotspots.drawingdata;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DependencyType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DependencyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BuildConfigurationName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DependencyType", propOrder = { "buildConfigurationName" })
public class DependencyType {

    @XmlElement(name = "BuildConfigurationName", required = true)
    protected String buildConfigurationName;

    /**
     * Gets the value of the buildConfigurationName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBuildConfigurationName() {
        return buildConfigurationName;
    }

    /**
     * Sets the value of the buildConfigurationName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBuildConfigurationName(String value) {
        this.buildConfigurationName = value;
    }

}
