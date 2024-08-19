package ch.fhnw.shakethelakebackend.mapper;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.SearchParameterDto;
import ch.fhnw.shakethelakebackend.model.entity.LocalizedString;
import ch.fhnw.shakethelakebackend.model.mapper.SearchParameterMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SearchParameterMapperTest {

    private final SearchParameterMapper mapper = Mappers.getMapper(SearchParameterMapper.class);

    @Test
    void testToDtoWithEmptyLists() {
        // Given
        List<BoatDto> boats = List.of();
        List<ActivityTypeDto> activities = List.of();

        // When
        SearchParameterDto result = mapper.toDto(boats, activities);

        // Then
        assertNotNull(result);
        assertEquals(boats, result.getBoats());
        assertEquals(activities, result.getActivityTypes());
    }

    @Test
    void testToDtoWithNonEmptyLists() {
        // Given
        BoatDto boatDto = new BoatDto();
        boatDto.setId(1L);
        boatDto.setName("Boat1");

        ActivityTypeDto activityTypeDto = new ActivityTypeDto();
        activityTypeDto.setId(1L);
        activityTypeDto.setName(new LocalizedString("Activity1", "Activity1", "Activity1"));

        List<BoatDto> boats = List.of(boatDto);
        List<ActivityTypeDto> activities = List.of(activityTypeDto);

        // When
        SearchParameterDto result = mapper.toDto(boats, activities);

        // Then
        assertNotNull(result);
        assertEquals(boats, result.getBoats());
        assertEquals(activities, result.getActivityTypes());
        assertEquals(1, result.getBoats().size());
        assertEquals(1, result.getActivityTypes().size());
        assertEquals("Boat1", result.getBoats().get(0).getName());
        assertEquals("Activity1", result.getActivityTypes().get(0).getName().getEn());
    }

    @Test
    void testToDtoWithMixedLists() {
        // Given
        BoatDto boatDto1 = new BoatDto();
        boatDto1.setId(1L);
        boatDto1.setName("Boat1");

        BoatDto boatDto2 = new BoatDto();
        boatDto2.setId(2L);
        boatDto2.setName("Boat2");

        ActivityTypeDto activityTypeDto1 = new ActivityTypeDto();
        activityTypeDto1.setId(1L);
        activityTypeDto1.setName(new LocalizedString("Activity1", "Activity1", "Activity1"));

        ActivityTypeDto activityTypeDto2 = new ActivityTypeDto();
        activityTypeDto2.setId(2L);
        activityTypeDto2.setName(new LocalizedString("Activity2", "Activity2", "Activity2"));

        List<BoatDto> boats = List.of(boatDto1, boatDto2);
        List<ActivityTypeDto> activities = List.of(activityTypeDto1, activityTypeDto2);

        // When
        SearchParameterDto result = mapper.toDto(boats, activities);

        // Then
        assertNotNull(result);
        assertEquals(boats, result.getBoats());
        assertEquals(activities, result.getActivityTypes());
    }

    @Test
    void testToDtoWithNullLists() {
        // Given
        List<BoatDto> boats = null;
        List<ActivityTypeDto> activities = null;

        // When
        SearchParameterDto result = mapper.toDto(boats, activities);

        // Then
        assertNull(result);
    }

    @Test
    void testToDtoWithNullBoats() {
        // Given
        List<BoatDto> boats = null;
        List<ActivityTypeDto> activities = List.of();

        // When
        SearchParameterDto result = mapper.toDto(boats, activities);

        // Then
        assertNotNull(result);
        assertNull(result.getBoats());
        assertEquals(0, result.getActivityTypes().size());
    }

    @Test
    void testToDtoWithNullActivities() {
        // Given
        List<BoatDto> boats = List.of();
        List<ActivityTypeDto> activities = null;

        // When
        SearchParameterDto result = mapper.toDto(boats, activities);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getBoats().size());
        assertNull(result.getActivityTypes());
    }
}
