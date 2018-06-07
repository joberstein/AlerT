package com.jesseoberstein.alert.validation;

import java.util.Collection;

public interface Validatable {

    /**
     * The implementing class should determine if the class fields are valid, returning true iff
     * all fields are valid and false otherwise.
     */
    boolean isValid();

    /**
     * Returns a collection of field names that are not valid.
     */
    Collection<String> getErrors();
}
