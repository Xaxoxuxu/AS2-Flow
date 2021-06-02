package com.as2flow.application.backend.repository;

import com.as2flow.application.backend.entity.Partnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartnershipRepository extends JpaRepository<Partnership, Long>
{
    @Query("select p from Partnership p " +
            "where lower(p.name) like lower(concat('%', :searchTerm, '%'))")
    List<Partnership> searchByName(@Param("searchTerm") String searchTerm);

    @Query("select p from Partnership p " +
            "join p.senderAttrs sa " +
            "where (KEY(sa) = :attribute " +
            "and sa = :value)")
    List<Partnership> searchBySenderAttributes(@Param("attribute") String attribute, @Param("value") String value);
}