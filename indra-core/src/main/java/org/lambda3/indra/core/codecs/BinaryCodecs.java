package org.lambda3.indra.core.codecs;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.io.*;
import java.util.Map;

public final class BinaryCodecs {

    /**
     * Sparse vector serialization to byte array.
     */
    public static byte[] marshall(Map<Integer, Double> vector) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (DataOutputStream tdos = new DataOutputStream(baos)) {
                tdos.writeInt(vector.size());
                for (Integer key : vector.keySet()) {
                    tdos.writeInt(key);
                    tdos.writeFloat(vector.get(key).floatValue());
                }
                return baos.toByteArray();
            }
        }
    }

    /**
     * Dense vectors serialization to byte array.
     */
    public static byte[] marshall(double[] vector) throws IOException  {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (DataOutputStream tdos = new DataOutputStream(baos)) {
                for (int i = 0; i < vector.length; i++) {
                    tdos.writeDouble((vector[i]));
                }
                return baos.toByteArray();
            }
        }
    }

    /**
     * Vector deserialization.
     */
    public static RealVector unmarshall(byte[] bytes, boolean sparse, int dimensions) throws IOException {
        RealVector realVector = !sparse ? new ArrayRealVector(dimensions) : new OpenMapRealVector(dimensions);

        if (!sparse) {
            try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes))) {
                for (int i = 0; i < dimensions; i++) {
                    realVector.setEntry(i, dis.readDouble());
                }
            }
        }
        else {
            try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes))) {
                while (true) {
                    try {
                        realVector.setEntry(dis.readInt(), dis.readDouble());
                    }
                    catch (EOFException e) {
                        break;
                    }
                }
            }
        }

        return realVector;
    }

    /**
     * Legacy (not created by IndraLoader) dense vector deserialization.
     */
    public static RealVector legacyUnmarshall(byte[] b, int limit, boolean sparse, int maxDimensions) throws IOException {
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b))) {
            int key;
            double score;
            int size = Math.min(dis.readInt(), limit);

            RealVector vector;
            if (sparse) {
                vector = new OpenMapRealVector(maxDimensions);
            } else {
                vector = new ArrayRealVector(size);
            }

            for (int i = 0; i < size; i++) {
                key = dis.readInt();
                score = dis.readFloat();
                vector.setEntry(key, score);
            }

            return vector;
        }
    }

}
