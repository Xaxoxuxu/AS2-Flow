package com.as2flow.application.backend.service;

import com.as2flow.application.backend.entity.Identity;
import com.as2flow.application.backend.repository.IdentityRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class IdentityService
{
    private static final Logger LOGGER = Logger.getLogger(IdentityService.class.getName());
    private final IdentityRepository identityRepository;

    public IdentityService(IdentityRepository identityRepository)
    {
        this.identityRepository = identityRepository;
    }

    public List<Identity> findAll(String stringFilter)
    {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return identityRepository.findAll();
        } else {
            return identityRepository.search(stringFilter);
        }
    }

    public long count()
    {
        return identityRepository.count();
    }

    public void delete(Identity identity)
    {
        identityRepository.delete(identity);
    }

    public void save(Identity identity)
    {
        if (identity == null)
        {
            LOGGER.log(Level.SEVERE,
                    "Identity is null. Are you sure you have connected your form to the application?");
            return;
        }
        identityRepository.save(identity);
    }

    @PostConstruct
    public void populateTestData()
    {
        if (identityRepository.count() == 0)
        {

            List<Identity> identities = new ArrayList<>();
            for (int i = 1; i <= 10; i++)
            {
                identities.add(new Identity("IdentityName" + i, "IdentityId" + i));
            }

            identityRepository.saveAll(identities);
        }
    }
}
