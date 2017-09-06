package com.priyo.go.Api;

import android.support.annotation.Nullable;

public interface HttpResponseListener {
    void httpResponseReceiver(@Nullable HttpResponseObject result);
}
