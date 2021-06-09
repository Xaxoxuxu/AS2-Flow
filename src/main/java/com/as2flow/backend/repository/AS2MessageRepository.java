package com.as2flow.backend.repository;

import com.as2flow.backend.entity.AS2Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AS2MessageRepository extends JpaRepository<AS2Message, Long>
{
    @Query("select m from AS2Message m " +
            "where lower(m.partnership.name) like lower(concat('%', :searchTerm, '%'))")
    List<AS2Message> searchByName(@Param("searchTerm") String searchTerm);
}