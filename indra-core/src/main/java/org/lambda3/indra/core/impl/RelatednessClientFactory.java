package org.lambda3.indra.core.impl;

import com.mongodb.MongoClientURI;
import org.lambda3.indra.core.RelatednessClient;
import org.lambda3.indra.core.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RelatednessClientFactory {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final MongoClientURI mURI;
    private final Map<Params, RelatednessClient> clients = new ConcurrentHashMap<>();


    public RelatednessClientFactory(String mongoURI) {
        if (mongoURI == null || mongoURI.isEmpty()) {
            throw new IllegalArgumentException("mongoURI is mandatory");
        }
        this.mURI = new MongoClientURI(mongoURI);
        logger.debug("Factory initialized.");
    }

    public RelatednessClient create(Params params) {
        switch (params.func) {
            case COSINE:
                return getCosineClient(params);
            case CORRELATION:
                return getCorrelationClient(params);
            case EUCLIDEAN:
                return getEuclideanClient(params);
            case JACCARD:
                return getJaccardClient(params);
            case LIN:
                return getLinClient(params);
            case TANIMOTO:
                return getTanimotoClient(params);
            case ALPHASKEW:
                return getAlphaSkewClient(params);
            case CHEBYSHEV:
                return getChebyshevClient(params);
            case CITYBLOCK:
                return getCityBlockClient(params);
            case DICE:
                return getDiceClient(params);
            case JACCARD2:
                return getJaccard2Client(params);
            case JENSENSHANNON:
                return getJensenShannonClient(params);
            default:
                throw new RuntimeException("Unsupported Score Function.");
        }
    }

    //TODO: VectorSpace implements a cache and is thread-safe, should we share between clients?

    private RelatednessClient getCosineClient(final Params params) {
        return clients.computeIfAbsent(params, (p) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, p.getDBName());
            return new CosineClient(p, vectorSpace);
        });
    }

    private RelatednessClient getCorrelationClient(final Params params) {
        return clients.computeIfAbsent(params, (p) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, p.getDBName());
            return new CorrelationClient(p, vectorSpace);
        });
    }

    private RelatednessClient getEuclideanClient(final Params params) {
        return clients.computeIfAbsent(params, (p) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, p.getDBName());
            return new EuclideanClient(p, vectorSpace);
        });
    }

    private RelatednessClient getJaccardClient(final Params params) {
        return clients.computeIfAbsent(params, (p) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, p.getDBName());
            return new JaccardClient(p, vectorSpace);
        });
    }

    private RelatednessClient getLinClient(final Params params) {
        return clients.computeIfAbsent(params, (p) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, p.getDBName());
            return new LinClient(p, vectorSpace);
        });
    }

    private RelatednessClient getTanimotoClient(final Params params) {
        return clients.computeIfAbsent(params, (p) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, p.getDBName());
            return new TanimotoClient(p, vectorSpace);
        });
    }

    private RelatednessClient getAlphaSkewClient(final Params params) {
        return clients.computeIfAbsent(params, (p) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, p.getDBName());
            return new AlphaSkewClient(p, vectorSpace);
        });
    }

    private RelatednessClient getChebyshevClient(final Params params) {
        return clients.computeIfAbsent(params, (p) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, p.getDBName());
            return new ChebyshevClient(p, vectorSpace);
        });
    }

    private RelatednessClient getCityBlockClient(final Params params) {
        return clients.computeIfAbsent(params, (p) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, p.getDBName());
            return new CityBlockClient(p, vectorSpace);
        });
    }

    private RelatednessClient getDiceClient(final Params params) {
        return clients.computeIfAbsent(params, (p) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, p.getDBName());
            return new DiceClient(p, vectorSpace);
        });
    }

    private RelatednessClient getJaccard2Client(final Params params) {
        return clients.computeIfAbsent(params, (p) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, p.getDBName());
            return new Jaccard2Client(p, vectorSpace);
        });
    }

    private RelatednessClient getJensenShannonClient(final Params params) {
        return clients.computeIfAbsent(params, (p) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, p.getDBName());
            return new JensenShannonClient(p, vectorSpace);
        });
    }

}
