package com.openbook.openbook.util;

import com.google.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonService {
    public static List<String> convertJsonToList(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        return gson.fromJson(jsonString, type);
    }
}
