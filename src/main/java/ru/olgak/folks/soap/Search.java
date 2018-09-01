
package ru.olgak.folks.soap;

import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString2;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy2;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;

import javax.annotation.Generated;
import javax.xml.bind.annotation.*;


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
 *         &lt;element name="query" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="include" type="{http://olgak.ru/folks/v1}WInclude" minOccurs="0"/&gt;
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
    "query",
    "include"
})
@XmlRootElement(name = "search")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2018-09-01T09:15:45+03:00", comments = "JAXB RI v2.2.11")
public class Search implements ToString2
{

    @XmlElement(required = true)
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-09-01T09:15:45+03:00", comments = "JAXB RI v2.2.11")
    protected String query;
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-09-01T09:15:45+03:00", comments = "JAXB RI v2.2.11")
    protected WInclude include;

    /**
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-09-01T09:15:45+03:00", comments = "JAXB RI v2.2.11")
    public String getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-09-01T09:15:45+03:00", comments = "JAXB RI v2.2.11")
    public void setQuery(String value) {
        this.query = value;
    }

    /**
     * Gets the value of the include property.
     * 
     * @return
     *     possible object is
     *     {@link WInclude }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-09-01T09:15:45+03:00", comments = "JAXB RI v2.2.11")
    public WInclude getInclude() {
        return include;
    }

    /**
     * Sets the value of the include property.
     * 
     * @param value
     *     allowed object is
     *     {@link WInclude }
     *     
     */
    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-09-01T09:15:45+03:00", comments = "JAXB RI v2.2.11")
    public void setInclude(WInclude value) {
        this.include = value;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-09-01T09:15:45+03:00", comments = "JAXB RI v2.2.11")
    public String toString() {
        final ToStringStrategy2 strategy = JAXBToStringStrategy.INSTANCE2;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-09-01T09:15:45+03:00", comments = "JAXB RI v2.2.11")
    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy2 strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    @Generated(value = "com.sun.tools.xjc.Driver", date = "2018-09-01T09:15:45+03:00", comments = "JAXB RI v2.2.11")
    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy2 strategy) {
        {
            String theQuery;
            theQuery = this.getQuery();
            strategy.appendField(locator, this, "query", buffer, theQuery, (this.query!= null));
        }
        {
            WInclude theInclude;
            theInclude = this.getInclude();
            strategy.appendField(locator, this, "include", buffer, theInclude, (this.include!= null));
        }
        return buffer;
    }

}
