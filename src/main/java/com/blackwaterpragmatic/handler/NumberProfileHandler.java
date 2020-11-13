package com.blackwaterpragmatic.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.blackwaterpragmatic.bean.Result;
import com.blackwaterpragmatic.helper.NumberProfileHelper;
import java.util.List;

/**
 * AWS Lambda handler to profile the supplied number series.
 */
public class NumberProfileHandler implements RequestHandler<List<Long>, Result> {

    private final NumberProfileHelper numberProfileHelper;

    public NumberProfileHandler() {
        numberProfileHelper = new NumberProfileHelper();
    }

    public NumberProfileHandler(final NumberProfileHelper numberProfileHelper) {
        this.numberProfileHelper = numberProfileHelper;
    }

    @SuppressWarnings("checkstyle:illegalcatch")
    @Override
    public Result handleRequest(final List<Long> numberSeries, final Context context) {
        try {
            return numberProfileHelper.profileSeries(numberSeries);
        } catch (final Throwable e) {
            final Result result = new Result();
            result.setError(String.format("Unexpected error. (%s)", e.getMessage()));
            return result;
        }
    }
}
