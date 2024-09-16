package ch.fhnw.shakethelakebackend.specification;

import ch.fhnw.shakethelakebackend.model.entity.Booking;
import ch.fhnw.shakethelakebackend.model.specification.SpecificationBooking;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SpecificationBookingTest {

    @Mock
    private Root<Booking> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private Path<Object> path;

    @InjectMocks
    private SpecificationBooking specificationBooking;

    @BeforeEach
    void setUp() {
        specificationBooking = new SpecificationBooking("timeSlot.fromTime", ">", ZonedDateTime.now());
    }

    @Test
    void testToPredicateWithGreaterThanOperation() {
        // Given
        LocalTime now = LocalTime.now();
        specificationBooking = new SpecificationBooking("timeSlot.fromTime", ">", now);
        when(root.get(anyString())).thenReturn(path);
        when(path.get(anyString())).thenReturn(path);
        Predicate predicate = mock(Predicate.class);
        when(builder.greaterThan(any(), any(LocalTime.class))).thenReturn(predicate);

        // When
        Predicate result = specificationBooking.toPredicate(root, query, builder);

        // Then
        assertNotNull(result);
        verify(builder, times(1)).greaterThan(any(), eq(now));
    }

    @Test
    void testToPredicateWithGreaterThanOrEqualToOperation() {
        // Given
        LocalTime now = LocalTime.now();
        specificationBooking = new SpecificationBooking("timeSlot.fromTime", ">=", now);
        when(root.get(anyString())).thenReturn(path);
        when(path.get(anyString())).thenReturn(path);
        Predicate predicate = mock(Predicate.class);
        when(builder.greaterThanOrEqualTo(any(), any(LocalTime.class))).thenReturn(predicate);

        // When
        Predicate result = specificationBooking.toPredicate(root, query, builder);

        // Then
        assertNotNull(result);
        verify(builder, times(1)).greaterThanOrEqualTo(any(), eq(now));
    }

    @Test
    void testToPredicateWithLessThanOperation() {
        // Given
        LocalTime now = LocalTime.now();
        specificationBooking = new SpecificationBooking("timeSlot.toTime", "<", now);
        when(root.get(anyString())).thenReturn(path);
        when(path.get(anyString())).thenReturn(path);
        Predicate predicate = mock(Predicate.class);
        when(builder.lessThan(any(), any(LocalTime.class))).thenReturn(predicate);

        // When
        Predicate result = specificationBooking.toPredicate(root, query, builder);

        // Then
        assertNotNull(result);
        verify(builder, times(1)).lessThan(any(), eq(now));
    }

    @Test
    void testToPredicateWithLessThanOrEqualToOperation() {
        // Given
        LocalTime now = LocalTime.now();
        specificationBooking = new SpecificationBooking("timeSlot.toTime", "<=", now);
        when(root.get(anyString())).thenReturn(path);
        when(path.get(anyString())).thenReturn(path);
        Predicate predicate = mock(Predicate.class);
        when(builder.lessThanOrEqualTo(any(), any(LocalTime.class))).thenReturn(predicate);

        // When
        Predicate result = specificationBooking.toPredicate(root, query, builder);

        // Then
        assertNotNull(result);
        verify(builder, times(1)).lessThanOrEqualTo(any(), eq(now));
    }


    @Test
    void testToPredicateWithUnknownOperation() {
        // Given
        specificationBooking = new SpecificationBooking("timeSlot.fromTime", "unknown", LocalTime.now());
        when(root.get(anyString())).thenReturn(path);
        when(path.get(anyString())).thenReturn(path);

        // When
        Predicate result = specificationBooking.toPredicate(root, query, builder);

        // Then
        assertNull(result);
    }
}
