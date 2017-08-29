package ru.olgak.folks.service.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.springframework.util.StringUtils;
import ru.hflabs.util.core.FormatUtil;
import ru.hflabs.util.lucene.LuceneBinderTransformer;
import ru.olgak.folks.api.Model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static ru.hflabs.util.lucene.LuceneUtil.valueToByte;

/**
 * Класс <class>SearchBinderTemplate</class>
 *
 * @author nikolaig
 */
public abstract class SearchBinderTemplate<E extends Model> implements LuceneBinderTransformer<E, Serializable> {

    public static final String ID_FIELD_NAME = "id";

    @Override
    public Serializable getPrimaryKey(Document document) {
        return FormatUtil.parseLongQuietly(document.get(ID_FIELD_NAME));
    }

    @Override
    public Term getPrimaryKey(E e) {
        return new Term("%s", valueToByte(e.getId()));
    }

    /**
         * Создает поле поиска поумолчанию
         *
         * @param fields текущие поля поиска
         * @return Возвращает поле поиска поумолчанию
         */
        protected static IndexableField createDefaultSearchField(List<IndexableField> fields) {
            Set<String> defaultValues = new LinkedHashSet<String>(fields.size());
            for (IndexableField fieldable : fields) {
                String value = fieldable.stringValue();
                if (value != null && !value.isEmpty()) {
                    String[] splittedValues = value.replaceAll("\\s+", " ").split(" ");
                    for (String splittedValue : splittedValues) {
                        splittedValue = splittedValue.trim();
                        if (!splittedValue.isEmpty()) {
                            defaultValues.add(splittedValue);
                        }
                    }
                }
            }
            return new TextField(DEFAULT_SEARCH_FIELD, StringUtils.collectionToDelimitedString(defaultValues, " "), Field.Store.NO);
        }
}
