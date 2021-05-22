package com.as2flow.application.backend.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Partner extends AbstractEntity implements Cloneable
{
    @NotNull
    @NotEmpty
    private String name = "";

    @NotNull
    @NotEmpty
    private String as2PartnerId = "";

    @NotNull
    @NotEmpty
    private String url = "";

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAs2PartnerId()
    {
        return as2PartnerId;
    }

    public void setAs2PartnerId(String as2PartnerId)
    {
        this.as2PartnerId = as2PartnerId;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "Partner{" +
                "name='" + name + '\'' +
                ", as2PartnerId='" + as2PartnerId + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
