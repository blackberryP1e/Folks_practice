package ru.olgak.folks.api;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import ru.hflabs.util.core.PartsJoiner;
import ru.olgak.folks.api.annotation.Searchable;
import ru.olgak.folks.api.model.Gender;
import ru.olgak.folks.api.model.ThreeStateStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/** Класс <class>Folk</class> представляет участника SQA */
@Getter
@Setter
@Entity
public class Folk extends AbstractEntity {

    @OneToMany(mappedBy = "folk", fetch = FetchType.EAGER)
    private List<Device> devices;

    // ФИО
    @Searchable(search = true, filter = true, sort = true)
    private String surname;
    @Searchable(search = true, filter = true, sort = true)
    private String name;
    @Searchable(search = true, filter = true, sort = true)
    private String patronymic;

    // Предпочитаемое имя
    @Searchable(search = true)
    private String preferredName;

    // Пол
    @Searchable(filter = true, sort = true)
    @Enumerated(value = EnumType.ORDINAL)
    private Gender gender;

    // Дата рождения
    @Searchable(search = true, filter = true, sort = true)
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    // Место рождения
    private String birthPlace;

    // Дата первого посещения конференции
    @Searchable(filter = true)
    @Temporal(TemporalType.DATE)
    private Date firstConfDate;

    // Компания, в которой работает участник
    @Searchable(search = true, filter = true)
    private String company;

    // Должность
    private String position;

    // Дата начала работы
    @Temporal(TemporalType.DATE)
    private Date jobStartDate;

    // Автор изменений
    @Searchable(search = true, filter = true)
    private String author;

    // Город проживания
    private String city;

    // Докладчик
    @Searchable(filter = true)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean speaker;

    // Член орг комитета или ПК
    @Searchable(filter = true)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean organizator;

    /** Признак: VIP-клиент */
    @Searchable(filter = true)
    @Enumerated(value = EnumType.ORDINAL)
    private ThreeStateStatus vipFlag = ThreeStateStatus.UNKNOWN;

    // Какой раз на конфе
    @Searchable(filter = true)
    private Integer visitCount;

    // Средний рейтинг участника
    @Searchable(filter = true)
    private BigDecimal averageRating;

    // Язык контакта с Клиентом
    private String language;

    // Дата актуальности
    @Temporal(TemporalType.DATE)
    private Date actualityDate;

    @Searchable(filter = true)
    public String getFio() {
        return new PartsJoiner(" ")
                .add(getSurname())
                .add(getName())
                .add(getPatronymic())
                .toString();
    }
}
