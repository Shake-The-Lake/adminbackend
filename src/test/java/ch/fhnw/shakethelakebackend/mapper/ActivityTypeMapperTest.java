package ch.fhnw.shakethelakebackend.mapper;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.entity.ActivityType;
import ch.fhnw.shakethelakebackend.model.mapper.ActivityTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ActivityTypeMapperTest {
    @InjectMocks
    private ActivityTypeMapper mapper = Mappers.getMapper(ActivityTypeMapper.class);

    private ActivityType activityType;
    private ActivityTypeDto activityTypeDto;
    private CreateActivityTypeDto createActivityTypeDto;

    @BeforeEach
    void setUp() {
        // Initialize your entities and DTOs
        activityType = new ActivityType();
        activityType.setId(1L); // Assuming an ID field

        activityTypeDto = new ActivityTypeDto();
        activityTypeDto.setId(1L);

        createActivityTypeDto = new CreateActivityTypeDto();
    }

    @Test
    void testToEntity() {
        ActivityType result = mapper.toEntity(createActivityTypeDto);
        assertEquals(createActivityTypeDto.getName(), result.getName());
    }

    @Test
    void testToDto() {
        ActivityTypeDto result = mapper.toDto(activityType);
        assertEquals(activityType.getId(), result.getId());
    }
}
