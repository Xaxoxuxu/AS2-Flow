package com.as2flow.application.backend.as2;

import com.as2flow.application.backend.entity.Partnership;
import com.as2flow.application.backend.service.PartnershipService;
import com.helger.as2lib.AbstractDynamicComponent;
import com.helger.as2lib.exception.AS2Exception;
import com.helger.as2lib.message.IMessage;
import com.helger.as2lib.message.IMessageMDN;
import com.helger.as2lib.params.MessageParameters;
import com.helger.as2lib.partner.AS2PartnershipNotFoundException;
import com.helger.as2lib.partner.CPartnershipIDs;
import com.helger.as2lib.partner.IPartnershipFactory;
import com.helger.commons.collection.attr.IStringMap;
import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.ICommonsSet;
import com.helger.commons.state.EChange;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class H2PartnershipFactory extends AbstractDynamicComponent implements IPartnershipFactory
{
    PartnershipService partnershipService;

    public H2PartnershipFactory(PartnershipService partnershipService)
    {
        this.partnershipService = partnershipService;
    }

    private static Partnership _toEntity(com.helger.as2lib.partner.Partnership partnership)
    {
        return new Partnership(
                partnership.getName(),
                partnership.getAllSenderIDs(),
                partnership.getAllReceiverIDs(),
                partnership.getAllAttributes());
    }

    private static com.helger.as2lib.partner.Partnership _toPartnership(Partnership partnership)
    {
        com.helger.as2lib.partner.Partnership p = new com.helger.as2lib.partner.Partnership(partnership.getName());
        p.addSenderIDs(partnership.getSenderAttrs());
        p.addReceiverIDs(partnership.getReceiverAttrs());
        p.addAllAttributes(partnership.getAttributes());

        return p;
    }

    @Nonnull
    @Override
    public EChange addPartnership(@Nonnull com.helger.as2lib.partner.Partnership aPartnership) throws AS2Exception
    {
        partnershipService.save(_toEntity(aPartnership));
        return EChange.CHANGED;
    }

    @Nonnull
    @Override
    public EChange removePartnership(@Nonnull com.helger.as2lib.partner.Partnership aPartnership) throws AS2Exception
    {
        partnershipService.delete(_toEntity(aPartnership));

        return partnershipService.findAll(aPartnership.getName()).size() == 0 ? EChange.CHANGED : EChange.UNCHANGED;
    }

    @Nonnull
    @Override
    public com.helger.as2lib.partner.Partnership getPartnership(@Nonnull com.helger.as2lib.partner.Partnership aPartnership) throws AS2Exception
    {
        com.helger.as2lib.partner.Partnership realPartnership = getPartnershipByName (aPartnership.getName ());

        if (realPartnership == null)
        {
            // Found no partnership by name
            realPartnership = getPartnershipByID (aPartnership.getAllSenderIDs (), aPartnership.getAllReceiverIDs ());
        }

        if (realPartnership == null)
        {
            throw new AS2PartnershipNotFoundException(aPartnership);
        }
        return realPartnership;
    }

    @Nullable
    @Override
    public com.helger.as2lib.partner.Partnership getPartnershipByName(@Nullable String sName)
    {
        List<Partnership> matchingPartnerShips = partnershipService.findAll(sName);

        if (matchingPartnerShips.size() == 0)
            return null;

        return _toPartnership(matchingPartnerShips.get(0));
    }

    @Nonnull
    @Override
    public ICommonsSet<String> getAllPartnershipNames()
    {
        return null;
    }

    @Nonnull
    @Override
    public ICommonsList<com.helger.as2lib.partner.Partnership> getAllPartnerships()
    {
        return partnershipService
                .findAll("")
                .stream()
                .map(H2PartnershipFactory::_toPartnership)
                .collect(Collectors.toCollection(CommonsArrayList::new));
    }

    @Override
    public void updatePartnership(@Nonnull IMessage aMsg, boolean bOverwrite) throws AS2Exception
    {
        // Fill in any available partnership information
        final com.helger.as2lib.partner.Partnership partnership = getPartnership (aMsg.partnership ());

        aMsg.partnership ().copyFrom (partnership);

        // Set attributes
        if (bOverwrite)
        {
            final String sSubject = partnership.getAttribute (CPartnershipIDs.PA_SUBJECT);
            if (sSubject != null)
            {
                aMsg.setSubject (new MessageParameters(aMsg).format (sSubject));
            }
        }
    }

    @Override
    public void updatePartnership(@Nonnull IMessageMDN aMdn, boolean bOverwrite) throws AS2Exception
    {

    }

    private com.helger.as2lib.partner.Partnership getPartnershipByID (final IStringMap allSenderIDs, final IStringMap allReceiverIDs)
    {
        for (final Map.Entry <String, String> entry : allSenderIDs.entrySet ())
        {
            List<Partnership> resultsList = partnershipService.findAll(entry.getKey(), entry.getValue(), PartnershipService.FindBy.SenderAttrs);
            if (resultsList.size() == 0) continue;
            return _toPartnership(resultsList.get(0));
        }

        return null;
    }
}
