
package org.tempuri;

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
 *         &lt;element name="login" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="begind" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="endd" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "login",
    "password",
    "begind",
    "endd"
})
@XmlRootElement(name = "SendSMSService___GetAbonents")
public class SendSMSServiceGetAbonents {

    @XmlElement(required = true)
    protected String login;
    @XmlElement(required = true)
    protected String password;
    @XmlElement(required = true)
    protected String begind;
    @XmlElement(required = true)
    protected String endd;

    /**
     * Gets the value of the login property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the value of the login property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogin(String value) {
        this.login = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the begind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBegind() {
        return begind;
    }

    /**
     * Sets the value of the begind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBegind(String value) {
        this.begind = value;
    }

    /**
     * Gets the value of the endd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndd() {
        return endd;
    }

    /**
     * Sets the value of the endd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndd(String value) {
        this.endd = value;
    }

}
