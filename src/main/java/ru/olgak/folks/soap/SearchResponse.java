
package ru.olgak.folks.soap;

import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString2;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy2;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;

import javax.annotation.Generated;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="folk" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="field" type="{http://olgak.ru/folks/v1}WField" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                   &lt;element name="attribute" type="{http://olgak.ru/folks/v1}WAttribute" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "folk"
})
@XmlRootElement(name = "searchResponse")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
public class SearchResponse implements ToString2
{

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
    protected List<SearchResponse.Folk> folk;

    /**
     * Gets the value of the folk property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the folk property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFolk().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SearchResponse.Folk }
     * 
     * 
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
    public List<SearchResponse.Folk> getFolk() {
        if (folk == null) {
            folk = new ArrayList<SearchResponse.Folk>();
        }
        return this.folk;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        final ToStringStrategy2 strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy2 strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy2 strategy) {
        {
            List<SearchResponse.Folk> theFolk;
            theFolk = (((this.folk!= null)&&(!this.folk.isEmpty()))?this.getFolk():null);
            strategy.appendField(locator, this, "folk", buffer, theFolk, ((this.folk!= null)&&(!this.folk.isEmpty())));
        }
        return buffer;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="field" type="{http://olgak.ru/folks/v1}WField" maxOccurs="unbounded" minOccurs="0"/&gt;
     *         &lt;element name="attribute" type="{http://olgak.ru/folks/v1}WAttribute" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}long" /&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "field",
        "attribute"
    })
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
    public static class Folk implements ToString2
    {

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
        protected List<WField> field;
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
        protected List<WAttribute> attribute;
        @XmlAttribute(name = "id")
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
        protected Long id;

        /**
         * Gets the value of the field property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the field property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getField().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link WField }
         * 
         * 
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
        public List<WField> getField() {
            if (field == null) {
                field = new ArrayList<WField>();
            }
            return this.field;
        }

        /**
         * Gets the value of the attribute property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the attribute property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAttribute().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link WAttribute }
         * 
         * 
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
        public List<WAttribute> getAttribute() {
            if (attribute == null) {
                attribute = new ArrayList<WAttribute>();
            }
            return this.attribute;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link Long }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
        public Long getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link Long }
         *     
         */
        @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
        public void setId(Long value) {
            this.id = value;
        }

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
        public String toString() {
            final ToStringStrategy2 strategy = JAXBToStringStrategy.INSTANCE;
            final StringBuilder buffer = new StringBuilder();
            append(null, buffer, strategy);
            return buffer.toString();
        }

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
        public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy2 strategy) {
            strategy.appendStart(locator, this, buffer);
            appendFields(locator, buffer, strategy);
            strategy.appendEnd(locator, this, buffer);
            return buffer;
        }

        @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-03-26T03:41:16+07:00", comments = "JAXB RI v2.2.11")
        public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy2 strategy) {
            {
                List<WField> theField;
                theField = (((this.field!= null)&&(!this.field.isEmpty()))?this.getField():null);
                strategy.appendField(locator, this, "field", buffer, theField, ((this.field!= null)&&(!this.field.isEmpty())));
            }
            {
                List<WAttribute> theAttribute;
                theAttribute = (((this.attribute!= null)&&(!this.attribute.isEmpty()))?this.getAttribute():null);
                strategy.appendField(locator, this, "attribute", buffer, theAttribute, ((this.attribute!= null)&&(!this.attribute.isEmpty())));
            }
            {
                Long theId;
                theId = this.getId();
                strategy.appendField(locator, this, "id", buffer, theId, (this.id!= null));
            }
            return buffer;
        }

    }

}
