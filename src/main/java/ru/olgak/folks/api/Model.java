package ru.olgak.folks.api;

import java.io.Serializable;

/**
 * Интерфейс <class>Model</class>
 *
 * @author nikolaig
 */
public interface Model<ID extends Serializable> extends Serializable {

    ID getId();
}
