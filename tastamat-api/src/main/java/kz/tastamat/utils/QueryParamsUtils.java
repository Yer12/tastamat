package kz.tastamat.utils;

import io.vertx.core.MultiMap;
import kz.tastamat.db.model.params.SearchParams;

import java.util.Date;
import java.util.List;

/**
 * Created by baur on 10/22/17.
 */
public class QueryParamsUtils {

    public static SearchParams build(MultiMap queryParams) {

        SearchParams searchParams = new SearchParams();
        searchParams.status = QueryParamsUtils.asString(QueryParams.status, queryParams, null);
        searchParams.page = QueryParamsUtils.asInteger(QueryParams.page, queryParams, 0);
        searchParams.limit = QueryParamsUtils.asInteger(QueryParams.limit, queryParams, 5);
        searchParams.user = QueryParamsUtils.asLong(QueryParams.user, queryParams, null);
        searchParams.txn_id = QueryParamsUtils.asLong(QueryParams.txn_id, queryParams, null);
        searchParams.sum = QueryParamsUtils.asDouble(QueryParams.sum, queryParams, null);
        searchParams.txn_date = QueryParamsUtils.asString(QueryParams.txn_date, queryParams, null);
        searchParams.account = QueryParamsUtils.asString(QueryParams.account, queryParams, null);
        searchParams.command = QueryParamsUtils.asString(QueryParams.command, queryParams, null);

        return searchParams;
    }

    public static SearchParams vip(MultiMap queryParams) {

        SearchParams searchParams = new SearchParams();
        searchParams.phone = QueryParamsUtils.asString(QueryParams.phone, queryParams, null);
        searchParams.password = QueryParamsUtils.asString(QueryParams.password, queryParams, null);
        searchParams.sum = QueryParamsUtils.asDouble(QueryParams.sum, queryParams, null);

        return searchParams;
    }

    public static Date asDate(QueryParams key, MultiMap queryParams, Date defaultValue) {
        if (queryParams.contains(key.toString())) {
            Long millis = Long.parseLong(queryParams.getAll(key.toString()).get(0));
            return new Date(millis);
        }
        return defaultValue;
    }

    public static Integer asInteger(QueryParams key, MultiMap queryParams, Integer defaultValue) {
        return queryParams.contains(key.toString()) ? Integer.parseInt(queryParams.getAll(key.toString()).get(0)) : defaultValue;
    }

    public static String asString(QueryParams key, MultiMap queryParams, String defaultValue) {
        return queryParams.contains(key.toString()) ? queryParams.getAll(key.toString()).get(0).toString() : defaultValue;
    }

    public static Long asLong(QueryParams key, MultiMap queryParams, Long defaultValue) {
        return queryParams.contains(key.toString()) ? Long.valueOf(queryParams.getAll(key.toString()).get(0)) : defaultValue;
    }

    public static <T> List<T> asList(QueryParams key, MultiMap queryParams, List<T> defaultValue) {
        return queryParams.contains(key.toString()) ? (List<T>) queryParams.getAll(key.toString()) : defaultValue;
    }

    public static Double asDouble(QueryParams key, MultiMap queryParams, Double defaultValue) {
        return queryParams.contains(key.toString()) ? Double.valueOf(queryParams.getAll(key.toString()).get(0)) : defaultValue;
    }
}
