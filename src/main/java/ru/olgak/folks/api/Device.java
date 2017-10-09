package ru.olgak.folks.api;

import ru.olgak.folks.api.annotation.Searchable;

import lombok.Getter;
import lombok.Setter;
import ru.olgak.folks.api.model.DeviceOS;
import ru.olgak.folks.api.model.DeviceType;

import javax.persistence.*;

/** Класс <class>Device</class> описывает устройство участника */
@Getter
@Setter
@Entity
public class Device extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private Folk folk;

    // Тип устройства
    @Searchable(filter = true)
    @Enumerated(value = EnumType.STRING)
    private DeviceType type = DeviceType.UNKNOWN;

    // Операционная система
    @Searchable(search = true)
    @Enumerated(value = EnumType.STRING)
    private DeviceOS os = DeviceOS.UNKNOWN;

    // Модель
    @Searchable(search = true, filter = true)
    private String model;

    // Серийный номер
    private String serialNumber;

    // Автор изменений
    @Searchable(search = true, filter = true)
    private String author;

}
