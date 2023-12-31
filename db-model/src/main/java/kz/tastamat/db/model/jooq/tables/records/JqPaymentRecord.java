/*
 * This file is generated by jOOQ.
*/
package kz.tastamat.db.model.jooq.tables.records;


import java.time.OffsetDateTime;

import javax.annotation.Generated;

import kz.tastamat.db.model.jooq.tables.JqPayment;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JqPaymentRecord extends UpdatableRecordImpl<JqPaymentRecord> implements Record8<Long, OffsetDateTime, OffsetDateTime, String, Long, Long, String, String> {

    private static final long serialVersionUID = 1706238985;

    /**
     * Setter for <code>public.tt_payment.id_</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.tt_payment.id_</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.tt_payment.create_date_</code>.
     */
    public void setCreateDate(OffsetDateTime value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.tt_payment.create_date_</code>.
     */
    public OffsetDateTime getCreateDate() {
        return (OffsetDateTime) get(1);
    }

    /**
     * Setter for <code>public.tt_payment.modify_date_</code>.
     */
    public void setModifyDate(OffsetDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.tt_payment.modify_date_</code>.
     */
    public OffsetDateTime getModifyDate() {
        return (OffsetDateTime) get(2);
    }

    /**
     * Setter for <code>public.tt_payment.identificator_</code>.
     */
    public void setIdentificator(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.tt_payment.identificator_</code>.
     */
    public String getIdentificator() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.tt_payment.user_</code>.
     */
    public void setUser(Long value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.tt_payment.user_</code>.
     */
    public Long getUser() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>public.tt_payment.amount_</code>.
     */
    public void setAmount(Long value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.tt_payment.amount_</code>.
     */
    public Long getAmount() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>public.tt_payment.pid_</code>.
     */
    public void setPid(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.tt_payment.pid_</code>.
     */
    public String getPid() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.tt_payment.status_</code>.
     */
    public void setStatus(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.tt_payment.status_</code>.
     */
    public String getStatus() {
        return (String) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, OffsetDateTime, OffsetDateTime, String, Long, Long, String, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row8<Long, OffsetDateTime, OffsetDateTime, String, Long, Long, String, String> valuesRow() {
        return (Row8) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return JqPayment.PAYMENT.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<OffsetDateTime> field2() {
        return JqPayment.PAYMENT.CREATE_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<OffsetDateTime> field3() {
        return JqPayment.PAYMENT.MODIFY_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return JqPayment.PAYMENT.IDENTIFICATOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return JqPayment.PAYMENT.USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field6() {
        return JqPayment.PAYMENT.AMOUNT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field7() {
        return JqPayment.PAYMENT.PID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return JqPayment.PAYMENT.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OffsetDateTime component2() {
        return getCreateDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OffsetDateTime component3() {
        return getModifyDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getIdentificator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component5() {
        return getUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component6() {
        return getAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component7() {
        return getPid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OffsetDateTime value2() {
        return getCreateDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OffsetDateTime value3() {
        return getModifyDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getIdentificator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value6() {
        return getAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value7() {
        return getPid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqPaymentRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqPaymentRecord value2(OffsetDateTime value) {
        setCreateDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqPaymentRecord value3(OffsetDateTime value) {
        setModifyDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqPaymentRecord value4(String value) {
        setIdentificator(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqPaymentRecord value5(Long value) {
        setUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqPaymentRecord value6(Long value) {
        setAmount(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqPaymentRecord value7(String value) {
        setPid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqPaymentRecord value8(String value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqPaymentRecord values(Long value1, OffsetDateTime value2, OffsetDateTime value3, String value4, Long value5, Long value6, String value7, String value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached JqPaymentRecord
     */
    public JqPaymentRecord() {
        super(JqPayment.PAYMENT);
    }

    /**
     * Create a detached, initialised JqPaymentRecord
     */
    public JqPaymentRecord(Long id_, OffsetDateTime createDate_, OffsetDateTime modifyDate_, String identificator_, Long user_, Long amount_, String pid_, String status_) {
        super(JqPayment.PAYMENT);

        set(0, id_);
        set(1, createDate_);
        set(2, modifyDate_);
        set(3, identificator_);
        set(4, user_);
        set(5, amount_);
        set(6, pid_);
        set(7, status_);
    }
}
