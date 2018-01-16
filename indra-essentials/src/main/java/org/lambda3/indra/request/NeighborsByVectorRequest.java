package org.lambda3.indra.request;

public class NeighborsByVectorRequest extends AbstractBasicRequest<NeighborsByVectorRequest> {

    private double[] vector;
    private int topk;

    public NeighborsByVectorRequest vector(double[] vector) {
        this.vector = vector;
        return this;
    }

    public NeighborsByVectorRequest topk(int topk) {
        this.topk = topk;
        return this;
    }

    public int getTopk() {
        return topk;
    }

    public double[] getVector() {
        return vector;
    }

    @Override
    protected String isValid() {
        StringBuilder errorMessages = new StringBuilder();

        if (topk <= 0) {
            errorMessages.append(" - 'topk' must be greater than 0;\\n");
        }

        if (vector == null) {
            errorMessages.append(" - 'vector' can't be null;\\n");
        } else if (vector.length == 0) {
            errorMessages.append(" - 'vector' can't be empty;\\n");
        }

        return errorMessages.toString();
    }
}
