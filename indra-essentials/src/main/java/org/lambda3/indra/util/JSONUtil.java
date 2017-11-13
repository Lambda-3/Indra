package org.lambda3.indra.util;

/*-
 * ==========================License-Start=============================
 * Indra Essentials Module
 * --------------------------------------------------------------------
 * Copyright (C) 2016 - 2017 Lambda^3
 * --------------------------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * ==========================License-End===============================
 */

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
