package com.as2flow.application.backend.service;

import com.as2flow.application.backend.entity.Partnership;
import com.as2flow.application.backend.repository.PartnershipRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PartnershipService
{
    private static final Logger LOGGER = Logger.getLogger(PartnershipService.class.getName());

    private final PartnershipRepository partnershipRepository;

    public PartnershipService(PartnershipRepository partnershipRepository)
    {
        this.partnershipRepository = partnershipRepository;
    }

    public enum FindBy{
        SenderAttrs, ReceiverAttrs
    }

    public List<Partnership> findAll(String stringFilter)
    {
        if (stringFilter == null || stringFilter.isEmpty())
        {
            return partnershipRepository.findAll();
        }

        return partnershipRepository.searchByName(stringFilter);
    }

    public List<Partnership> findAll(String key, String value, FindBy findBy)
    {
        if (value == null || value.isEmpty())
        {
            return partnershipRepository.findAll();
        }
        if (findBy == FindBy.SenderAttrs)
        {
            return partnershipRepository.searchBySenderAttributes(key, value);
        }
        if (findBy == FindBy.ReceiverAttrs)
        {
            return partnershipRepository.searchByReceiverAttributes(key, value);
        }

        // default
        return partnershipRepository.findAll();
    }

    public long count()
    {
        return partnershipRepository.count();
    }

    public void delete(Partnership partnership)
    {
        partnershipRepository.delete(partnership);
    }

    public void save(Partnership partnership)
    {
        if (partnership == null)
        {
            LOGGER.log(Level.SEVERE,
                    "Partnership is null. Are you sure you have connected your form to the application?");
            return;
        }
        partnershipRepository.save(partnership);
    }

    @PostConstruct
    public void populateTestData()
    {
        if (partnershipRepository.count() == 0)
        {
            //TODO
        }
    }
}