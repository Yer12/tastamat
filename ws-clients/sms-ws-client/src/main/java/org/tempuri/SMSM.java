
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SMSM complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SMSM">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="recepient" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="senderid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="msg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="msgtype" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="scheduled" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UserMsgID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="prioritet" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SMSM", propOrder = {
    "recepient",
    "senderid",
    "msg",
    "msgtype",
    "scheduled",
    "userMsgID",
    "prioritet"
})
public class SMSM {

    @XmlElement(required = true)
    protected String recepient;
    @XmlElement(required = true)
    protected String senderid;
    @XmlElement(required = true)
    protected String msg;
    protected int msgtype;
    @XmlElement(required = true)
    protected String scheduled;
    @XmlElement(name = "UserMsgID", required = true)
    protected String userMsgID;
    protected int prioritet;

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
     * Gets the value of the senderid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderid() {
        return senderid;
    }

    /**
     * Sets the value of the senderid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderid(String value) {
        this.senderid = value;
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
     * Gets the value of the msgtype property.
     * 
     */
    public int getMsgtype() {
        return msgtype;
    }

    /**
     * Sets the value of the msgtype property.
     * 
     */
    public void setMsgtype(int value) {
        this.msgtype = value;
    }

    /**
     * Gets the value of the scheduled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheduled() {
        return scheduled;
    }

    /**
     * Sets the value of the scheduled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheduled(String value) {
        this.scheduled = value;
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
     * Gets the value of the prioritet property.
     * 
     */
    public int getPrioritet() {
        return prioritet;
    }

    /**
     * Sets the value of the prioritet property.
     * 
     */
    public void setPrioritet(int value) {
        this.prioritet = value;
    }

}
