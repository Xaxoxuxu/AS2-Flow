package com.as2flow.application.backend.service;

import com.as2flow.application.backend.entity.Identity;
import com.as2flow.application.backend.entity.Partner;
import com.as2flow.application.backend.entity.Partnership;
import com.as2flow.application.backend.repository.IdentityRepository;
import com.as2flow.application.backend.repository.PartnerRepository;
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
    private final IdentityRepository identityRepository;
    private final PartnerRepository partnerRepository;

    public PartnershipService(PartnershipRepository partnershipRepository, IdentityRepository identityRepository, PartnerRepository partnerRepository)
    {
        this.partnershipRepository = partnershipRepository;
        this.identityRepository = identityRepository;
        this.partnerRepository = partnerRepository;
    }

    public enum FindBy{
        SenderAttrs
    }

    public List<Partnership> findAll(String stringFilter)
    {
        if (stringFilter == null || stringFilter.isEmpty())
        {
            return partnershipRepository.findAll();
        } else
        {
            return partnershipRepository.searchByName(stringFilter);
        }
    }

    public List<Partnership> findAll(String key, String value, FindBy findBy)
    {
        if (findBy == FindBy.SenderAttrs)
        {
            return partnershipRepository.searchBySenderAttributes(key, value);
        }

        // default
        return partnershipRepository.searchBySenderAttributes(key, value);
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
        if (partnerRepository.count() == 0)
        {
            Partner p = new Partner("OpenAS2A", "OpenAS2A", "OpenAS2A_alias", "http://localhost:8080/as2");
            partnerRepository.save(p);
        }

        if (identityRepository.count() == 0)
        {
            Identity identity = new Identity("OpenAS2B", "OpenAS2B", "OpenAS2B_alias");
            identityRepository.save(identity);
        }

        if (partnershipRepository.count() == 0)
        {
            /*Identity identity = identityRepository.findAll().get(0);
            Partner partner = partnerRepository.findAll().get(0);

            PartnershipEntity p = new PartnershipEntity(
                    "OpenAS2A-OpenAS2B",
                    identity,
                    partner,
                    "From OpenAS2A to OpenAS2B",
                    "signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, md5",
                    ECryptoAlgorithmCrypt.CRYPT_3DES,
                    ECryptoAlgorithmSign.DIGEST_MD5);

            partnershipRepository.save(p);*/


        }
    }
}