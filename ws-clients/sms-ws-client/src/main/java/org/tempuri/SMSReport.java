
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SMSReport complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SMSReport">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResultCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ResultS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RecepCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SmsCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DelivCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UndelivCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SendCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SentCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SendingCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SMSReport", propOrder = {
    "resultCode",
    "resultS",
    "recepCount",
    "smsCount",
    "delivCount",
    "undelivCount",
    "sendCount",
    "sentCount",
    "sendingCount"
})
public class SMSReport {

    @XmlElement(name = "ResultCode")
    protected int resultCode;
    @XmlElement(name = "ResultS", required = true)
    protected String resultS;
    @XmlElement(name = "RecepCount")
    protected int recepCount;
    @XmlElement(name = "SmsCount")
    protected int smsCount;
    @XmlElement(name = "DelivCount")
    protected int delivCount;
    @XmlElement(name = "UndelivCount")
    protected int undelivCount;
    @XmlElement(name = "SendCount")
    protected int sendCount;
    @XmlElement(name = "SentCount")
    protected int sentCount;
    @XmlElement(name = "SendingCount")
    protected int sendingCount;

    /**
     * Gets the value of the resultCode property.
     * 
     */
    public int getResultCode() {
        return resultCode;
    }

    /**
     * Sets the value of the resultCode property.
     * 
     */
    public void setResultCode(int value) {
        this.resultCode = value;
    }

    /**
     * Gets the value of the resultS property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultS() {
        return resultS;
    }

    /**
     * Sets the value of the resultS property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultS(String value) {
        this.resultS = value;
    }

    /**
     * Gets the value of the recepCount property.
     * 
     */
    public int getRecepCount() {
        return recepCount;
    }

    /**
     * Sets the value of the recepCount property.
     * 
     */
    public void setRecepCount(int value) {
        this.recepCount = value;
    }

    /**
     * Gets the value of the smsCount property.
     * 
     */
    public int getSmsCount() {
        return smsCount;
    }

    /**
     * Sets the value of the smsCount property.
     * 
     */
    public void setSmsCount(int value) {
        this.smsCount = value;
    }

    /**
     * Gets the value of the delivCount property.
     * 
     */
    public int getDelivCount() {
        return delivCount;
    }

    /**
     * Sets the value of the delivCount property.
     * 
     */
    public void setDelivCount(int value) {
        this.delivCount = value;
    }

    /**
     * Gets the value of the undelivCount property.
     * 
     */
    public int getUndelivCount() {
        return undelivCount;
    }

    /**
     * Sets the value of the undelivCount property.
     * 
     */
    public void setUndelivCount(int value) {
        this.undelivCount = value;
    }

    /**
     * Gets the value of the sendCount property.
     * 
     */
    public int getSendCount() {
        return sendCount;
    }

    /**
     * Sets the value of the sendCount property.
     * 
     */
    public void setSendCount(int value) {
        this.sendCount = value;
    }

    /**
     * Gets the value of the sentCount property.
     * 
     */
    public int getSentCount() {
        return sentCount;
    }

    /**
     * Sets the value of the sentCount property.
     * 
     */
    public void setSentCount(int value) {
        this.sentCount = value;
    }

    /**
     * Gets the value of the sendingCount property.
     * 
     */
    public int getSendingCount() {
        return sendingCount;
    }

    /**
     * Sets the value of the sendingCount property.
     * 
     */
    public void setSendingCount(int value) {
        this.sendingCount = value;
    }

}
