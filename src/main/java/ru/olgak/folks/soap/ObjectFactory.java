
package ru.olgak.folks.soap;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ru.olgak.folks.soap package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ru.olgak.folks.soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SearchResponse }
     * 
     */
    public SearchResponse createSearchResponse() {
        return new SearchResponse();
    }

    /**
     * Create an instance of {@link Search }
     * 
     */
    public Search createSearch() {
        return new Search();
    }

    /**
     * Create an instance of {@link WInclude }
     * 
     */
    public WInclude createWInclude() {
        return new WInclude();
    }

    /**
     * Create an instance of {@link SearchResponse.Folk }
     * 
     */
    public SearchResponse.Folk createSearchResponseFolk() {
        return new SearchResponse.Folk();
    }

    /**
     * Create an instance of {@link WAttribute }
     * 
     */
    public WAttribute createWAttribute() {
        return new WAttribute();
    }

    /**
     * Create an instance of {@link WField }
     * 
     */
    public WField createWField() {
        return new WField();
    }

}
