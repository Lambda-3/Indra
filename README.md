![](indra_logo.png)

[![Build Status](https://travis-ci.org/Lambda-3/Indra.svg?branch=master)](https://travis-ci.org/Lambda-3/Indra)
[![Chat](https://badges.gitter.im/Lambda-3/gitter.png)](https://gitter.im/Lambda-3/Lobby)

Table of Contents
=================

   * [What is Indra?](#what-is-indra)
   * [Features](#features)
   * [Supported Models](#supported-models)
   * [Word Embeddings](#word-embeddings)
      * [Request data model (POST /vectors)](#request-data-model-post-vectors)
         * [Field <em>corpus</em>](#field-corpus)
         * [Field <em>model</em>](#field-model)
         * [Field <em>language</em>](#field-language)
      * [Response model](#response-model)
   * [Semantic Similarity](#semantic-similarity)
      * [Request data model (POST /relatedness)](#request-data-model-post-relatedness)
         * [Field <em>scoreFunction</em>](#field-scorefunction)
      * [Response model](#response-model-1)
   * [Translated Word Embeddings and Semantic Similarity](#translated-word-embeddings-and-semantic-similarity)
   * [Usage](#usage)
      * [Public Endpoint](#public-endpoint)
         * [For word embeddings:](#for-word-embeddings)
         * [For semantic similarity:](#for-semantic-similarity)
   * [Citing Indra](#citing-indra)

# What is Indra?

The creation of real-world Artificial Intelligence (AI) applications is dependent on leveraging a large volume of commonsense knowledge. Simple semantic interpretation tasks such as understanding that if 'A is married to B' then 'A is the spouse of B' or that 'car, vehicle, auto' have very similar meanings are examples of semantic approximation operations/inferences that are present in practically all applications of AI that interpret natural language.

Many AI applications depend on being semantically flexible, i.e. coping with the large vocabulary variation that is permited by natural language. Sentiment Analysis, Question Answering, Information Extraction, Semantic Search and Classification tasks are examples of tasks in which the ability to do semantic approximation is a central requirement.

[Distributional Semantics Models](https://en.wikipedia.org/wiki/Distributional_semantics) and Word Vector models emerged as successful approaches for supporting semantic approximations due to their ability to build comprehensive semantic approximation models and also to their simplicity of representation.

Indra is a distributional semantics engine which facilitates the deployment of robust distributional semantic models for industry-level applications.

# Features

* Supports multiple distributional semantic models and distance measures;
* No strings attached: permissive license for commercial and academic use;
* Access to the semantic models as a service;
* High performance vector computation;
* Easy deploy: Deploy the infrastructure in 3 steps;
* Intrinsically multi-lingual;
* Pre-build models from different languages;
* Provides translated distributional relatedness.

# Supported Models

* [Latent Semantic Analysis (LSA)](https://en.wikipedia.org/wiki/Latent_semantic_analysis)
* [Explicit Semantic Analysis (ESA)](https://en.wikipedia.org/wiki/Explicit_semantic_analysis)
* [Word2Vec (W2V)](https://en.wikipedia.org/wiki/Word2vec)
* [Global Vectors (GloVe)](https://en.wikipedia.org/wiki/GloVe_(machine_learning))

# Word Embeddings
This is the payload consumed by Indra to serve [Word Embeddings](https://en.wikipedia.org/wiki/Word_embedding) of words or phrases.

## Request data model `(POST /vectors)`

```json
{
	"corpus": "wiki-2014",
	"model": "W2V",
	"language": "EN",
	"terms": ["love", "mother"]
}
```
### Field _corpus_
 
The name of the corpus used to build the models:
 
* wiki-2014 (except JP and KO)
* wiki-2016 (only JP and KO)

### Field _model_ 

The distributional model:

* W2V
* GLOVE
* LSA 
* ESA

### Field _language_ 

Two-letter-code [ISO 639-1](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes):

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
* JP - Japanese
* AR - Arabic
* FA - Persian

## Response model

This is the response for the request above.
```json
{
  "corpus": "wiki-2014",
  "model": "W2V",
  "language": "EN",
  "terms":
    {
      "love" : [0.333, 0.21, 0.3532],
      "mother" : [0.6356, 0.756, 0.9867]
    }
}
```

In the case that the model provides sparse vectors, *terms* attribute will be defined as follows:

```json
{
  "love" : { "0" : 0.333, "1" : 0.21, "2" : 0.3532 },
  "mother" : { "0" : 0.6356, "1" : 0.756, "2" : 0.9867 }
}
```
Currently, only the __ESA__ model is sparse.

# Semantic Similarity

This is the payload consumed by Indra to compute [Semantic Similarity](https://en.wikipedia.org/wiki/Semantic_similarity) between words or phrase pairs.

## Request data model `(POST /relatedness)`

```json
{
	"corpus": "wiki-2014",
	"model": "W2V",
	"language": "EN",
	"scoreFunction": "COSINE",
	"pairs": [{
		"t2": "love",
		"t1": "mother"
	},
	{
		"t2": "love",
		"t1": "father"
	}]
}
```

Fields _corpus_, _model_ and _language_ has the same definition previously shown.

### Field _scoreFunction_

The function to compute the relatedness between the distributional vectors:

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

## Response model

This is the response for the request above.
```json
{
  "corpus": "wiki-2014",
  "model": "W2V",
  "language": "EN",
  "pairs": [
    {
      "t1": "mother",
      "t2": "love",
      "score": 0.45996829519139865
    },
    {
      "t1": "father",
      "t2": "love",
      "score": 0.32337835808129745
    }
  ],
  "scoreFunction": "COSINE"
}
```

## One-to-many request data model `(POST /relatedness/otm)`

```json
{
        "corpus": "wiki-2014",
        "model": "W2V",
        "language": "EN",
        "scoreFunction": "COSINE",
        "one" : "love",
	"many" : ["mother", "father", "child"]
}
```

## One-to-many response model

This is the response for the request above.
```json
{
  "corpus" : "wiki-2014",
  "model" : "W2V",
  "language" : "EN",
  "scoreFunction": "COSINE",
  "one" : "love",
  "many" : 
   {
      "mother" : 0.45996829519139865,
      "father": 0.32337835808129745,
      "child": 0.39881548413514684
   }
}
```

# Translated Word Embeddings and Semantic Similarity

For __translated word embeddings__ and __translated semantic similarity__ just append _"mt" : true_ in the JSON payload.

# Usage

If you want to give a try on your own infrastructure take a look on [Indra-Composed](https://github.com/Lambda-3/indra-composed).

## Public Endpoint

We have a public endpoint for demonstration only hence you can try right now with _cURL_ on the command line.

### For word embeddings:

```
curl -X POST -H "Content-Type: application/json" -d '{
	"corpus": "wiki-2014",
	"model": "W2V",
	"language": "EN",
	"terms": ["love", "mother"]
}' "http://indra.lambda3.org/vectors"
```

### For semantic similarity:

```
curl -X POST -H "Content-Type: application/json" -d '{
	"corpus": "wiki-2014",
	"model": "W2V",
	"language": "EN",
	"scoreFunction": "COSINE",
	"pairs": [{
		"t2": "love",
		"t1": "mother"
	},
	{
		"t2": "love",
		"t1": "father"
	}]
}' "http://indra.lambda3.org/relatedness"
```

# Citing Indra

Please cite Indra, if you use it in your experiments or project.
```latex
@Inbook{Freitas2016,
author="Freitas, Andr{\'e}
and Barzegar, Siamak
and Sales, Juliano Efson
and Handschuh, Siegfried
and Davis, Brian",
editor="Blomqvist, Eva
and Ciancarini, Paolo
and Poggi, Francesco
and Vitali, Fabio",
title="Semantic Relatedness for All (Languages): A Comparative Analysis of Multilingual Semantic Relatedness Using Machine Translation",
bookTitle="Knowledge Engineering and Knowledge Management: 20th International Conference, EKAW 2016, Bologna, Italy, November 19-23, 2016, Proceedings",
year="2016",
publisher="Springer International Publishing",
address="Cham",
pages="212--222",
isbn="978-3-319-49004-5",
doi="10.1007/978-3-319-49004-5_14",
url="http://dx.doi.org/10.1007/978-3-319-49004-5_14"
}
```

# Contributors (alphabetical order)

- Andre Freitas
- Brian Davis
- Juliano Sales
- Leonardo Souza
- Siamak Barzegar
- Siegfried Handschuh
