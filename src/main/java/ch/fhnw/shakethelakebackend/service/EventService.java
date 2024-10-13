package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.ActivityTypeDto;
import ch.fhnw.shakethelakebackend.model.dto.BoatDto;
import ch.fhnw.shakethelakebackend.model.dto.CreateEventDto;
import ch.fhnw.shakethelakebackend.model.dto.EventDto;
import ch.fhnw.shakethelakebackend.model.entity.Event;
import ch.fhnw.shakethelakebackend.model.mapper.ActivityTypeMapper;
import ch.fhnw.shakethelakebackend.model.mapper.BoatMapper;
import ch.fhnw.shakethelakebackend.model.mapper.EventMapper;
import ch.fhnw.shakethelakebackend.model.repository.EventRepository;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService {
    public static final String EVENT_NOT_FOUND = "Event not found";
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final BoatMapper boatMapper;
    private final ActivityTypeMapper activityTypeMapper;
    private final Expander expander;
    private final JwtService jwtService;

    public EventDto getEventDto(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND));
        return eventMapper.toDto(event);
    }

    public Event getEvent(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND));
    }

    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream().map(eventMapper::toDto).toList();
    }

    public List<Event> getAllEventEntities() {
        return eventRepository.findAll();
    }

    public EventDto createEvent(CreateEventDto createEventDto) {
        //TODO; location not mvp
        // Location location = locationRepository.save(createEventDto.getLocationId());
        Event event = eventMapper.toEntity(createEventDto);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        eventRepository.save(event);
        Date start = Date.from(event.getDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(event.getDate().atStartOfDay().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        String EmployeeCode = event.getId() + "-" + jwtService.generateToken("employee", start, end);
        String CustomerCode = event.getId() + "-" + jwtService.generateToken("customer", start, end);
        event.setEmployeeCode(EmployeeCode);
        event.setCustomerCode(CustomerCode);
        try {

            BitMatrix bitMatrix = qrCodeWriter.encode(event.getEmployeeCode(), BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
            event.setEmployeeBarcode(Base64.getEncoder().encodeToString(bos.toByteArray()));

            bitMatrix = qrCodeWriter.encode(event.getCustomerCode(), BarcodeFormat.QR_CODE, 200, 200);
            bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
            event.setCustomerBarcode(Base64.getEncoder().encodeToString(bos.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    public EventDto updateEvent(Long id, CreateEventDto createEventDto) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException(EVENT_NOT_FOUND);
        }

        Event oldEvent = getEvent(id);
        Event updateEvent = eventMapper.toEntity(createEventDto);
        updateEvent.setId(id);
        updateEvent.setBoats(oldEvent.getBoats());
        updateEvent.setActivityTypes(oldEvent.getActivityTypes());
        eventRepository.save(updateEvent);
        return eventMapper.toDto(updateEvent);

    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND));
        eventRepository.delete(event);
    }

    public EventDto getEventWithDetails(Long id, Optional<String> expand) {
        Event event = getEvent(id);
        EventDto eventDto = eventMapper.toDto(event);

        expander.applyExpansion(expand, "boats", () -> {

            List<BoatDto> boatDtos = event.getBoats().stream().map(boatMapper::toDto).toList();
            eventDto.setBoats(boatDtos);

        });
        expander.applyExpansion(expand, "boats.timeSlots", () -> {

            List<BoatDto> boatDtos = event.getBoats().stream().map(boatMapper::toDtoWithTimeSlots).toList();
            eventDto.setBoats(boatDtos);

        });
        expander.applyExpansion(expand, "activityTypes", () -> {

            List<ActivityTypeDto> activityTypeDtos = event.getActivityTypes().stream().map(activityTypeMapper::toDto)
                    .toList();
            eventDto.setActivityTypes(activityTypeDtos);

        });

        return eventDto;
    }
}
