package ru.olgak.folks.service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.olgak.folks.api.Folk;

/**
 * Класс <class>FolkRepository</class>
 *
 */
public interface FolkRepository extends JpaRepository<Folk, Long> {

}
