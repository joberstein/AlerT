package com.jesseoberstein.alert.models.mbta;


import com.github.jasminb.jsonapi.Links;
import com.github.jasminb.jsonapi.annotations.Id;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

class BaseResource implements Serializable {

    @Id
    @DatabaseField(columnName = "id", id = true)
    private String id;

    @com.github.jasminb.jsonapi.annotations.Links
    private Links links;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return String.format("id='%s'", id);
    }
}
