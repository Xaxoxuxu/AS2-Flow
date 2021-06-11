package com.as2flow.backend.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
public class AS2MessageEntity extends AbstractEntity implements Cloneable
{
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private final PartnershipEntity partnershipEntity;

    @NotNull
    private final LocalTime timeProcessed;

    public AS2MessageEntity(PartnershipEntity partnershipEntity)
    {
        this.partnershipEntity = partnershipEntity;
        this.timeProcessed = LocalTime.now();
    }

    public PartnershipEntity getPartnership()
    {
        return partnershipEntity;
    }

    public LocalTime getTimeProcessed()
    {
        return timeProcessed;
    }
}
