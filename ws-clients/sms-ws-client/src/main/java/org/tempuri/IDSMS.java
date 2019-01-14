
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IDSMS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IDSMS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MsgID" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "IDSMS", propOrder = {
    "msgID",
    "userMsgID"
})
public class IDSMS {

    @XmlElement(name = "MsgID", required = true)
    protected String msgID;
    @XmlElement(name = "UserMsgID", required = true)
    protected String userMsgID;

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
