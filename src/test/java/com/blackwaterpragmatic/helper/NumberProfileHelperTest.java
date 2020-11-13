package com.blackwaterpragmatic.helper;

import com.blackwaterpragmatic.bean.Result;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Number profile helper unit test.
 */
@SuppressWarnings("checkstyle:magicnumber")
class NumberProfileHelperTest {

    private final NumberProfileHelper numberProfileHelper = new NumberProfileHelper();

    @Test
    void testEmptyNumberSeries() {
        final List<Long> numberSeries = new ArrayList<>();
        final Result result = numberProfileHelper.profileSeries(numberSeries);

        assertNull(result.getMean());
        assertNull(result.getMedian());
        assertNull(result.getMode());
        assertEquals("Empty list.", result.getError());
    }

    @Test
    void testNumberSeriesWith1Entry() {
        final List<Long> numberSeries = new ArrayList<Long>() {
            {
                add(1L);
            }
        };
        final Result result = numberProfileHelper.profileSeries(numberSeries);

        assertEquals(Double.valueOf(1.0), result.getMean());
        assertEquals(Long.valueOf(1), result.getMedian());
        assertNull(result.getMode());
        assertNull(result.getError());
    }

    @Test
    void testNumberSeriesWith2DifferentEntries() {
        final List<Long> numberSeries = new ArrayList<Long>() {
            {
                add(1L);
                add(2L);
            }
        };
        final Result result = numberProfileHelper.profileSeries(numberSeries);

        assertEquals(Double.valueOf(1.5), result.getMean());
        assertEquals(Double.valueOf(1.5), result.getMedian());
        assertNull(result.getMode());
        assertNull(result.getError());
    }

    @Test
    void testNumberSeriesWith2SameEntries() {
        final List<Long> numberSeries = new ArrayList<Long>() {
            {
                add(2L);
                add(2L);
            }
        };
        final Result result = numberProfileHelper.profileSeries(numberSeries);

        assertEquals(Double.valueOf(2), result.getMean());
        assertEquals(Double.valueOf(2), result.getMedian());
        assertArrayEquals(new Number[]{2L}, result.getMode());
        assertNull(result.getError());
    }

    @Test
    void testNumberSeriesWith2ModeEntries() {
        final List<Long> numberSeries = new ArrayList<Long>() {
            {
                add(1L);
                add(2L);
                add(3L);
                add(2L);
                add(1L);
            }
        };
        final Result result = numberProfileHelper.profileSeries(numberSeries);

        assertEquals(Double.valueOf(1.8), result.getMean());
        assertEquals(Long.valueOf(2), result.getMedian());
        assertArrayEquals(new Number[]{1L, 2L}, result.getMode());
        assertNull(result.getError());
    }

    @Test
    void testNumberSeriesWithSecondShorterModeRun() {
        final List<Long> numberSeries = new ArrayList<Long>() {
            {
                add(1L);
                add(2L);
                add(2L);
                add(1L);
                add(1L);
            }
        };
        final Result result = numberProfileHelper.profileSeries(numberSeries);

        assertEquals(Double.valueOf(1.4), result.getMean());
        assertEquals(Long.valueOf(1), result.getMedian());
        assertArrayEquals(new Number[]{1L}, result.getMode());
        assertNull(result.getError());
    }

    @Test
    void testArithmeticOverflow() {
        final List<Long> numberSeries = new ArrayList<Long>() {
            {
                add(Long.MAX_VALUE);
                add(1L);
            }
        };
        final Result result = numberProfileHelper.profileSeries(numberSeries);

        assertNull(result.getMean());
        assertNull(result.getMedian());
        assertNull(result.getMode());
        assertEquals("Arithmetic overflow.  Cannot compute result.", result.getError());
    }

}
