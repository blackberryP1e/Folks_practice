package ru.olgak.folks.api;

import ru.olgak.folks.api.annotation.Searchable;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/** Класс <class>Folk</class> представляет участника SQA */
@Entity
public class Folk extends AbstractEntity {

    @OneToMany(mappedBy = "folk", fetch = FetchType.EAGER)
    private List<Device> devices;

    @Searchable(search = true, filter = true, sort = true)
    private String name;

    @Searchable(search = true, filter = true)
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Searchable(search = true, filter = true)
    private String company;

    private String position;


    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

}
