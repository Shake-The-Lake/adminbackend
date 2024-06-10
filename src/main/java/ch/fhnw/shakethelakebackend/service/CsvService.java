package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.TimeSlotDto;
import ch.fhnw.shakethelakebackend.model.entity.Boat;
import ch.fhnw.shakethelakebackend.model.mapper.TimeSlotMapper;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;

@AllArgsConstructor
@Service
public class CsvService {

    private final TimeSlotMapper timeSlotMapper;
    private final BoatService boatService;

    public ResponseEntity<String> exportTimeSlotsFromBoat(Long boatId, String filename) {
        Boat boat = boatService.getBoat(boatId);
        List<TimeSlotDto> timeSlotDtos = boat.getTimeSlots().stream().map(timeSlotMapper::toDtoWithBoatName)
            .toList();
        return buildCsv(timeSlotDtos, filename);

    }

    private <T> ResponseEntity<String> buildCsv(List<T> entityList, String filename) {
        try (StringWriter sw = new StringWriter()) {
            var builder = new StatefulBeanToCsvBuilder<T>(sw)
                .withQuotechar(ICSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(';')
                .build();

            builder.write(entityList);
            String csvContent = sw.toString();
            System.out.println("CSV content: " + csvContent);
            if (csvContent.isBlank()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentType(MediaType.parseMediaType("application/csv"));

            return ResponseEntity.ok().headers(headers).body(csvContent);
        } catch (Exception e) {
            System.out.println("Error while building CSV: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
