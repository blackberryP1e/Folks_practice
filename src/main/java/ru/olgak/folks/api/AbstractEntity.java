package ru.olgak.folks.api;

import ru.olgak.folks.api.annotation.Searchable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Класс <class>AbstractEntity</class>
 *
 */
@MappedSuperclass
public class AbstractEntity implements Model<Long> {

    @Id
    @Searchable(search = true, filter = true, sort = true)
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
