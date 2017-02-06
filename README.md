![](indra_logo.png)

[![Build Status](https://travis-ci.org/Lambda-3/indra.svg?branch=master)](https://travis-ci.org/Lambda-3/indra)
[![Chat](https://badges.gitter.im/Lambda-3/gitter.png)](https://gitter.im/Lambda-3/Lobby)

# What is Indra?

The creation of real-world Artificial Intelligence (AI) applications is dependent on leveraging a large volume of commonsense knowledge. Simple semantic interpretation tasks such as understanding that if 'A is married to B' then 'A is the spouse of B' or that 'car, vehicle, auto' have very similar meanings are examples of semantic approximation operations/inferences that are present in practically all applications of AI that interpret natural language.

Many AI applications depend on being semantically flexible, i.e. coping with the large vocabulary variation that is permited by natural language. Sentiment Analysis, Question Answering, Information Extraction, Semantic Search and Classification tasks are examples of tasks in which the ability to do semantic approximation is a central requirement.

[Distributional Semantics Models](https://en.wikipedia.org/wiki/Distributional_semantics) and Word Vector models emerged as successful approaches for supporting semantic approximations due to their ability to build comprehensive semantic approximation models and also to their simplicity of representation.

Indra is a distributional semantics engine which facilitates the deployment of robust distributional semantic models for industry-level applications.

# Features

* Supports multiple distributional semantic models and distance measures.
* No strings attached: permissive license for commercial and academic use.
* Access to the semantic models as a service.
* High performance vector computation.
* Easy deploy: Deploy the infrastructure in 3 steps.
* Intrinsically multi-lingual.
* Pre-build models from different languages.


# Supported Models

* [Latent Semantic Analysis (LSA)](https://en.wikipedia.org/wiki/Latent_semantic_analysis)
* [Explicit Semantic Analysis (ESA)](https://en.wikipedia.org/wiki/Explicit_semantic_analysis)
* [Word2Vec (W2V)](https://en.wikipedia.org/wiki/Word2vec)
* [Global Vectors (GloVe)](https://en.wikipedia.org/wiki/GloVe_(machine_learning))

# JSON over HTTP API (REST like ;)

This is the payload consumed by Indra to compute [Semantic Similarity](https://en.wikipedia.org/wiki/Semantic_similarity) between words or phrase pairs.

## Request data model

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

* model: The distributional model
 * W2V
 * GLOVE
 * LSA 
 * ESA

* language: Two-letter-code [ISO 639-1] (https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes).
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

* corpus: The name of the corpus used to build the models.
 * wiki-2014 (except JP and KO)
 * wiki-2016 (only JP and KO)

* scoreFunction: The function to compute the relatedness between the distributional vectors
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

# Usage

If you want to give a try on your own infrastructure take a look on [Indra-Composed](https://github.com/Lambda-3/indra-composed).

## Public Endpoint

We have a public endpoint for demonstration only hence you can try right now with _cURL_ on the command line:
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

# Citing Indra in papers

Please cite Indra in your paper, if you use it in your experiments.
```latex
@inproceedings{Barzegar:2015:DOS:2766462.2767870,
 author = {Barzegar, Siamak and Sales, Juliano Efson and Freitas, Andre and Handschuh, Siegfried and Davis, Brian},
 title = {DINFRA: A One Stop Shop for Computing Multilingual Semantic Relatedness},
 booktitle = {Proceedings of the 38th International ACM SIGIR Conference on Research and Development in Information Retrieval},
 series = {SIGIR '15},
 year = {2015},
 isbn = {978-1-4503-3621-5},
 location = {Santiago, Chile},
 pages = {1027--1028},
 numpages = {2},
 url = {http://doi.acm.org/10.1145/2766462.2767870},
 doi = {10.1145/2766462.2767870},
 acmid = {2767870},
 publisher = {ACM},
 address = {New York, NY, USA},
 keywords = {distributional infrastructure, distributional semantic models, multilingual semantic relatedness},
}
```
---
