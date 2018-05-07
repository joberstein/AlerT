package com.jesseoberstein.alert.models.mbta;


import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.github.jasminb.jsonapi.Links;
import com.github.jasminb.jsonapi.annotations.Id;

import java.io.Serializable;

class BaseResource implements Serializable {

    @Id
    @NonNull
    private String id;

    @Ignore
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
