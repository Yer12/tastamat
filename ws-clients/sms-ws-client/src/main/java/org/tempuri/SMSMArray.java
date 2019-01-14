
package org.tempuri;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SMSMArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SMSMArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SMSM" type="{http://tempuri.org/}SMSM" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SMSMArray", propOrder = {
    "smsm"
})
public class SMSMArray {

    @XmlElement(name = "SMSM")
    protected List<SMSM> smsm;

    /**
     * Gets the value of the smsm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smsm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSMSM().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SMSM }
     * 
     * 
     */
    public List<SMSM> getSMSM() {
        if (smsm == null) {
            smsm = new ArrayList<SMSM>();
        }
        return this.smsm;
    }

}
