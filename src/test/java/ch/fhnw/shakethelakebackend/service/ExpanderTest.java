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
class ExpanderTest {
    private final Expander expander = new Expander();

    @Test
    void testParseExpandParamWithValidInput() {
        Set<String> expected = new HashSet<>(Arrays.asList("boats", "timeSlots"));
        assertEquals(expected, expander.parseExpandParam("boats,timeSlots"));
    }

    @Test
    void testParseExpandParamWithEmptyInput() {
        assertTrue(expander.parseExpandParam("").isEmpty());
    }

    @Test
    void testParseExpandParamWithNullInput() {
        assertTrue(expander.parseExpandParam(null).isEmpty());
    }

    @Test
    void testParseExpandParamWithSpaces() {
        Set<String> expected = new HashSet<>(Arrays.asList("boats", "timeSlots"));
        assertEquals(expected, expander.parseExpandParam("  boats  ,  timeSlots  "));
    }

    @Test
    void testShouldExpandTrue() {
        Optional<String> expand = Optional.of("boats,timeSlots");
        assertTrue(expander.shouldExpand(expand, "boats"));
    }

    @Test
    void testShouldExpandFalse() {
        Optional<String> expand = Optional.of("boats,timeSlots");
        assertFalse(expander.shouldExpand(expand, "participants"));
    }

}
