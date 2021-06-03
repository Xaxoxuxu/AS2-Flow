package com.as2flow.application.backend.entity;

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
    @ElementCollection(fetch=FetchType.EAGER)
    @Column(name="value")
    @MapKeyColumn(name="attribute")
    private Map<String, String> senderAttrs;

    @NotNull
    @NotEmpty
    @ElementCollection(fetch=FetchType.EAGER)
    @Column(name="value")
    @MapKeyColumn(name="attribute")
    private Map<String, String> receiverAttrs;

    @NotNull
    @NotEmpty
    @ElementCollection(fetch=FetchType.EAGER)
    @Column(name="value")
    @MapKeyColumn(name="attribute")
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
}
