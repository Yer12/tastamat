
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SMSInbox complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SMSInbox">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResultCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Results" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sender" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="receiver" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="msg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="senttime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="receivedtime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="msgtype" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="new" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SMSInbox", propOrder = {
    "resultCode",
    "results",
    "id",
    "sender",
    "receiver",
    "msg",
    "senttime",
    "receivedtime",
    "msgtype",
    "_new"
})
public class SMSInbox {

    @XmlElement(name = "ResultCode")
    protected int resultCode;
    @XmlElement(name = "Results", required = true)
    protected String results;
    @XmlElement(required = true)
    protected String id;
    @XmlElement(required = true)
    protected String sender;
    @XmlElement(required = true)
    protected String receiver;
    @XmlElement(required = true)
    protected String msg;
    @XmlElement(required = true)
    protected String senttime;
    @XmlElement(required = true)
    protected String receivedtime;
    @XmlElement(required = true)
    protected String msgtype;
    @XmlElement(name = "new", required = true)
    protected String _new;

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
     * Gets the value of the results property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResults() {
        return results;
    }

    /**
     * Sets the value of the results property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResults(String value) {
        this.results = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the sender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the value of the sender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSender(String value) {
        this.sender = value;
    }

    /**
     * Gets the value of the receiver property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Sets the value of the receiver property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiver(String value) {
        this.receiver = value;
    }

    /**
     * Gets the value of the msg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Sets the value of the msg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsg(String value) {
        this.msg = value;
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
     * Gets the value of the msgtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsgtype() {
        return msgtype;
    }

    /**
     * Sets the value of the msgtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsgtype(String value) {
        this.msgtype = value;
    }

    /**
     * Gets the value of the new property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNew() {
        return _new;
    }

    /**
     * Sets the value of the new property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNew(String value) {
        this._new = value;
    }

}
