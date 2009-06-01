package org.mockitousage.internal.debugging;

import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockitoutil.TestBase;

public class LocationTest extends TestBase {

    @Test
    public void shouldLocationNotContainGetStackTraceMethod() {
        assertContains("shouldLocationNotContainGetStackTraceMethod", new Location().toString());
    }
}