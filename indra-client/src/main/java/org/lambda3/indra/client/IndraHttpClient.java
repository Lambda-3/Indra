package org.lambda3.indra.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.lambda3.indra.common.client.RelatednessRequest;
import org.lambda3.indra.common.client.RelatednessResponse;

import java.io.IOException;

/*-
 * ==========================License-Start=============================
 * Indra Client Module
 * --------------------------------------------------------------------
 * Copyright (C) 2017 Lambda^3
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

/**
 * <h3>IndraHttpClients should be shared</h3>
 * <p>
 * <p>It performs best when you create a single {@code IndraHttpClient} instance and reuse it for
 * all of your calls.</p>
 * <p>
 * <p>Usage:</p>
 * <pre>{@code
 *   List<TextPair> reqTextPairs = new LinkedList<TextPair>() {{
 *     add(new TextPair("mother", "love"));
 *     add(new TextPair("father", "love"));
 *   }};
 *   RelatednessRequest request = new RelatednessRequest("wiki-2014", "W2V", "EN", reqTextPairs, ScoreFunction.COSINE);
 *   IndraHttpClient c = new IndraHttpClient("http://localhost:8916");
 *   RelatednessResponse resp = c.Relatedness(request);
 *   Collection<ScoredTextPair> scored = resp.pairs;
 * }</pre>
 */
public class IndraHttpClient {
    private OkHttpClient client;
    private HttpUrl endpoint;
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse(JSON_CONTENT_TYPE);

    /**
     * Creates a new IndraHttpClient to communicate with the Indra server.
     *
     * @param endpoint The URL to the Indra server without path, e.g. "http://127.0.0.1:8916"
     */
    public IndraHttpClient(String endpoint) {
        this.endpoint = HttpUrl.parse(endpoint);
        this.client = new OkHttpClient();
    }

    /**
     * Requests the relatedness between {@code TextPair}s from the endpoint.
     *
     * @param r RelatednessRequest Request containing the TextPairs.
     * @return RelatednessResponse consisting of ScoredTextPairs, null if request was not successful.
     * @throws IOException if the request could not be executed or response could not be parsed.
     */
    public RelatednessResponse Relatedness(RelatednessRequest r) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(r);
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);

        Request request = new Request.Builder()
                .url(this.endpoint.resolve("/relatedness"))
                .addHeader("accept", JSON_CONTENT_TYPE)
                .addHeader("content-type", JSON_CONTENT_TYPE)
                .post(body)
                .build();

        RelatednessResponse resp = null;
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                resp = mapper.readValue(response.body().byteStream(), RelatednessResponse.class);
            }
        }
        return resp;
    }
}
