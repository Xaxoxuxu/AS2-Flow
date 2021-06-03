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
            "where (lower(KEY(sa)) = lower(:attribute) " +
            "and lower(sa) like lower(concat('%', :value, '%')))")
    List<Partnership> searchBySenderAttributes(@Param("attribute") String attribute, @Param("value") String value);

    @Query("select p from Partnership p " +
            "join p.receiverAttrs ra " +
            "where (lower(KEY(ra)) = lower(:attribute) " +
            "and lower(ra) like lower(concat('%', :value, '%')))")
    List<Partnership> searchByReceiverAttributes(@Param("attribute") String attribute, @Param("value") String value);
}