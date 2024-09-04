package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.IconDto;
import ch.fhnw.shakethelakebackend.service.IconService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/icon")
public class IconController {
    private final IconService iconService;

    @GetMapping()
    public Map<String, List<IconDto>> getAllIconBytes() {
        return iconService.getAllIconsGroupedByDescription();
    }
}
