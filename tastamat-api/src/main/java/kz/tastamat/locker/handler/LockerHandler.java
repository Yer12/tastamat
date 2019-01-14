package kz.tastamat.locker.handler;

import io.vertx.core.*;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.core.handler.CoreHandler;
import kz.tastamat.locker.dto.LockerInfoDto;
import kz.zx.api.app.DbHandler;
import kz.zx.utils.Holder;
import kz.zx.utils.PaginatedList;

public class LockerHandler extends DbHandler {

	private Logger log = LoggerFactory.getLogger(LockerHandler.class);

	private final CoreHandler coreHandler;

	public LockerHandler(Vertx vertx) {
		super(vertx);
		this.coreHandler = new CoreHandler(vertx);
	}

	public void handleGetLockers(MultiMap params, Handler<AsyncResult<PaginatedList<LockerInfoDto>>> handler) {

		Holder<String> holder = new Holder<>();
		holder.set("?");

		params.iterator().forEachRemaining(stringStringEntry -> {
			String s = holder.get();
			if(!s.equals("?")){
				s += "&";
			}
			s += stringStringEntry.getKey()+"="+stringStringEntry.getValue();
			holder.set(s);
		});

		coreHandler.getLockers(holder.get(), rr -> {
			if(rr.succeeded()){
				PaginatedList<LockerInfoDto> list = rr.result();
				handler.handle(Future.succeededFuture(list));
			} else {
				handler.handle(Future.failedFuture(rr.cause()));
			}
		});
	}

	public void handleGetLocker(Long id, Handler<AsyncResult<LockerInfoDto>> handler) {

		coreHandler.getLocker(id, rr -> {
			if(rr.succeeded()){
				LockerInfoDto locker = rr.result();
				handler.handle(Future.succeededFuture(locker));
			} else {
				handler.handle(Future.failedFuture(rr.cause()));
			}
		});
	}
}
