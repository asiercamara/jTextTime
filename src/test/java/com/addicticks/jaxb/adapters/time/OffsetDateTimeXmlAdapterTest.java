/*
 * Copyright 2018 Addicticks.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.addicticks.jaxb.adapters.time;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OffsetDateTimeXmlAdapterTest {
    
    @Test
    public void testUnmarshal() {
        System.out.println("unmarshal");
        OffsetDateTimeXmlAdapter instance = new OffsetDateTimeXmlAdapter() {
            @Override
            public ZoneOffset getCurrentZoneOffset() {
                return ZoneOffset.ofTotalSeconds(13549);  // +03:45:49
            }
        };
        
        OffsetDateTime result;
        
        
        // More than 9 digits after the decimal point in the seconds 
        // element will throw error. This is ok.
        try {
            result = instance.unmarshal("2018-03-14T23:30:28.123456789012345678901234567890Z");
        } catch (Exception ex) {
            assertTrue(ex instanceof DateTimeParseException);
        }
        
        
        result = instance.unmarshal("2018-03-14T23:30:28.123456789Z");
        assertEquals(2018, result.getYear());
        assertEquals(3, result.getMonthValue());
        assertEquals(14, result.getDayOfMonth());
        assertEquals(23, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(28, result.getSecond());
        assertEquals(123456789, result.getNano());
        assertEquals(0, result.getOffset().getTotalSeconds());
        
        result = instance.unmarshal("2018-03-14T23:30:28.123456789-07:30");
        assertEquals(2018, result.getYear());
        assertEquals(3, result.getMonthValue());
        assertEquals(14, result.getDayOfMonth());
        assertEquals(23, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(28, result.getSecond());
        assertEquals(123456789, result.getNano());
        assertEquals(-((7*60*60) + (30*60)), result.getOffset().getTotalSeconds());
        
        result = instance.unmarshal("2018-03-14T23:30:28.123456789");
        assertEquals(2018, result.getYear());
        assertEquals(3, result.getMonthValue());
        assertEquals(14, result.getDayOfMonth());
        assertEquals(23, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(28, result.getSecond());
        assertEquals(123456789, result.getNano());
        assertEquals(13549, result.getOffset().getTotalSeconds());
        
    }

    @Test
    public void testMarshal() {
        System.out.println("marshal");
        OffsetDateTimeXmlAdapter instance = new OffsetDateTimeXmlAdapter();
        OffsetDateTime t;
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals("2018-03-14T23:30:28.123456789Z", instance.marshal(t));
        
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 0, ZoneOffset.UTC);
        assertEquals("2018-03-14T23:30:28Z", instance.marshal(t));
        
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 0, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals("2018-03-14T23:30:00+02:00", instance.marshal(t));
    }
    
}
