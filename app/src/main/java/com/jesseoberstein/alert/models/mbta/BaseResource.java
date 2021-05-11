package com.jesseoberstein.alert.models.mbta;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

import com.github.jasminb.jsonapi.Links;
import com.github.jasminb.jsonapi.annotations.Id;

import java.io.Serializable;

public class BaseResource implements Serializable {

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
