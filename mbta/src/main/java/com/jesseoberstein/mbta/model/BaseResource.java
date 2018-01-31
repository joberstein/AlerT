package com.jesseoberstein.mbta.model;


import com.github.jasminb.jsonapi.annotations.Id;
import com.github.jasminb.jsonapi.Links;

class BaseResource {
    @Id
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
