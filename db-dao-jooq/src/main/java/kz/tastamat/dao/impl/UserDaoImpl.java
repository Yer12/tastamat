package kz.tastamat.dao.impl;

import kz.tastamat.dao.UserDao;
import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.db.model.enums.Role;
import kz.tastamat.db.model.jooq.Sequences;
import kz.tastamat.db.model.jooq.tables.JqUser;
import kz.tastamat.db.model.jooq.tables.records.JqUserRecord;
import org.apache.commons.codec.digest.DigestUtils;
import org.jooq.*;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by baur on 10/18/17.
 */
public class UserDaoImpl extends JooqDao implements UserDao {

	private JqUser u = JqUser.USER;

	public UserDaoImpl(DSLContext ctx) {
		super(ctx);
	}


	@Override
	public Optional<UserDto> findById(Long id) {
		return ctx.selectFrom(u).where(u.ID.eq(id)).fetchOptional(UserDto::build);
	}

	@Override
	public Optional<UserDto> findByPhone(String phone) {
		return ctx.selectFrom(u).where(u.PHONE.eq(phone)).fetchOptional(UserDto::build);
	}

	@Override
	public Optional<UserDto> findEnabledByPhone(String phone) {
		return ctx.selectFrom(u)
				.where(u.PHONE.eq(phone))
				.and(u.ENABLED.eq(true))
				.fetchOptional(UserDto::build);
	}

	@Override
	public Optional<UserDto> findDisabledByPhone(String phone) {
		return ctx.selectFrom(u)
				.where(u.PHONE.eq(phone))
				.and(u.ENABLED.eq(false))
				.fetchOptional(UserDto::build);
	}

	@Override
	public Optional<UserDto> findByPhonePassword(String login, String password) {
		String pass = DigestUtils.md5Hex(password);
		return ctx.selectFrom(u)
				.where(u.PHONE.eq(login))
				.and(u.PASSWORD.eq(pass))
				.and(u.ENABLED.eq(true))
				.fetchOptional(UserDto::build);
	}

//	@Override
//	public Optional<UserDto> findByEmail(String email) {
//		return ctx.selectFrom(u)
//			.where(u.EMAIL.eq(email))
//			.fetchOptional(UserDto::build);
//	}

//	@Override
//	public boolean hasRole(String email, Role role) {
//		Integer result = ctx.selectCount()
//			.from(u)
//			.where(u.EMAIL.eq(email))
//				.and(u.ROLE.eq(role.name()))
//			.fetchOne().value1();
//		return result > 0;
//	}

	@Override
	public UserDto create(String phone, String smsCode) {
		JqUserRecord record = ctx.newRecord(u);
		record.setId(ctx.nextval(Sequences.USER_SEQUENCE));

		record.setPhone(phone);
		record.setSmsCode(smsCode);

		InsertQuery<JqUserRecord> query = ctx.insertQuery(u);
		query.addRecord(record);
		query.setReturning();
		query.execute();

		return UserDto.build(query.getReturnedRecord());
	}

	@Override
	public UserDto clean(Long id, String smsCode) {
		Objects.requireNonNull(id);

		JqUserRecord record = ctx.newRecord(u);
		record.setId(id);
		record.setSmsCode(smsCode);
		record.setConfirmed(false);
		record.update();

		return UserDto.build(record);
	}

	@Override
	public UserDto confirm(Long id) {
		Objects.requireNonNull(id);

		JqUserRecord record = ctx.newRecord(u);
		record.setId(id);
		record.setConfirmed(true);
		record.update();

		return UserDto.build(record);
	}

	@Override
	public UserDto enable(Long id, String password) {
		Objects.requireNonNull(id);

		JqUserRecord record = ctx.newRecord(u);
		record.setId(id);
		record.setPassword(password);
		record.setEnabled(true);
		record.update();

		return UserDto.build(record);
	}
}
