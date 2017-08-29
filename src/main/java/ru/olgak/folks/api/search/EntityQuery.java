package ru.olgak.folks.api.search;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Класс <class>EntityQuery</class>
 *
 * @author nikolaig
 */
public class EntityQuery {

    /** Регулярное выражения для интерпретации строки в качестве запроса */
    public static final Pattern DIRECT_QUERY_PATTERN = Pattern.compile("^\\?(.*)$");


    /** Поле, по которому производится сортировка */
    private String sortOrderKey;
    /** Значение сортировки */
    private SortOrder sortOrderValue;
    /** Строка поиска */
    private String search;
    /** Карта фильтрации, где ключ - имя фильтруемого поля, значение фильтра */
    private Map<String, EntityFieldFilter<?>> filters = new HashMap<String, EntityFieldFilter<?>>();

    public String getSortOrderKey() {
        return sortOrderKey;
    }

    public void setSortOrderKey(String sortOrderKey) {
        this.sortOrderKey = sortOrderKey;
    }

    public SortOrder getSortOrderValue() {
        return sortOrderValue;
    }

    public void setSortOrderValue(SortOrder sortOrderValue) {
        this.sortOrderValue = sortOrderValue;
    }

    public void setSortOrderValue(String sortOrderKey, SortOrder sortOrderValue) {
        setSortOrderKey(sortOrderKey);
        setSortOrderValue(sortOrderValue);
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Map<String, EntityFieldFilter<?>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, EntityFieldFilter<?>> filters) {
        this.filters = filters;
    }
}
