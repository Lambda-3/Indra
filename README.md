# Indra Project

* Indra is a web service which allows easy access to four different distributional semantics models (DSM) in 13 languages.
    * Languages: English, German, Spanish, French, Portuguese, Italian, Swedish	, Chinese, Dutch, Russian, Korean, Arabic and Persian;
    * DSM: Latent Semantic Analysis (LSA), Explicit Semantic Analysis (ESA), Skip-gram (W2V) and Global Vectors (GloVe).
    * Score Function: ALPHASKEW, CHEBYSHEV, CITYBLOCK, CORRELATION, COSINE, DICE, EUCLIDEAN, JACCARD, JACCARD2, JENSENSHANNON, LIN, TANIMOTO.

## Code Examples
* Python. This relies on the library `requests`.

```python

import requests
import json

pairs = [
    {'t1': 'house', 't2': 'beer'},
    {'t1': 'car', 't2': 'engine'}]

data = {'corpus': 'wiki-2014',
        'model': 'W2V',
        'language': 'EN',
        'scoreFunction': 'COSINE', 'pairs': pairs}

headers = {
    'accept': "application/json",
    'content-type': "application/json",
    'cache-control': "no-cache"
}

res = requests.post("http://example.com:8916/indra/v1/relatedness", data=json.dumps(data), headers=headers)
res.raise_for_status()
print(res.json())
```

* Java. This code depends on the [OkHttp lib] (http://square.github.io/okhttp/), whose maven dependency entry is bellow

```xml
<dependency>
  <groupId>com.squareup.okhttp3</groupId>
  <artifactId>okhttp</artifactId>
  <version>3.4.1</version>
</dependency>
```

```java
    OkHttpClient client = new OkHttpClient();
    String content = "{\"corpus\": \"wiki-2014\", \"model\": \"W2V\", \"language\": \"EN\","
	    		+ "\"scoreFunction\": \"COSINE\", \"pairs\": [{\"t1\": \"wife\", \"t2\": \"mother\"}]}";

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, content);
    Request request = new Request.Builder()
      .url("http://indra.amtera.net:80/indra/v1/relatedness")
      .post(body)
      .addHeader("accept", "application/json")
      .addHeader("content-type", "application/json")
      .addHeader("authorization", "<ADD HERE>")
      .addHeader("cache-control", "no-cache")
      .build();
    
    Response response = client.newCall(request).execute();
    System.out.println(response.body().string());
```


## Parameters
The indra service requires five parameters:

### language
Specify the model language according to the two-letter-code [ISO 639-1] (https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes).
The following languages are available in the current version of Indra:
* EN - English
* DE - German
* ES - Spanish
* FR - French
* PT - Portuguese
* IT - Italian
* SV - Swedish
* ZH - Chinese
* NL - Dutch
* RU - Russian
* KO - Korean
* AR - Arabic
* FA - Persian

### corpus
Define the corpus from which the model where generated.
Currently there is only one option of model per language. for Korean, the corpus name is wiki-2016, while for all others it is wiki-2014.

### model
Specify the distributional semantics model. Four models are available:
* LSA - Latent Semantic Analysis
* ESA - Explicit Semantic Analysis
* W2V - Skip-gram
* GLOVE - Global Vectors

### scoreFunction
Specify the function applied to calculate the relatedness between vectors
* ALPHASKEW
* CHEBYSHEV
* CITYBLOCK
* CORRELATION
* COSINE
* DICE
* EUCLIDEAN
* JACCARD
* JACCARD2
* JENSENSHANNON
* LIN
* TANIMOTO

## Tech Details/Important TODOs (not relevant for users):

* This Project generates two main artifacts:
    * The Java Indra Client that reads the indices from a MongoDB intance and implements all
    distributional models;
    * And a REST Service to expose Relatedness features.
    
* If you got the source files just run `mvn package` to build all aritifacts.

* There are a lot of TODOs scattered all over the code but here the most important ones:
    * JavaDocs of all public interfaces;
    * Configure generation of JavaDoc / Maven Site?
    * Improve unit tests. Specially for the VectorSpace implementation;
    * Unit tests to enforce JSON contract on the REST interface;
    * Decouple the composition method of multi-term vectors;
    * <s>Add an authentication method to the REST service;</s>
    * Improve Error messages on the REST interface;
    * <s>Distribute the REST Java Bean Interfaces to ease serialization on the REST Client side;</s>  
    * Evaluate the results;
    * Add the Java Client to build the distributional indices from the corpus.
 
