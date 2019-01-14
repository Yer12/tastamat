
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SMSInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SMSInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resultCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="resultS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="recepient" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="senderId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="senttime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="receivedtime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="segments" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="lang" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userMsgID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="msgID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SMSInfo", propOrder = {
    "resultCode",
    "resultS",
    "recepient",
    "status",
    "senderId",
    "senttime",
    "receivedtime",
    "segments",
    "lang",
    "userMsgID",
    "msgID"
})
public class SMSInfo {

    protected int resultCode;
    @XmlElement(required = true)
    protected String resultS;
    @XmlElement(required = true)
    protected String recepient;
    @XmlElement(required = true)
    protected String status;
    @XmlElement(required = true)
    protected String senderId;
    @XmlElement(required = true)
    protected String senttime;
    @XmlElement(required = true)
    protected String receivedtime;
    protected int segments;
    @XmlElement(required = true)
    protected String lang;
    @XmlElement(required = true)
    protected String userMsgID;
    @XmlElement(required = true)
    protected String msgID;

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
     * Gets the value of the recepient property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecepient() {
        return recepient;
    }

    /**
     * Sets the value of the recepient property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecepient(String value) {
        this.recepient = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the senderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * Sets the value of the senderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderId(String value) {
        this.senderId = value;
    }

    /**
     * Gets the value of the senttime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenttime() {
        return senttime;
    }

    /**
     * Sets the value of the senttime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenttime(String value) {
        this.senttime = value;
    }

    /**
     * Gets the value of the receivedtime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceivedtime() {
        return receivedtime;
    }

    /**
     * Sets the value of the receivedtime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceivedtime(String value) {
        this.receivedtime = value;
    }

    /**
     * Gets the value of the segments property.
     * 
     */
    public int getSegments() {
        return segments;
    }

    /**
     * Sets the value of the segments property.
     * 
     */
    public void setSegments(int value) {
        this.segments = value;
    }

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

    /**
     * Gets the value of the userMsgID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserMsgID() {
        return userMsgID;
    }

    /**
     * Sets the value of the userMsgID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserMsgID(String value) {
        this.userMsgID = value;
    }

    /**
     * Gets the value of the msgID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgID() {
        return msgID;
    }

    /**
     * Sets the value of the msgID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgID(String value) {
        this.msgID = value;
    }

}
