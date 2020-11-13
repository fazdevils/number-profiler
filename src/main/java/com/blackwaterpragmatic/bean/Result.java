package com.blackwaterpragmatic.bean;

/**
 * Holds result of the number profiler operation.
 */
public class Result {
    private Number mean;
    private Number median;
    private Number[] mode;
    private String error;

    public Number getMean() {
        return mean;
    }

    public void setMean(final Number mean) {
        this.mean = mean;
    }

    public Number getMedian() {
        return median;
    }

    public void setMedian(final Number median) {
        this.median = median;
    }

    public Number[] getMode() {
        return mode;
    }

    public void setMode(final Number[] mode) {
        this.mode = mode;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

}
