package com.jesseoberstein.alert.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class TaggedRequest extends StringRequest {

    public interface TaggedResponseListener {
        void onResponse(Object tag, String response);
    }

    public interface TaggedErrorListener {
        void onError(Object tag, VolleyError error);
    }

    private final TaggedResponseListener mListener;
    private final TaggedErrorListener mErrorListener;

    public TaggedRequest(String url, TaggedResponseListener listener, TaggedErrorListener errorListener) {
        super(url, null, null);
        mListener = listener;
        mErrorListener = errorListener;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(getTag(), response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onError(getTag(), error);
    }
}
