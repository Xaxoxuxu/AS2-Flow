package com.as2flow.application.backend.service;

import com.as2flow.application.backend.entity.Partner;
import com.as2flow.application.backend.repository.PartnerRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PartnerService
{
    private static final Logger LOGGER = Logger.getLogger(PartnerService.class.getName());

    private final PartnerRepository partnerRepository;

    public PartnerService(PartnerRepository partnerRepository)
    {
        this.partnerRepository = partnerRepository;
    }

    public List<Partner> findAll()
    {
        return partnerRepository.findAll();
    }

    public long count()
    {
        return partnerRepository.count();
    }

    public void delete(Partner partner)
    {
        partnerRepository.delete(partner);
    }

    public void save(Partner partner)
    {
        if (partner == null)
        {
            LOGGER.log(Level.SEVERE,
                    "Partner is null. Are you sure you have connected your form to the application?");
            return;
        }
        partnerRepository.save(partner);
    }

    @PostConstruct
    public void populateTestData()
    {
        if (partnerRepository.count() == 0)
        {
            Partner testPartner = new Partner();
            testPartner.setAs2PartnerId("partner");
            testPartner.setName("partnerName");
            testPartner.setUrl("http://localhost:6666/as2");

            partnerRepository.save(testPartner);
        }
    }
}
