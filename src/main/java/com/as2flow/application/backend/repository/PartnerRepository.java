package com.as2flow.application.backend.repository;

import com.as2flow.application.backend.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PartnerRepository extends JpaRepository<Partner, Long>
{
    @Query("select p from Partner p " +
            "where lower(p.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(p.as2PartnerId) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(p.url) like lower(concat('%', :searchTerm, '%'))")
    List<Partner> search(@Param("searchTerm") String searchTerm);
}
