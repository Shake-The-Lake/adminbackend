package ch.fhnw.shakethelakebackend.mapper;

import ch.fhnw.shakethelakebackend.model.dto.CreateEventDto;
import ch.fhnw.shakethelakebackend.model.dto.EventDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.mapper.EventMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EventMapperTest {
    private EventMapper mapper = Mappers.getMapper(EventMapper.class);

    @Test
    void testCreateEventDtoToEventMapping() {
        CreateEventDto dto = new CreateEventDto();
        // Populate dto fields as necessary

        Event event = mapper.toEntity(dto);

        assertNotNull(event);
        // Add assertions to verify that ignored fields are not mapped
    }

    @Test
    void testEventToEventDtoMapping() {
        Event event = new Event();
        Set<ActivityType> types = new HashSet<>();
        ActivityType type = new ActivityType();
        type.setId(1L);
        types.add(type);
        event.setActivityTypes(types);

        EventDto dto = mapper.toDto(event);

        assertNotNull(dto);
        assertTrue(dto.getActivityTypeIds().contains(1L));
        assertEquals(1, dto.getActivityTypeIds().size());
    }
}
