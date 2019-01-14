package kz.zx.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deedarb on 10/4/17.
 */
public class PaginatedList<E> extends ArrayList<E> implements List<E> {

	private static final long serialVersionUID = 9160105579432115661L;
	private Long count = 0L;    //общее кол-во записей

	public PaginatedList() {
	}

	public PaginatedList(List<E> list, Long count) {
		this.addAll(list);
		this.count = count;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
}
