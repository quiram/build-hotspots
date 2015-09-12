
package com.github.quiram.buildhotspots.drawingdata;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BuildConfigurations">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="BuildConfiguration" type="{com/github/quiram/buildhotspots/drawingdata}BuildConfigurationType" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "buildConfigurations" })
@XmlRootElement(name = "Root")
public class Root {

    @XmlElement(name = "BuildConfigurations", required = true)
    protected Root.BuildConfigurations buildConfigurations;

    /**
     * Gets the value of the buildConfigurations property.
     *
     * @return
     *     possible object is
     *     {@link Root.BuildConfigurations }
     *
     */
    public Root.BuildConfigurations getBuildConfigurations() {
        return buildConfigurations;
    }

    /**
     * Sets the value of the buildConfigurations property.
     *
     * @param value
     *     allowed object is
     *     {@link Root.BuildConfigurations }
     *
     */
    public void setBuildConfigurations(Root.BuildConfigurations value) {
        this.buildConfigurations = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="BuildConfiguration" type="{com/github/quiram/buildhotspots/drawingdata}BuildConfigurationType" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "buildConfiguration" })
    public static class BuildConfigurations {

        @XmlElement(name = "BuildConfiguration")
        protected List<BuildConfigurationType> buildConfiguration;

        /**
         * Gets the value of the buildConfiguration property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the buildConfiguration property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getBuildConfiguration().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BuildConfigurationType }
         *
         *
         */
        public List<BuildConfigurationType> getBuildConfiguration() {
            if (buildConfiguration == null) {
                buildConfiguration = new ArrayList<BuildConfigurationType>();
            }
            return this.buildConfiguration;
        }

    }

}
