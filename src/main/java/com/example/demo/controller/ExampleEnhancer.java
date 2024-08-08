package com.example.demo.controller;

import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.LoggingEnhancer;
import org.slf4j.MDC;

public class ExampleEnhancer implements LoggingEnhancer {

    private static final String TRACE_ID = "traceId";
    private static final String SPAN_ID = "spanId";

    @Override
    public void enhanceLogEntry(LogEntry.Builder logEntry) {
        logEntry.addLabel(TRACE_ID, MDC.get(TRACE_ID));
        logEntry.addLabel(SPAN_ID, MDC.get(SPAN_ID));
    }
}
