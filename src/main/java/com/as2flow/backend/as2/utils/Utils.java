package com.as2flow.backend.as2.utils;

import com.as2flow.backend.entity.PartnershipEntity;

public class Utils
{
    private Utils()
    {
    }

    public static PartnershipEntity convertPartnershipToEntity(com.helger.as2lib.partner.Partnership partnership)
    {
        return new PartnershipEntity(partnership.getAllSenderIDs(), partnership.getAllReceiverIDs(), partnership.getAllAttributes());
    }

    public static com.helger.as2lib.partner.Partnership convertEntityToPartnership(PartnershipEntity partnershipEntity)
    {
        com.helger.as2lib.partner.Partnership result = new com.helger.as2lib.partner.Partnership(partnershipEntity.getName());

        result.addAllAttributes(partnershipEntity.getAttributes());
        result.addReceiverIDs(partnershipEntity.getReceiverAttrs());
        result.addSenderIDs(partnershipEntity.getSenderAttrs());

        return result;
    }
}
