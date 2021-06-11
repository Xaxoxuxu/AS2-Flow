package com.as2flow.backend.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
public class AS2Message extends AbstractEntity implements Cloneable
{
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private final Partnership partnership;

    @NotNull
    private final LocalTime timeProcessed;

    public AS2Message(Partnership partnership)
    {
        this.partnership = partnership;
        this.timeProcessed = LocalTime.now();
    }

    public Partnership getPartnership()
    {
        return partnership;
    }

    public LocalTime getTimeProcessed()
    {
        return timeProcessed;
    }
}
