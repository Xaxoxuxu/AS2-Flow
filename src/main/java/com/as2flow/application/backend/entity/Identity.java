package com.as2flow.application.backend.entity;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Identity extends AbstractEntity implements Cloneable
{
    @NotNull
    @NotEmpty
    private String name = "";

    @NotNull
    @NotEmpty
    private String as2Id = "";

    public Identity()
    {}

    public Identity(String name, String as2Id)
    {
        this.name = name;
        this.as2Id = as2Id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAs2Id()
    {
        return as2Id;
    }

    public void setAs2Id(String as2Id)
    {
        this.as2Id = as2Id;
    }

    @Override
    public String toString()
    {
        return "Identity{" +
                "name='" + name + '\'' +
                ", as2Id='" + as2Id + '\'' +
                '}';
    }
}
