![](indra_logo.png)

[![Build Status](https://travis-ci.org/Lambda-3/indra.svg?branch=master)](https://travis-ci.org/Lambda-3/indra)

# Indra

Indra is a Web Service which allows easy access to several [Distributional Semantics Models](https://en.wikipedia.org/wiki/Distributional_semantics), currently in English, German, Spanish, French, Portuguese, Italian, Swedish, Chinese, Dutch, Russian, Korean, Arabic, Japanese and Persian.

The Supported models are:
* [Latent Semantic Analysis (LSA)](https://en.wikipedia.org/wiki/Latent_semantic_analysis)
* [Explicit Semantic Analysis (ESA)](https://en.wikipedia.org/wiki/Explicit_semantic_analysis)
* [Word2Vec (W2V)](https://en.wikipedia.org/wiki/Word2vec)
* [Global Vectors (GloVe)](https://en.wikipedia.org/wiki/GloVe_(machine_learning))

# Usage

If you want to give a try on your own infrastructure take a look on [Indra-Composed](https://github.com/Lambda-3/indra-composed).


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
* COSINE
* ALPHASKEW
* CHEBYSHEV
* CITYBLOCK
* SPEARMAN
* PEARSON
* DICE
* EUCLIDEAN
* JACCARD
* JACCARD2
* JENSENSHANNON

