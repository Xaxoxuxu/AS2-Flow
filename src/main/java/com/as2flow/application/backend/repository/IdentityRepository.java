package com.as2flow.application.backend.repository;

import com.as2flow.application.backend.entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityRepository extends JpaRepository<Identity, Long>
{

}
