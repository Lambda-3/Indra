# Code Snippet
 
 You just need to add the dependency to the `org.indra_project:indra-rest` artifact.

        ClientResource cli =
                new ClientResource("http://example.com:8916/indra/v1/relatedness");

        RelatednessResource relatednessResource = cli.wrap(RelatednessResource.class);

        RelatednessRequest request = new RelatednessRequest();
        request.corpus = "wiki-2014";
        request.language = Language.EN;
        request.model = Model.ESA;
        request.scoreFunction = ScoreFunction.COSINE;
        request.pairs = Collections.singletonList(new TextPair("car", "engine"));

        RelatednessResponse response = relatednessResource.getRelatedness(request);
        
        response.pairs.forEach(pair -> {
            System.out.println(String.format("%s-%s: %f", pair.t1, pair.t2, pair.score));
        });
