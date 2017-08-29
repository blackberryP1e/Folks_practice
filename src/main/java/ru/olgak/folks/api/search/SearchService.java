package ru.olgak.folks.api.search;

import ru.olgak.folks.api.AbstractEntity;
import ru.olgak.folks.api.Model;

import java.io.IOException;
import java.util.List;

/**
 * Класс <class>SearchService</class> декларирует методы поиска сущностей
 *
 * @author nikolaig
 */
public interface SearchService<E extends AbstractEntity> {

    void rebuild() throws IOException;

    List<E> findEntities(EntityQuery entityQuery);
}
