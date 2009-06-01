/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.exceptions.base;

import static org.mockitoutil.ExtraMatchers.*;

import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

public class StackTraceFilterTest extends TestBase {
    
    private StackTraceFilter filter = new StackTraceFilter();
    
    @Test
    public void shouldFilterOutCglibGarbage() {
        StackTraceElement[] t = new TraceBuilder().classes(
            "MockitoExampleTest",
            "List$$EnhancerByMockitoWithCGLIB$$2c406024"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t, 0);
        
        assertThat(filtered, hasOnlyThoseClasses("MockitoExampleTest"));
    }
    
    @Test
    public void shouldFilterOutMockitoPackage() {
        StackTraceElement[] t = new TraceBuilder().classes(
            "org.test.MockitoSampleTest",
            "org.mockito.Mockito"
        ).toTraceArray();
            
        StackTraceElement[] filtered = filter.filter(t, 0);
        
        assertThat(filtered, hasOnlyThoseClasses("org.test.MockitoSampleTest"));
    }
    
    @Test
    public void shouldFilterOutTracesMiddleBadTraces() {
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.test.MockitoSampleTest",
                "org.test.TestSupport",
                "org.mockito.Mockito", 
                "org.test.TestSupport",
                "org.mockito.Mockito"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t, 0);
        
        assertThat(filtered, hasOnlyThoseClasses("org.test.TestSupport", "org.test.MockitoSampleTest"));
    }
    
    @Test
    public void shouldKeepRunners() {
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.mockito.runners.Runner",
                "junit.stuff",
                "org.test.MockitoSampleTest",
                "org.mockito.Mockito"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t, 0);
        
        assertThat(filtered, hasOnlyThoseClasses("org.test.MockitoSampleTest", "junit.stuff", "org.mockito.runners.Runner"));
    }
    
    @Test
    public void shouldKeepInternalRunners() {
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.mockito.internal.runners.Runner",
                "org.test.MockitoSampleTest"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t, 0);
        
        assertThat(filtered, hasOnlyThoseClasses("org.test.MockitoSampleTest", "org.mockito.internal.runners.Runner"));
    }
    
    @Test
    public void shouldStartFilteringFromIndex() {
        //given
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.test.Good",
                "org.mockito.internal.Bad",
                "org.test.MockitoSampleTest"
        ).toTraceArray();
        
        int startIndex = 1;
        
        //when
        StackTraceElement[] filtered = filter.filter(t, startIndex);
        
        //then
        assertThat(filtered, hasOnlyThoseClasses("org.test.Good"));
    }

    @Test
    public void shouldKeepGoodTraceFromTheTopBecauseSpiesSometimesThrowExceptions() {
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.good.Trace",
                "org.yet.another.good.Trace",
                "org.mockito.internal.to.be.Filtered",
                "org.test.MockitoSampleTest"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t, 0);
        
        assertThat(filtered, hasOnlyThoseClasses(
                "org.test.MockitoSampleTest",
                "org.yet.another.good.Trace",
                "org.good.Trace"
                ));
    }
}