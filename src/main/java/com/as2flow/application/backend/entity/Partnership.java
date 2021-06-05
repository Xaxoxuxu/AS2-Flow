package com.as2flow.application.backend.entity;

import com.helger.as2lib.partner.CPartnershipIDs;
import com.helger.commons.collection.attr.IStringMap;
import com.helger.commons.collection.attr.StringMap;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Entity
public class Partnership extends AbstractEntity implements Cloneable
{
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String name;

    @NotNull
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "value")
    @MapKeyColumn(name = "attribute")
    private Map<String, String> senderAttrs;

    @NotNull
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "value")
    @MapKeyColumn(name = "attribute")
    private Map<String, String> receiverAttrs;

    @NotNull
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "value")
    @MapKeyColumn(name = "attribute")
    private Map<String, String> attributes;

    public Partnership()
    {
    }

    public Partnership(String name, IStringMap senderAttrs, IStringMap receiverAttrs, IStringMap attributes)
    {
        this.name = name;
        this.senderAttrs = senderAttrs;
        this.receiverAttrs = receiverAttrs;
        this.attributes = attributes;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public IStringMap getSenderAttrs()
    {
        IStringMap map = new StringMap();
        senderAttrs.forEach(map::put);
        return map;
    }

    public IStringMap getReceiverAttrs()
    {
        IStringMap map = new StringMap();
        receiverAttrs.forEach(map::put);
        return map;
    }

    public IStringMap getAttributes()
    {
        IStringMap map = new StringMap();
        attributes.forEach(map::put);
        return map;
    }

    public String getSenderAs2Id()
    {
        return getSenderAttrs().getValue(CPartnershipIDs.PID_AS2);
    }

    public void setSenderAs2Id(String as2Id)
    {
        getSenderAttrs().put(CPartnershipIDs.PID_AS2, as2Id);
    }

    public String getReceiverAs2Id()
    {
        return getReceiverAttrs().getValue(CPartnershipIDs.PID_AS2);
    }

    public void setReceiverAs2Id(String as2Id)
    {
        getReceiverAttrs().put(CPartnershipIDs.PID_AS2, as2Id);
    }

    public String getAs2Url()
    {
        return getAttributes().getValue(CPartnershipIDs.PA_AS2_URL);
    }

    public void setAs2Url(String url)
    {
        getAttributes().put(CPartnershipIDs.PA_AS2_URL, url);
    }

    public String getSubject()
    {
        return getAttributes().getValue(CPartnershipIDs.PA_SUBJECT);
    }

    public void setSubject(String subject)
    {
        getAttributes().put(CPartnershipIDs.PA_SUBJECT, subject);
    }

    public String getSenderX509Alias()
    {
        return getSenderAttrs().getValue(CPartnershipIDs.PID_X509_ALIAS);
    }

    public void setSenderX509Alias(String alias)
    {
        getSenderAttrs().put(CPartnershipIDs.PID_X509_ALIAS, alias);
    }

    public String getReceiverX509Alias()
    {
        return getReceiverAttrs().getValue(CPartnershipIDs.PID_X509_ALIAS);
    }

    public void setReceiverX509Alias(String alias)
    {
        getReceiverAttrs().put(CPartnershipIDs.PID_X509_ALIAS, alias);
    }

    public String getEmail()
    {
        return getAttributes().getValue(CPartnershipIDs.PID_EMAIL);
    }

    public void setEmail(String email)
    {
        getAttributes().put(CPartnershipIDs.PID_EMAIL, email);
    }
}
