package ru.olgak.folks.service.search;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.queryparser.flexible.precedence.PrecedenceQueryParser;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.NumberDateFormat;
import org.apache.lucene.queryparser.flexible.standard.config.NumericConfig;
import org.apache.lucene.queryparser.flexible.standard.config.StandardQueryConfigHandler;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.NumericUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.hflabs.util.core.date.DateUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Класс <class>LuceneQueryParser</class> реализует сервис разбора текстового запроса
 *
 * @author Nazin Alexander
 */
public class LuceneQueryParser {

    /** Класс логирования */
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    /** Запрос с пустым результатом */
    private static final Query EMPTY_QUERY = new BooleanQuery() {
        {
            add(new MatchAllDocsQuery(), BooleanClause.Occur.MUST_NOT);
        }
    };

    /** Анализатор индекса */
    private final Analyzer luceneAnalyzer;

    public LuceneQueryParser(Analyzer luceneAnalyzer) {
        this.luceneAnalyzer = luceneAnalyzer;
    }


    /**
     * Создает и возвращает сервис разбора запросов
     *
     * @return Возвращает сервис разбора запросов
     */
    private StandardQueryParser createQueryParser(Collection<SearchBinderFactory.SearchableField> fields) {
        final StandardQueryParser queryParser = new PrecedenceQueryParser(luceneAnalyzer);
        // Настройка операций
        queryParser.setDefaultOperator(StandardQueryConfigHandler.Operator.AND);
        // Настройка разбора
        {
            final Map<String, NumericConfig> numericConfigs = new LinkedHashMap<String, NumericConfig>();
            for (SearchBinderFactory.SearchableField field : fields) {
                NumberFormat numberFormat = null;
                FieldType.NumericType numericType = null;
                if (Integer.class.isAssignableFrom(field.getType())) {
                    numberFormat = NumberFormat.getNumberInstance();
                    numericType = FieldType.NumericType.INT;
                } else if (Long.class.isAssignableFrom(field.getType())) {
                    numberFormat = NumberFormat.getNumberInstance();
                    numericType = FieldType.NumericType.LONG;
                } else if (BigDecimal.class.isAssignableFrom(field.getType())) {
                    numberFormat = getDecimalFormat();
                    numericType = FieldType.NumericType.DOUBLE;
                } else if (Date.class.isAssignableFrom(field.getType())) {
                    numberFormat = new NumberDateFormat(new SimpleDateFormat(DateUtil.DATE_PATTERN));
                    numericType = FieldType.NumericType.LONG;
                }
                if (numberFormat != null) {
                    numericConfigs.put(
                            field.getName(),
                            new NumericConfig(NumericUtils.PRECISION_STEP_DEFAULT, numberFormat, numericType)
                    );
                }
            }
            queryParser.setNumericConfigMap(numericConfigs);
        }
        // Возвращаем настроенный сервис
        return queryParser;
    }

    private static NumberFormat getDecimalFormat() {
        // Точка в качестве разделителя дробной части
        DecimalFormat format = new DecimalFormat();
        DecimalFormatSymbols custom = new DecimalFormatSymbols();
        custom.setDecimalSeparator('.');
        format.setDecimalFormatSymbols(custom);
        return format;
    }

    private Query doParseQuery(String query, StandardQueryParser parser) throws Exception {
        // Выполняем разбор запроса
        return parser.parse(query, SearchBinderTemplate.DEFAULT_SEARCH_FIELD);
    }

    /**
     * Выполняет разбор текстового запроса
     *
     * @param query запрос
     * @param fields коллекция полей поиска
     * @return Возвращает разобранный запрос
     */
    public Query parseQuery(String query, Collection<SearchBinderFactory.SearchableField> fields) {
        try {
            return doParseQuery(query, createQueryParser(fields));
        } catch (Exception ex) {
            LOG.debug(ex.getMessage());
            return EMPTY_QUERY;
        }
    }
}
