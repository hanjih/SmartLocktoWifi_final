package com.example.han.smartlocktowifi;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by han on 2017-06-20.
 */

public class GPSlng implements Serializable {
    private static LatLng crtposition;

    public static void setlng(LatLng crt) {
        crtposition = crt;
    }

    public static LatLng getlng() {
        return crtposition;
    }
}
