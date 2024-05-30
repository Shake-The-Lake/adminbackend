package ch.fhnw.shakethelakebackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ExpandHelperTest {
    private final ExpandHelper expandHelper = new ExpandHelper();

    @Test
    void testParseExpandParamWithValidInput() {
        Set<String> expected = new HashSet<>(Arrays.asList("boats", "timeSlots"));
        assertEquals(expected, expandHelper.parseExpandParam("boats,timeSlots"));
    }

    @Test
    void testParseExpandParamWithEmptyInput() {
        assertTrue(expandHelper.parseExpandParam("").isEmpty());
    }

    @Test
    void testParseExpandParamWithNullInput() {
        assertTrue(expandHelper.parseExpandParam(null).isEmpty());
    }

    @Test
    void testParseExpandParamWithSpaces() {
        Set<String> expected = new HashSet<>(Arrays.asList("boats", "timeSlots"));
        assertEquals(expected, expandHelper.parseExpandParam("  boats  ,  timeSlots  "));
    }
    @Test
    void testShouldExpandTrue() {
        Optional<String> expand = Optional.of("boats,timeSlots");
        assertTrue(expandHelper.shouldExpand(expand, "boats"));
    }

    @Test
    void testShouldExpandFalse() {
        Optional<String> expand = Optional.of("boats,timeSlots");
        assertFalse(expandHelper.shouldExpand(expand, "participants"));
    }

    @Test
    void testApplyExpansionFunctionReturnsValue() {
        Optional<String> expand = Optional.of("boats");
        String result = expandHelper.applyExpansion(expand, "boats",
            shouldExpand -> shouldExpand.get() ? "Expanded" : "Not Expanded");
        assertEquals("Expanded", result);
    }

    @Test
    void testApplyExpansionFunctionMultipleFields() {
        Optional<String> expand = Optional.of("boats,crew");
        Set<String> fields = new HashSet<>(Arrays.asList("boats", "crew"));
        String result = expandHelper.applyExpansion(expand, fields,
            shouldExpand -> shouldExpand.get() ? "All Expanded" : "Not All Expanded");
        assertEquals("All Expanded", result);
    }
}
