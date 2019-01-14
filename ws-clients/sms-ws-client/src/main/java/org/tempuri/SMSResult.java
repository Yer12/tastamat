
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SMSResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SMSResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StatusCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Segments" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MsgID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ResepDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Recepient" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Lang" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UserMsgID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SMSResult", propOrder = {
    "statusCode",
    "status",
    "segments",
    "msgID",
    "resepDateTime",
    "recepient",
    "lang",
    "userMsgID"
})
public class SMSResult {

    @XmlElement(name = "StatusCode")
    protected int statusCode;
    @XmlElement(name = "Status", required = true)
    protected String status;
    @XmlElement(name = "Segments")
    protected int segments;
    @XmlElement(name = "MsgID", required = true)
    protected String msgID;
    @XmlElement(name = "ResepDateTime", required = true)
    protected String resepDateTime;
    @XmlElement(name = "Recepient", required = true)
    protected String recepient;
    @XmlElement(name = "Lang", required = true)
    protected String lang;
    @XmlElement(name = "UserMsgID", required = true)
    protected String userMsgID;

    /**
     * Gets the value of the statusCode property.
     * 
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     */
    public void setStatusCode(int value) {
        this.statusCode = value;
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

    /**
     * Gets the value of the resepDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResepDateTime() {
        return resepDateTime;
    }

    /**
     * Sets the value of the resepDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResepDateTime(String value) {
        this.resepDateTime = value;
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

}
