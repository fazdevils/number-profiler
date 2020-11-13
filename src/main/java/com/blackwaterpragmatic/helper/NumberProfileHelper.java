package com.blackwaterpragmatic.helper;

import com.blackwaterpragmatic.bean.Result;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to perform the number profile operation.
 */
public class NumberProfileHelper {

    /**
     * Profile the provided number series.
     * @param numberSeries
     * @return
     */
    public Result profileSeries(final List<Long> numberSeries) {
        try {
            if (numberSeries.isEmpty()) {
                return buildErrorResult("Empty list.");
            } else {
                return buildSeriesProfileResult(numberSeries);
            }
        } catch (@SuppressWarnings("unused") final ArithmeticException e) {
            return buildErrorResult("Arithmetic overflow.  Cannot compute result.");
        }
    }

    /**
     * Algorithm to profile the series.
     * - sort the list (required for median & mode)
     * - take one pass through the list and
     * -- calculate sum (required for mean)
     * -- determine mode
     * - return the calculated mean, median, and mode
     * @param numberSeries
     * @return
     */
    private Result buildSeriesProfileResult(final List<Long> numberSeries) {
        numberSeries.sort(null);

        Long sum = numberSeries.get(0);
        List<Long> modeList = null;

        Long previousNumber = numberSeries.get(0);

        Integer currentModeRun = 1;
        Integer maxModeRun = 0;

        for (int i = 1; i < numberSeries.size(); ++i) {
            final Long number = numberSeries.get(i);
            sum = Math.addExact(sum, number);

            if (number.equals(previousNumber)) {
                currentModeRun++;
            } else {
                if (shouldCheckForModeUpdate(currentModeRun)) {
                    modeList = updateModeList(modeList, previousNumber, currentModeRun, maxModeRun);
                    maxModeRun = updateMaxModeRun(currentModeRun, maxModeRun);
                }
                previousNumber = number;
                currentModeRun = 1;
            }
        }

        if (shouldCheckForModeUpdate(currentModeRun)) {
            modeList = updateModeList(modeList, previousNumber, currentModeRun, maxModeRun);
        }

        final Result result = new Result();
        result.setMean(calculateMean(sum, numberSeries.size()));
        result.setMedian(calculateMedian(numberSeries));
        result.setMode(getModeArray(modeList));
        return result;
    }

    private Integer updateMaxModeRun(final Integer currentModeRun, final Integer maxModeRun) {
        if (newLongRun(currentModeRun, maxModeRun)) {
            return currentModeRun;
        } else {
            return maxModeRun;
        }
    }

    private List<Long> updateModeList(
            final List<Long> modeList,
            final Long modeNumber,
            final Integer currentModeRun,
            final Integer maxModeRun) {

        if (newLongRun(currentModeRun, maxModeRun)) {
            final List<Long> newModeList = new ArrayList<>();
            newModeList.add(modeNumber);
            return newModeList;
        } else if (matchingLongRun(currentModeRun, maxModeRun)) {
            modeList.add(modeNumber);
            return modeList;
        } else {
            return modeList;
        }
    }

    private Number calculateMean(final Long sum, final int size) {
        return sum / (double) size;
    }

    private Number calculateMedian(final List<Long> numberSeries) {
        final int midpoint = numberSeries.size() / 2;
        if (((numberSeries.size()) % 2) == 0) {
            return (numberSeries.get(midpoint - 1) + numberSeries.get(midpoint)) / 2.0;
        } else {
            return numberSeries.get(midpoint);
        }
    }

    private Number[] getModeArray(final List<Long> modeList) {
        if (modeList == null) {
            return null;
        } else {
            return modeList.toArray(new Number[0]);
        }
    }

    private Result buildErrorResult(final String errorMessage) {
        return new Result() {
            {
                setError(errorMessage);
            }
        };
    }

    private boolean shouldCheckForModeUpdate(final Integer runValue) {
        return runValue > 1;
    }

    private boolean newLongRun(final Integer currentModeRun, final Integer maxModeRun) {
        return currentModeRun > maxModeRun;
    }

    private boolean matchingLongRun(final Integer currentModeRun, final Integer maxModeRun) {
        return currentModeRun == maxModeRun;
    }
}
