/*
 * This file is generated by jOOQ.
*/
package kz.tastamat.db.model.jooq.tables.records;


import java.time.OffsetDateTime;

import javax.annotation.Generated;

import kz.tastamat.db.model.jooq.tables.JqProfile;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
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
public class JqProfileRecord extends UpdatableRecordImpl<JqProfileRecord> implements Record6<Long, OffsetDateTime, OffsetDateTime, Long, Long, String> {

    private static final long serialVersionUID = -1731543055;

    /**
     * Setter for <code>public.wx_profile.id_</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.wx_profile.id_</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.wx_profile.create_date_</code>.
     */
    public void setCreateDate(OffsetDateTime value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.wx_profile.create_date_</code>.
     */
    public OffsetDateTime getCreateDate() {
        return (OffsetDateTime) get(1);
    }

    /**
     * Setter for <code>public.wx_profile.modify_date_</code>.
     */
    public void setModifyDate(OffsetDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.wx_profile.modify_date_</code>.
     */
    public OffsetDateTime getModifyDate() {
        return (OffsetDateTime) get(2);
    }

    /**
     * Setter for <code>public.wx_profile.user_</code>.
     */
    public void setUser(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.wx_profile.user_</code>.
     */
    public Long getUser() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>public.wx_profile.wallet_</code>.
     */
    public void setWallet(Long value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.wx_profile.wallet_</code>.
     */
    public Long getWallet() {
        return (Long) get(4);
    }

    /**
     * Setter for <code>public.wx_profile.template_</code>.
     */
    public void setTemplate(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.wx_profile.template_</code>.
     */
    public String getTemplate() {
        return (String) get(5);
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
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Long, OffsetDateTime, OffsetDateTime, Long, Long, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<Long, OffsetDateTime, OffsetDateTime, Long, Long, String> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return JqProfile.PROFILE.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<OffsetDateTime> field2() {
        return JqProfile.PROFILE.CREATE_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<OffsetDateTime> field3() {
        return JqProfile.PROFILE.MODIFY_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return JqProfile.PROFILE.USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field5() {
        return JqProfile.PROFILE.WALLET;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return JqProfile.PROFILE.TEMPLATE;
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
    public Long component4() {
        return getUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component5() {
        return getWallet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getTemplate();
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
    public Long value4() {
        return getUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value5() {
        return getWallet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getTemplate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqProfileRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqProfileRecord value2(OffsetDateTime value) {
        setCreateDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqProfileRecord value3(OffsetDateTime value) {
        setModifyDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqProfileRecord value4(Long value) {
        setUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqProfileRecord value5(Long value) {
        setWallet(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqProfileRecord value6(String value) {
        setTemplate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JqProfileRecord values(Long value1, OffsetDateTime value2, OffsetDateTime value3, Long value4, Long value5, String value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached JqProfileRecord
     */
    public JqProfileRecord() {
        super(JqProfile.PROFILE);
    }

    /**
     * Create a detached, initialised JqProfileRecord
     */
    public JqProfileRecord(Long id_, OffsetDateTime createDate_, OffsetDateTime modifyDate_, Long user_, Long wallet_, String template_) {
        super(JqProfile.PROFILE);

        set(0, id_);
        set(1, createDate_);
        set(2, modifyDate_);
        set(3, user_);
        set(4, wallet_);
        set(5, template_);
    }
}
