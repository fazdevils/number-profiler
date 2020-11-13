package com.blackwaterpragmatic.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.blackwaterpragmatic.bean.Result;
import com.blackwaterpragmatic.helper.NumberProfileHelper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Number profile handler unit test.
 */
@ExtendWith(MockitoExtension.class)
class NumberProfileHandlerTest {

    @Mock
    private NumberProfileHelper numberProfileHelper;

    private NumberProfileHandler numberProfileHandler;

    @Mock
    private List<Long> numberSeries;

    @Mock
    private Context context;

    @BeforeEach
    public void before() {
        numberProfileHandler = new NumberProfileHandler(numberProfileHelper);
    }

    @Test
    void testValidRequest() {
        numberProfileHandler.handleRequest(numberSeries, context);

        verify(numberProfileHelper).profileSeries(numberSeries);
        verifyNoMoreInteractions(numberProfileHelper, numberSeries, context);
    }

    @Test
    void testFailedRequest() {
        when(numberProfileHelper.profileSeries(numberSeries)).thenThrow(new RuntimeException("mock exception"));

        final Result result = numberProfileHandler.handleRequest(numberSeries, context);
        assertNull(result.getMean());
        assertNull(result.getMedian());
        assertNull(result.getMode());
        assertEquals("Unexpected error. (mock exception)", result.getError());

        verify(numberProfileHelper).profileSeries(numberSeries);
        verifyNoMoreInteractions(numberProfileHelper, numberSeries, context);
    }
}
