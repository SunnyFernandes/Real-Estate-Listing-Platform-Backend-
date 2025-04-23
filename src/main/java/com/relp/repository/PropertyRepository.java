package com.relp.repository;

import com.relp.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT p FROM Property p WHERE p.title = :title AND p.user.id = :id")
    Optional<Property> findByTitleAndUser(@Param("title") String title, @Param("id") Long id);

    Optional<Property> findByTitle(String title);

    @Query("SELECT p FROM Property p " +
            "JOIN p.location l " +
            "WHERE l.city= :param OR " +
            "l.state= :param OR l.zipcode= :param OR " +
            "p.bathroom= :param OR p.bedroom= :param OR p.size= :param "
    )
    Optional<List<Property>> findByCityOrStateOrZipcode(@Param("param") String param);
}