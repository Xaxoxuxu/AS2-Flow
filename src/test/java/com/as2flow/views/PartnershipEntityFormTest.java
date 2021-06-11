package com.as2flow.views;

import com.as2flow.backend.entity.PartnershipEntity;
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt;
import com.helger.as2lib.crypto.ECryptoAlgorithmSign;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicReference;

public class PartnershipEntityFormTest
{
    private PartnershipEntity partnershipEntity1;
    private PartnershipEntity partnershipEntity2;

    @BeforeClass
    public void setupData()
    {
        partnershipEntity1 = new PartnershipEntity();
        //partnership1.setName("OpenAS2A-OpenAS2B");
        partnershipEntity1.setSenderAs2Id("OpenAS2A");
        partnershipEntity1.setReceiverAs2Id("OpenAS2B");
        partnershipEntity1.setProtocol("as2");
        partnershipEntity1.setSubject("From OpenAS2A to OpenAS2B");
        //partnership1.setAS2URL("http://localhost:8081");
        //partnership1.setAS2MDNTo("http://localhost:8081");
        //partnership1.setAS2MDNOptions("signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, md5");
        partnershipEntity1.setEncryptAlgorithm(ECryptoAlgorithmCrypt.CRYPT_3DES);
        partnershipEntity1.setSigningAlgorithm(ECryptoAlgorithmSign.DIGEST_MD5);
        partnershipEntity1.setReceiverX509Alias("OpenAS2B_alias");
        partnershipEntity1.setSenderX509Alias("OpenAS2A_alias");
        //partnership1.setSenderEmail("sender@sender.com");
        //partnership1.setReceiverEmail("receiver@receiver.com");

        partnershipEntity2 = new PartnershipEntity();
        //partnership2.setName("OpenAS2B-OpenAS2A");
        partnershipEntity2.setSenderAs2Id("OpenAS2B");
        partnershipEntity2.setReceiverAs2Id("OpenAS2A");
        partnershipEntity2.setProtocol("as2");
        partnershipEntity2.setSubject("From OpenAS2B to OpenAS2A");
        partnershipEntity2.setAs2Url("http://localhost:8081");
        //partnership2.setAS2MDNTo("http://localhost:8081");
        //partnership2.setAS2MDNOptions("signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, md5");
        partnershipEntity2.setEncryptAlgorithm(ECryptoAlgorithmCrypt.CRYPT_3DES);
        partnershipEntity2.setSigningAlgorithm(ECryptoAlgorithmSign.DIGEST_MD5);
        partnershipEntity2.setReceiverX509Alias("OpenAS2A_alias");
        partnershipEntity2.setSenderX509Alias("OpenAS2B_alias");
        partnershipEntity2.setSenderEmail("sender@sender.com");
        //pSend.setReceiverEmail("receiver@receiver.com");
    }

    @Test
    public void formFieldsPopulated()
    {
        PartnershipForm partnershipForm = new PartnershipForm();
        partnershipForm.setPartnership(partnershipEntity1);

        Assert.assertEquals(partnershipForm.name.getValue(), partnershipEntity1.getName());
        Assert.assertEquals(partnershipForm.senderAs2Id.getValue(), partnershipEntity1.getSenderAs2Id());
        Assert.assertEquals(partnershipForm.receiverAs2Id.getValue(), partnershipEntity1.getReceiverAs2Id());
        Assert.assertEquals(partnershipForm.as2Url.getValue(), "");
        Assert.assertEquals(partnershipForm.subject.getValue(), partnershipEntity1.getSubject());
        Assert.assertEquals(partnershipForm.cryptoAlgorithm.getValue(), partnershipEntity1.getEncryptAlgorithm());
        Assert.assertEquals(partnershipForm.signingAlgorithm.getValue(), partnershipEntity1.getSigningAlgorithm());
        Assert.assertEquals(partnershipForm.senderX509Alias.getValue(), partnershipEntity1.getSenderX509Alias());
        Assert.assertEquals(partnershipForm.receiverX509Alias.getValue(), partnershipEntity1.getReceiverX509Alias());
        Assert.assertNull(partnershipEntity1.getSenderEmail());
        Assert.assertNull(partnershipEntity1.getReceiverEmail());
    }

    @Test
    public void saveEventHasCorrectValues()
    {
        PartnershipForm partnershipForm = new PartnershipForm();

        partnershipForm.setPartnership(partnershipEntity1);
        partnershipForm.name.setValue(partnershipEntity1.getName());
        partnershipForm.senderAs2Id.setValue(partnershipEntity1.getSenderAs2Id());
        partnershipForm.receiverAs2Id.setValue(partnershipEntity1.getReceiverAs2Id());
        partnershipForm.subject.setValue(partnershipEntity1.getSubject());
        partnershipForm.cryptoAlgorithm.setValue(partnershipEntity1.getEncryptAlgorithm());
        partnershipForm.signingAlgorithm.setValue(partnershipEntity1.getSigningAlgorithm());
        partnershipForm.senderX509Alias.setValue(partnershipEntity1.getSenderX509Alias());
        partnershipForm.receiverX509Alias.setValue(partnershipEntity1.getReceiverX509Alias());

        AtomicReference<PartnershipEntity> savedPartnershipRef = new AtomicReference<>(null);
        partnershipForm.addListener(PartnershipForm.SaveEvent.class, e ->
        {
            savedPartnershipRef.set(e.getPartnership());
        });
        partnershipForm.save.click();
        PartnershipEntity savedPartnershipEntity = savedPartnershipRef.get();

        Assert.assertEquals(savedPartnershipEntity.getName(), partnershipEntity1.getName());
        Assert.assertEquals(savedPartnershipEntity.getSenderAs2Id(), partnershipEntity1.getSenderAs2Id());
        Assert.assertEquals(savedPartnershipEntity.getReceiverAs2Id(), partnershipEntity1.getReceiverAs2Id());
        Assert.assertEquals(savedPartnershipEntity.getAs2Url(), "");
        Assert.assertEquals(savedPartnershipEntity.getSubject(), partnershipEntity1.getSubject());
        Assert.assertEquals(savedPartnershipEntity.getEncryptAlgorithm(), partnershipEntity1.getEncryptAlgorithm());
        Assert.assertEquals(savedPartnershipEntity.getSigningAlgorithm(), partnershipEntity1.getSigningAlgorithm());
        Assert.assertEquals(savedPartnershipEntity.getSenderX509Alias(), partnershipEntity1.getSenderX509Alias());
        Assert.assertEquals(savedPartnershipEntity.getReceiverX509Alias(), partnershipEntity1.getReceiverX509Alias());
        Assert.assertEquals(savedPartnershipEntity.getSenderEmail(), partnershipEntity1.getSenderEmail());
        Assert.assertEquals(savedPartnershipEntity.getReceiverEmail(), "");
    }
}