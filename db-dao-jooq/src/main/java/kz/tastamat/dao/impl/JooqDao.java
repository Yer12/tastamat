package kz.tastamat.dao.impl;

import org.jooq.DSLContext;

public class JooqDao {

	protected DSLContext ctx;

	public JooqDao(DSLContext ctx){
		this.ctx = ctx;
	}

}
