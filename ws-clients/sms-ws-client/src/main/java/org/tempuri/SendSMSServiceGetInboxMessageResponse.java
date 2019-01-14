
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
 *         &lt;element name="Result" type="{http://tempuri.org/}SmsInboxArray"/>
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
    "result"
})
@XmlRootElement(name = "SendSMSService___GetInboxMessageResponse")
public class SendSMSServiceGetInboxMessageResponse {

    @XmlElement(name = "Result", required = true)
    protected SmsInboxArray result;

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link SmsInboxArray }
     *     
     */
    public SmsInboxArray getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link SmsInboxArray }
     *     
     */
    public void setResult(SmsInboxArray value) {
        this.result = value;
    }

}
