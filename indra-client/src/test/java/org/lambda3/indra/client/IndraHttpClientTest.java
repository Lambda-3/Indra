package org.lambda3.indra.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.lambda3.indra.common.client.*;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.testng.Assert.assertEquals;

/*-
 * ==========================License-Start=============================
 * Indra Core Module
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

public class IndraHttpClientTest {

    @Test
    public void RelatednessTest() throws Exception {

        // Build the request
        List<TextPair> reqTextPairs = new LinkedList<TextPair>() {{
            add(new TextPair("mother", "love"));
            add(new TextPair("father", "love"));
        }};
        RelatednessRequest request = new RelatednessRequest("wiki-2014", "W2V", "EN", reqTextPairs, ScoreFunction.COSINE);

        // Mock the response
        ObjectMapper mapper = new ObjectMapper();
        RelatednessResponse mockResp = new RelatednessResponse();
        mockResp.corpus = "wiki-2014";
        mockResp.model = "W2V";
        mockResp.language = "EN";
        mockResp.scoreFunction = ScoreFunction.COSINE;
        mockResp.pairs = new LinkedList<ScoredTextPair>() {{
            add(new ScoredTextPair(new AnalyzedPair(new TextPair("mother", "love")), 0.9));
            add(new ScoredTextPair(new AnalyzedPair(new TextPair("father", "love")), 0.3));
        }};

        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(mapper.writeValueAsString(mockResp)));

        // Start the mock server.
        server.start();

        IndraHttpClient c = new IndraHttpClient(server.url("/").toString());

        RelatednessResponse resp = c.Relatedness(request);
        Collection<ScoredTextPair> pairse = resp.pairs;

        // Test response unmarshalling
        assertEquals(resp, mockResp);

        // Test request path and body
        RecordedRequest request1 = server.takeRequest();
        assertEquals(request1.getPath(), "/relatedness");

        RelatednessRequest gotReq = mapper.readValue(request1.getBody().inputStream(), RelatednessRequest.class);
        assertEquals(gotReq, request);
    }
}
