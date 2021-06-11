package com.as2flow.backend.service;

import com.as2flow.backend.entity.AS2Message;
import com.as2flow.backend.repository.AS2MessageRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class AS2MessageService
{
    private static final Logger LOGGER = Logger.getLogger(AS2MessageService.class.getName());

    private final AS2MessageRepository as2MessageRepository;

    public AS2MessageService(AS2MessageRepository as2MessageRepository)
    {
        this.as2MessageRepository = as2MessageRepository;
    }

    public List<AS2Message> findAll(String stringFilter)
    {
        if (stringFilter == null || stringFilter.isEmpty())
        {
            return as2MessageRepository.findAll();
        }

        return as2MessageRepository.searchByName(stringFilter);
    }

    public long count()
    {
        return as2MessageRepository.count();
    }

    public void delete(AS2Message as2Message)
    {
        as2MessageRepository.delete(as2Message);
    }

    public void save(AS2Message as2Message)
    {
        if (as2Message == null)
        {
            LOGGER.log(Level.SEVERE,
                    "AS2Message is null. Are you sure you have connected your form to the application?");
            return;
        }
        as2MessageRepository.save(as2Message);
    }

    @PostConstruct
    public void populateTestData()
    {
        if (as2MessageRepository.count() == 0)
        {
            //TODO
        }
    }
}