package io.github.avew.util;

import com.google.gson.Gson;

public class KalaUtil {

    public static String bearerToken(String token) {
        return "Bearer " + token;
    }

    public static String toJson(Object o) {
        return new Gson().toJson(o);
    }

}
