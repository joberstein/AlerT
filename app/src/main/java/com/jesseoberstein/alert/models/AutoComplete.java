package com.jesseoberstein.alert.models;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static android.widget.AdapterView.OnItemClickListener;
import static android.widget.AutoCompleteTextView.Validator;
import static java.util.stream.Collectors.toSet;

public class AutoComplete<T> {
    private List<T> items;
    private int layout;

    private ArrayAdapter<T> adapter;
    private Validator validator;
    private OnItemClickListener itemClickListener;
    
    public AutoComplete(List<T> items, OnItemClickListener itemClickListener) {
        this.items = Collections.unmodifiableList(items);
        this.layout = android.R.layout.simple_dropdown_item_1line;
        this.validator = this.createValidator();
        this.itemClickListener = itemClickListener;
    }

    public ArrayAdapter<T> getAdapter() {
        return adapter;
    }

    public void setAdapter(ArrayAdapter<T> adapter) {
        this.adapter = adapter;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void attachAdapter(Context context) {
        setAdapter(new ArrayAdapter<T>(context, this.layout, this.items));
    }

    private Validator createValidator() {
        return new Validator() {
            @Override
            public boolean isValid(CharSequence input) {
                HashSet<String> suggestions = new HashSet<>(items.stream().map(T::toString).collect(toSet()));
                return suggestions.contains(input.toString().trim());
            }

            @Override
            public CharSequence fixText(CharSequence charSequence) {
                return "";
            }
        };
    }
}
