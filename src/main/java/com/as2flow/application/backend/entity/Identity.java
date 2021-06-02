package com.as2flow.application.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Identity extends AbstractEntity implements Cloneable
{
   // @OneToMany(mappedBy = "identity")
    //Set<PartnershipEntity> partnerships;
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String name = "";
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String as2Id = "";
    @NotNull
    @NotEmpty
    private String keyAlias = "";

    public Identity()
    {
    }

    public Identity(String name, String as2Id, String keyAlias)
    {
        this.name = name;
        this.as2Id = as2Id;
        this.keyAlias = keyAlias;
    }

    public String getKeyAlias()
    {
        return keyAlias;
    }

    public String getName()
    {
        return name;
    }

    public String getAs2Id()
    {
        return as2Id;
    }

    @Override
    public String toString()
    {
        return "Identity{" +
                "name='" + name + '\'' +
                ", as2Id='" + as2Id + '\'' +
                ", keyAlias='" + keyAlias + '\'' +
                '}';
    }
}
