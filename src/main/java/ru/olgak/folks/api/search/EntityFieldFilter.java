package ru.olgak.folks.api.search;

import java.util.regex.Pattern;

/**
 * Класс <class>EntityFieldFilter</class>
 *
 * @author nikolaig
 */
public abstract class EntityFieldFilter<T> {

    /** Значение фильтра */
    protected T value;

    /**
     * Возвращает строковое представление значения фильтра
     *
     * @return Возвращает строковое представление значения фильтра
     */
    public abstract String getValueAsString();


    /** Класс <code>EmptyValue</code> реализует фильтр для пустых значений */
    public static final class EmptyValue extends EntityFieldFilter<Void> {

        @Override
        public String getValueAsString() {
            return null;
        }

    }

    /** Класс <code>NotEmptyValue</code> реализует фильтр для не пустых значений */
    public static final class NotEmptyValue extends EntityFieldFilter<Void> {

        @Override
        public String getValueAsString() {
            return null;
        }
    }

    /** Класс <code>PatternValue</code> реализует фильтр для регулярного выражения */
    public static final class PatternValue extends EntityFieldFilter<String> {


        /** Зарезервированные символы регулярного выражения */
        public static final String PATTERN_SPECIAL_CHARACTERS = "^$\\|.+*?=![](){}";

        /** Зарезервированный символ для выражения ".*" */
        public static final String MATCH_ANY_CHARACTER = "%";
        /** Зарезервированный символ для выражения "." */
        public static final String MATCH_SINGLE_CHARACTER = "_";
        /** Опции регулярного выражения по умолчанию */
        public static final Integer DEFAULT_FLAGS = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;

        /** Опции регулярного выражения */
        private Integer flags = DEFAULT_FLAGS;

        /** Конструктор класса <code>PatternValue</code> */
        public PatternValue() {
        }

        /**
         * Конструктор класса <code>PatternValue</code>
         *
         * @param pattern регулярное выражение
         */
        public PatternValue(String pattern) {
            this.value = pattern;
        }


        @Override
        public String getValueAsString() {
            return getPattern();
        }

        /**
         * Возвращает значение регулярного выражения
         *
         * @return Возвращает значение регулярного выражения
         */
        public String getPattern() {
            return value;
        }

        public Integer getFlags() {
            return flags;
        }

        /**
         * Конвертирует значение в синтаксис регулярного выражения
         *
         * @param value значение
         * @return Возвращает значение в синтаксисе регулярного выражения
         */
        public static String convertToPatternSyntax(String value) {
            return value.replaceAll(MATCH_ANY_CHARACTER, ".*").replaceAll(MATCH_SINGLE_CHARACTER, ".");
        }
    }
}
