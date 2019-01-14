
package org.tempuri;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SmsInboxArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SmsInboxArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SMSInbox" type="{http://tempuri.org/}SMSInbox" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SmsInboxArray", propOrder = {
    "smsInbox"
})
public class SmsInboxArray {

    @XmlElement(name = "SMSInbox")
    protected List<SMSInbox> smsInbox;

    /**
     * Gets the value of the smsInbox property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smsInbox property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSMSInbox().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SMSInbox }
     * 
     * 
     */
    public List<SMSInbox> getSMSInbox() {
        if (smsInbox == null) {
            smsInbox = new ArrayList<SMSInbox>();
        }
        return this.smsInbox;
    }

}
