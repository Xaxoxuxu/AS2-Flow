package com.as2flow.application.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Partner extends AbstractEntity implements Cloneable
{
    @NotNull
    @NotEmpty
    @Column(unique=true)
    private String name = "";

    @NotNull
    @NotEmpty
    @Column(unique=true)
    private String as2PartnerId = "";

    @NotNull
    @NotEmpty
    private String keyAlias = "";

    @NotNull
    @NotEmpty
    private String url = "";

   // @OneToMany(mappedBy = "partner")
    //Set<PartnershipEntity> partnerships;

    public Partner()
    {
    }

    public Partner(String name, String as2PartnerId, String keyAlias, String url)
    {
        this.name = name;
        this.as2PartnerId = as2PartnerId;
        this.keyAlias = keyAlias;
        this.url = url;
    }

    public String getKeyAlias()
    {
        return keyAlias;
    }

    public String getName()
    {
        return name;
    }

    public String getAs2PartnerId()
    {
        return as2PartnerId;
    }

    public String getUrl()
    {
        return url;
    }

    @Override
    public String toString()
    {
        return "Partner{" +
                "name='" + name + '\'' +
                ", as2PartnerId='" + as2PartnerId + '\'' +
                ", keyAlias='" + keyAlias + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
