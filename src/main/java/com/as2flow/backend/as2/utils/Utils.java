package com.as2flow.backend.as2.utils;

import com.as2flow.backend.entity.Partnership;

public class Utils
{
    private Utils()
    {
    }

    public static Partnership convertPartnershipToEntity(com.helger.as2lib.partner.Partnership partnership)
    {
        Partnership result = new Partnership();

        result.setName(partnership.getName());
        result.setAttributes(partnership.getAllAttributes());
        result.setReceiverAttrs(partnership.getAllReceiverIDs());
        result.setSenderAttrs(partnership.getAllSenderIDs());

        return result;
    }

    public static com.helger.as2lib.partner.Partnership convertEntityToPartnership(Partnership partnership)
    {
        com.helger.as2lib.partner.Partnership result = new com.helger.as2lib.partner.Partnership(partnership.getName());

        result.addAllAttributes(partnership.getAttributes());
        result.addReceiverIDs(partnership.getReceiverAttrs());
        result.addSenderIDs(partnership.getSenderAttrs());

        return result;
    }
}
