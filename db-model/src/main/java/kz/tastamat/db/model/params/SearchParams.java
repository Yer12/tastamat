package kz.tastamat.db.model.params;

import kz.tastamat.db.model.enums.BoxSize;
import kz.tastamat.db.model.enums.Sort;

public class SearchParams {
	public Long id;
	public String status;
	public Integer page;
	public Sort sort;
	public String sortBy;
	public String searchKey;
	public Long from;
	public Long to;
	public Long locker;
	public BoxSize size;
	public Integer limit;
	public Long user;
}
