package com.as2flow.application.as2;

import com.helger.as2lib.crypto.ECryptoAlgorithmSign;
import com.helger.as2lib.exception.AS2Exception;
import com.helger.as2lib.partner.AbstractPartnershipFactory;
import com.helger.as2lib.partner.Partnership;
import com.helger.commons.annotation.OverrideOnDemand;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

public class PartnershipFactory extends AbstractPartnershipFactory
{
  /**
   * Ensure that a new partnership is usable. It ensure the following properties
   * present:
   * <ul>
   * <li>Sender X509 alias</li>
   * <li>Receiver X509 alias</li>
   * <li>Unique name</li>
   * <li>Fallback signing algorithm SHA_1</li>
   * </ul>
   *
   * @param aPartnership
   *        The partnership to be used.
   */
  public static void ensureUsablePartnership (@Nonnull final Partnership aPartnership)
  {
    // Ensure the X509 key is contained for certificate store alias retrieval
    if (!aPartnership.containsSenderX509Alias ())
      aPartnership.setSenderX509Alias (aPartnership.getSenderAS2ID ());

    if (!aPartnership.containsReceiverX509Alias ())
      aPartnership.setReceiverX509Alias (aPartnership.getReceiverAS2ID ());

    // Ensure a unique name
    if (Partnership.DEFAULT_NAME.equals (aPartnership.getName ()))
      aPartnership.setName (aPartnership.getSenderAS2ID () + "-" + aPartnership.getReceiverAS2ID ());

    // Ensure a signing algorithm is present in the partnership. This is
    // relevant for MIC calculation, so that the headers are included
    // The algorithm itself does not really matter as for sending the algorithm
    // is specified anyway and for the MIC it is specified explicitly
    if (aPartnership.getSigningAlgorithm () == null)
      aPartnership.setSigningAlgorithm (ECryptoAlgorithmSign.DIGEST_SHA_1);
  }

  /**
   * Callback method that is invoked every time a new partnership is
   * automatically added. This method is called BEFORE the main add-process is
   * started.
   *
   * @param aPartnership
   *        The partnership that will be added. Never <code>null</code>.
   * @throws AS2Exception
   *         In case of an error.
   */
  @OverrideOnDemand
  @OverridingMethodsMustInvokeSuper
  protected void onBeforeAddPartnership (@Nonnull final Partnership aPartnership) throws AS2Exception
  {
    ensureUsablePartnership (aPartnership);
  }

}
