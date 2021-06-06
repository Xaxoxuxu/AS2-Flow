package com.as2flow.views;

import com.as2flow.backend.entity.Partnership;
import com.helger.as2lib.crypto.ECryptoAlgorithmCrypt;
import com.helger.as2lib.crypto.ECryptoAlgorithmSign;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicReference;

public class PartnershipFormTest
{
    private Partnership partnership1;
    private Partnership partnership2;

    @BeforeClass
    public void setupData()
    {
        partnership1 = new Partnership();
        partnership1.setName("OpenAS2A-OpenAS2B");
        partnership1.setSenderAs2Id("OpenAS2A");
        partnership1.setReceiverAs2Id("OpenAS2B");
        partnership1.setProtocol("as2");
        partnership1.setSubject("From OpenAS2A to OpenAS2B");
        //partnership1.setAS2URL("http://localhost:8081");
        //partnership1.setAS2MDNTo("http://localhost:8081");
        //partnership1.setAS2MDNOptions("signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, md5");
        partnership1.setEncryptAlgorithm(ECryptoAlgorithmCrypt.CRYPT_3DES);
        partnership1.setSigningAlgorithm(ECryptoAlgorithmSign.DIGEST_MD5);
        partnership1.setReceiverX509Alias("OpenAS2B_alias");
        partnership1.setSenderX509Alias("OpenAS2A_alias");
        //partnership1.setSenderEmail("sender@sender.com");
        //partnership1.setReceiverEmail("receiver@receiver.com");

        partnership2 = new Partnership();
        partnership2.setName("OpenAS2B-OpenAS2A");
        partnership2.setSenderAs2Id("OpenAS2B");
        partnership2.setReceiverAs2Id("OpenAS2A");
        partnership2.setProtocol("as2");
        partnership2.setSubject("From OpenAS2B to OpenAS2A");
        partnership2.setAs2Url("http://localhost:8081");
        //partnership2.setAS2MDNTo("http://localhost:8081");
        //partnership2.setAS2MDNOptions("signed-receipt-protocol=optional, pkcs7-signature; signed-receipt-micalg=optional, md5");
        partnership2.setEncryptAlgorithm(ECryptoAlgorithmCrypt.CRYPT_3DES);
        partnership2.setSigningAlgorithm(ECryptoAlgorithmSign.DIGEST_MD5);
        partnership2.setReceiverX509Alias("OpenAS2A_alias");
        partnership2.setSenderX509Alias("OpenAS2B_alias");
        partnership2.setSenderEmail("sender@sender.com");
        //pSend.setReceiverEmail("receiver@receiver.com");
    }

    @Test
    public void formFieldsPopulated()
    {
        PartnershipForm partnershipForm = new PartnershipForm();
        partnershipForm.setPartnership(partnership1);

        Assert.assertEquals(partnershipForm.name.getValue(), partnership1.getName());
        Assert.assertEquals(partnershipForm.senderAs2Id.getValue(), partnership1.getSenderAs2Id());
        Assert.assertEquals(partnershipForm.receiverAs2Id.getValue(), partnership1.getReceiverAs2Id());
        Assert.assertEquals(partnershipForm.as2Url.getValue(), "");
        Assert.assertEquals(partnershipForm.subject.getValue(), partnership1.getSubject());
        Assert.assertEquals(partnershipForm.cryptoAlgorithm.getValue(), partnership1.getEncryptAlgorithm());
        Assert.assertEquals(partnershipForm.signingAlgorithm.getValue(), partnership1.getSigningAlgorithm());
        Assert.assertEquals(partnershipForm.senderX509Alias.getValue(), partnership1.getSenderX509Alias());
        Assert.assertEquals(partnershipForm.receiverX509Alias.getValue(), partnership1.getReceiverX509Alias());
        Assert.assertNull(partnership1.getSenderEmail());
        Assert.assertNull(partnership1.getReceiverEmail());
    }

    @Test
    public void saveEventHasCorrectValues()
    {
        PartnershipForm partnershipForm = new PartnershipForm();

        partnershipForm.setPartnership(partnership1);
        partnershipForm.name.setValue(partnership1.getName());
        partnershipForm.senderAs2Id.setValue(partnership1.getSenderAs2Id());
        partnershipForm.receiverAs2Id.setValue(partnership1.getReceiverAs2Id());
        partnershipForm.subject.setValue(partnership1.getSubject());
        partnershipForm.cryptoAlgorithm.setValue(partnership1.getEncryptAlgorithm());
        partnershipForm.signingAlgorithm.setValue(partnership1.getSigningAlgorithm());
        partnershipForm.senderX509Alias.setValue(partnership1.getSenderX509Alias());
        partnershipForm.receiverX509Alias.setValue(partnership1.getReceiverX509Alias());

        AtomicReference<Partnership> savedPartnershipRef = new AtomicReference<>(null);
        partnershipForm.addListener(PartnershipForm.SaveEvent.class, e ->
        {
            savedPartnershipRef.set(e.getPartnership());
        });
        partnershipForm.save.click();
        Partnership savedPartnership = savedPartnershipRef.get();

        Assert.assertEquals(savedPartnership.getName(), partnership1.getName());
        Assert.assertEquals(savedPartnership.getSenderAs2Id(), partnership1.getSenderAs2Id());
        Assert.assertEquals(savedPartnership.getReceiverAs2Id(), partnership1.getReceiverAs2Id());
        Assert.assertEquals(savedPartnership.getAs2Url(), "");
        Assert.assertEquals(savedPartnership.getSubject(), partnership1.getSubject());
        Assert.assertEquals(savedPartnership.getEncryptAlgorithm(), partnership1.getEncryptAlgorithm());
        Assert.assertEquals(savedPartnership.getSigningAlgorithm(), partnership1.getSigningAlgorithm());
        Assert.assertEquals(savedPartnership.getSenderX509Alias(), partnership1.getSenderX509Alias());
        Assert.assertEquals(savedPartnership.getReceiverX509Alias(), partnership1.getReceiverX509Alias());
        Assert.assertEquals(savedPartnership.getSenderEmail(), partnership1.getSenderEmail());
        Assert.assertEquals(savedPartnership.getReceiverEmail(), "");
    }
}