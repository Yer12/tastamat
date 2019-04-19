package kz.tastamat.dao.impl;

import kz.tastamat.dao.ProfileDao;
import kz.tastamat.dao.UserDao;
import kz.tastamat.db.model.dto.ProfileDto;
import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.db.model.jooq.Sequences;
import kz.tastamat.db.model.jooq.tables.JqProfile;
import kz.tastamat.db.model.jooq.tables.JqUser;
import kz.tastamat.db.model.jooq.tables.records.JqProfileRecord;
import kz.tastamat.db.model.jooq.tables.records.JqUserRecord;
import org.apache.commons.codec.digest.DigestUtils;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by baur on 10/18/17.
 */
public class ProfileDaoImpl extends JooqDao implements ProfileDao {

	private JqProfile p = JqProfile.PROFILE;

	public ProfileDaoImpl(DSLContext ctx) {
		super(ctx);
	}

	@Override
	public Optional<ProfileDto> findById(Long id) {
		return ctx.selectFrom(p).where(p.ID.eq(id)).fetchOptional(ProfileDto::build);
	}

	@Override
	public Optional<ProfileDto> findByUser(Long userId) {
		return ctx.selectFrom(p).where(p.USER.eq(userId)).limit(1).fetchOptional(ProfileDto::build);
	}

	@Override
	public ProfileDto create(Long userId, Long wallet, String template) {
		JqProfileRecord record = ctx.newRecord(p);
		record.setId(ctx.nextval(Sequences.PROFILE_SEQUENCE));

		record.setUser(userId);
		record.setWallet(wallet);
		record.setTemplate(template);

		InsertQuery<JqProfileRecord> query = ctx.insertQuery(p);
		query.addRecord(record);
		query.setReturning();
		query.execute();

		return ProfileDto.build(query.getReturnedRecord());
	}

	@Override
	public int template(Long id, String template) {
		return ctx.update(p).set(p.TEMPLATE, template).where(p.ID.eq(id)).execute();
	}

	@Override
	public int wallet(Long id, Long wallet) {
		return ctx.update(p).set(p.WALLET, wallet).where(p.ID.eq(id)).execute();
	}
}
