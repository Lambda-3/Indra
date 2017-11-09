package org.lambda3.indra;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class JSONUtil {

    public static Map<String, Object> loadJSONAsMap(File file) {
        if (file.exists()) {
            try {
                JSONParser jsonParser = new JSONParser();
                return (JSONObject) jsonParser.parse(new FileReader(file));

            } catch (ParseException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        return Collections.EMPTY_MAP;
    }

    public static void writeMapAsJson(Map<String, Object> map, File file) throws IOException {
        JSONObject metadataJson = toJSONObject(map);

        Writer writer = new FileWriter(file);
        metadataJson.writeJSONString(writer);
        writer.close();
    }

    private static JSONObject toJSONObject(Map<String, Object> map) {
        JSONObject object = new JSONObject();

        for (String key : map.keySet()) {
            Object content = map.get(key);
            if (content instanceof Collection) {
                JSONArray array = new JSONArray();
                array.addAll((Collection<?>) content);
                object.put(key, array);
            } else if (content instanceof Map) {
                object.put(key, toJSONObject((Map<String, Object>) content));
            } else {
                object.put(key, content);
            }
        }

        return object;
    }
}
