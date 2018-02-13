package ru.olgak.folks.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Аннотация <class>Searchable</class> помечает поля, по которым возможен поикс
 *
 */
@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface Searchable {

    /** Участвует в полнотекстовом поиске */
    boolean search() default false;

    /** Возможна фильтрация и расширенный поиск */
    boolean filter() default false;

    /** Возможна сортировка */
    boolean sort() default false;
}
