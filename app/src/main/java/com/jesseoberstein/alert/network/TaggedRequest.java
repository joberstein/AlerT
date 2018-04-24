package com.jesseoberstein.alert.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class TaggedRequest extends StringRequest {

    public interface TaggedResponseListener {
        void onResponse(Object tag, String response);
    }

    private final TaggedResponseListener mListener;

    public TaggedRequest(String url, TaggedResponseListener listener, Response.ErrorListener errorListener) {
        super(url, null, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(getTag(), response);
    }
}
