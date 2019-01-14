
package org.tempuri;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IDSMSArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IDSMSArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IDSMS" type="{http://tempuri.org/}IDSMS" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IDSMSArray", propOrder = {
    "idsms"
})
public class IDSMSArray {

    @XmlElement(name = "IDSMS")
    protected List<IDSMS> idsms;

    /**
     * Gets the value of the idsms property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idsms property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIDSMS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IDSMS }
     * 
     * 
     */
    public List<IDSMS> getIDSMS() {
        if (idsms == null) {
            idsms = new ArrayList<IDSMS>();
        }
        return this.idsms;
    }

}
