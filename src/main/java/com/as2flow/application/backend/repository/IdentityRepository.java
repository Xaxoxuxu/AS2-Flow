package com.as2flow.application.backend.repository;

import com.as2flow.application.backend.entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IdentityRepository extends JpaRepository<Identity, Long>
{
    @Query("select i from Identity i " +
            "where lower(i.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(i.as2Id) like lower(concat('%', :searchTerm, '%'))")
    List<Identity> search(@Param("searchTerm") String searchTerm);
}
