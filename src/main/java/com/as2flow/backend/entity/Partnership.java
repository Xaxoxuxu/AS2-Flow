package com.as2flow.backend.entity;

import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt;
import com.helger.as2lib.crypto.ECryptoAlgorithmSign;
import com.helger.as2lib.partner.CPartnershipIDs;
import com.helger.commons.collection.attr.IStringMap;
import com.helger.commons.collection.attr.StringMap;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Partnership extends AbstractEntity implements Cloneable
{
    @NotNull
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "value")
    @MapKeyColumn(name = "attribute")
    private final Map<String, String> senderAttrs;
    @NotNull
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "value")
    @MapKeyColumn(name = "attribute")
    private final Map<String, String> receiverAttrs;
    @NotNull
    @NotEmpty
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "value")
    @MapKeyColumn(name = "attribute")
    private final Map<String, String> attributes;
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String name;

    public Partnership()
    {
        name = "";
        senderAttrs = new HashMap<>();
        receiverAttrs = new HashMap<>();
        attributes = new HashMap<>();
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
        if (senderAttrs != null)
            senderAttrs.forEach(map::put);
        return map;
    }

    public IStringMap getReceiverAttrs()
    {
        IStringMap map = new StringMap();
        if (receiverAttrs != null)
            receiverAttrs.forEach(map::put);
        return map;
    }

    public IStringMap getAttributes()
    {
        IStringMap map = new StringMap();
        if (attributes != null)
            attributes.forEach(map::put);
        return map;
    }

    private String getSenderAttribute(String attribute)
    {
        return getSenderAttrs().getValue(attribute);
    }

    private String getReceiverAttribute(String attribute)
    {
        return getReceiverAttrs().getValue(attribute);
    }

    private String getAttribute(String attribute)
    {
        return getAttributes().getValue(attribute);
    }

    public String getSenderAs2Id()
    {
        return getSenderAttribute(CPartnershipIDs.PID_AS2);
    }

    public void setSenderAs2Id(String as2Id)
    {
        senderAttrs.put(CPartnershipIDs.PID_AS2, as2Id);
    }

    public String getReceiverAs2Id()
    {
        return getReceiverAttribute(CPartnershipIDs.PID_AS2);
    }

    public void setReceiverAs2Id(String as2Id)
    {
        receiverAttrs.put(CPartnershipIDs.PID_AS2, as2Id);
    }

    public String getAs2Url()
    {
        return getAttribute(CPartnershipIDs.PA_AS2_URL);
    }

    public void setAs2Url(String url)
    {
        attributes.put(CPartnershipIDs.PA_AS2_URL, url);
    }

    public String getSubject()
    {
        return getAttribute(CPartnershipIDs.PA_SUBJECT);
    }

    public void setSubject(String subject)
    {
        attributes.put(CPartnershipIDs.PA_SUBJECT, subject);
    }

    public String getSenderX509Alias()
    {
        return getSenderAttribute(CPartnershipIDs.PID_X509_ALIAS);
    }

    public void setSenderX509Alias(String alias)
    {
        senderAttrs.put(CPartnershipIDs.PID_X509_ALIAS, alias);
    }

    public String getReceiverX509Alias()
    {
        return getReceiverAttribute(CPartnershipIDs.PID_X509_ALIAS);
    }

    public void setReceiverX509Alias(String alias)
    {
        receiverAttrs.put(CPartnershipIDs.PID_X509_ALIAS, alias);
    }

    public String getSenderEmail()
    {
        return getSenderAttribute(CPartnershipIDs.PID_EMAIL);
    }

    public void setSenderEmail(String email)
    {
        senderAttrs.put(CPartnershipIDs.PID_EMAIL, email);
    }

    public String getReceiverEmail()
    {
        return getReceiverAttribute(CPartnershipIDs.PID_EMAIL);
    }

    public void setReceiverEmail(String email)
    {
        receiverAttrs.put(CPartnershipIDs.PID_EMAIL, email);
    }

    public ECryptoAlgorithmCrypt getEncryptAlgorithm()
    {
        return ECryptoAlgorithmCrypt.getFromIDOrNull(getAttribute(CPartnershipIDs.PA_ENCRYPT));
    }

    public void setEncryptAlgorithm(String encryptAlgorithm)
    {
        attributes.put(CPartnershipIDs.PA_ENCRYPT, encryptAlgorithm);
    }

    public void setEncryptAlgorithm(ECryptoAlgorithmCrypt encryptAlgorithm)
    {
        setEncryptAlgorithm(encryptAlgorithm == null ? null : encryptAlgorithm.getID());
    }

    public ECryptoAlgorithmSign getSigningAlgorithm()
    {
        return ECryptoAlgorithmSign.getFromIDOrNull(getAttribute(CPartnershipIDs.PA_SIGN));
    }

    public void setSigningAlgorithm(String signingAlgorithm)
    {
        attributes.put(CPartnershipIDs.PA_SIGN, signingAlgorithm);
    }

    public void setSigningAlgorithm(ECryptoAlgorithmSign signingAlgorithm)
    {
        setSigningAlgorithm(signingAlgorithm == null ? null : signingAlgorithm.getID());
    }

    public String getProtocol()
    {
        return getAttribute(CPartnershipIDs.PA_PROTOCOL);
    }

    public void setProtocol(String protocol)
    {
        attributes.put(CPartnershipIDs.PA_PROTOCOL, protocol);
    }
}
