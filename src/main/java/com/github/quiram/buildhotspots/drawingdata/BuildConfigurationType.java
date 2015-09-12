
package com.github.quiram.buildhotspots.drawingdata;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BuildConfigurationType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BuildConfigurationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Dependencies">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Dependency" type="{com/github/quiram/buildhotspots/drawingdata}DependencyType" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BuildStats">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Percentage" type="{http://www.w3.org/2001/XMLSchema}byte"/>
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
@XmlType(name = "BuildConfigurationType", propOrder = { "name", "dependencies", "buildStats" })
public class BuildConfigurationType {

    @XmlElement(name = "Name", required = true)
    protected String name;
    @XmlElement(name = "Dependencies", required = true)
    protected BuildConfigurationType.Dependencies dependencies;
    @XmlElement(name = "BuildStats", required = true)
    protected BuildConfigurationType.BuildStats buildStats;

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the dependencies property.
     *
     * @return
     *     possible object is
     *     {@link BuildConfigurationType.Dependencies }
     *
     */
    public BuildConfigurationType.Dependencies getDependencies() {
        return dependencies;
    }

    /**
     * Sets the value of the dependencies property.
     *
     * @param value
     *     allowed object is
     *     {@link BuildConfigurationType.Dependencies }
     *
     */
    public void setDependencies(BuildConfigurationType.Dependencies value) {
        this.dependencies = value;
    }

    /**
     * Gets the value of the buildStats property.
     *
     * @return
     *     possible object is
     *     {@link BuildConfigurationType.BuildStats }
     *
     */
    public BuildConfigurationType.BuildStats getBuildStats() {
        return buildStats;
    }

    /**
     * Sets the value of the buildStats property.
     *
     * @param value
     *     allowed object is
     *     {@link BuildConfigurationType.BuildStats }
     *
     */
    public void setBuildStats(BuildConfigurationType.BuildStats value) {
        this.buildStats = value;
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
     *         &lt;element name="Percentage" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "percentage" })
    public static class BuildStats {

        @XmlElement(name = "Percentage")
        protected byte percentage;

        /**
         * Gets the value of the percentage property.
         *
         */
        public byte getPercentage() {
            return percentage;
        }

        /**
         * Sets the value of the percentage property.
         *
         */
        public void setPercentage(byte value) {
            this.percentage = value;
        }

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
     *         &lt;element name="Dependency" type="{com/github/quiram/buildhotspots/drawingdata}DependencyType" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "dependency" })
    public static class Dependencies {

        @XmlElement(name = "Dependency")
        protected List<DependencyType> dependency;

        /**
         * Gets the value of the dependency property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the dependency property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDependency().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DependencyType }
         *
         *
         */
        public List<DependencyType> getDependency() {
            if (dependency == null) {
                dependency = new ArrayList<DependencyType>();
            }
            return this.dependency;
        }

    }

}
