package ru.olgak.folks.api;

import ru.olgak.folks.api.annotation.Searchable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/** Класс <class>Device</class> описывает устройство участника */
@Entity
public class Device extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private Folk folk;
    @Searchable(search = true)
    private String model;
    @Searchable(search = true)
    private String serialNumber;

    public Folk getFolk() {
        return folk;
    }

    public void setFolk(Folk folk) {
        this.folk = folk;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
