package ch.fhnw.shakethelakebackend.service;

import ch.fhnw.shakethelakebackend.model.dto.IconDto;
import ch.fhnw.shakethelakebackend.model.entity.Icon;
import ch.fhnw.shakethelakebackend.model.mapper.IconMapper;
import ch.fhnw.shakethelakebackend.model.repository.IconRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class IconService {

    @Value("classpath:icons/")
    private Resource iconsDirectory;

    private final IconRepository iconRepository;
    private final IconMapper iconMapper;

    public byte[] getIcon(String iconName) {
        Icon icon = iconRepository.findByName(iconName).orElseThrow(() ->
            new EntityNotFoundException("Icon not found"));
        return icon.getPng();
    }
    public Icon getIcon(Long id) {
        return iconRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Icon not found"));
    }

    public List<IconDto> getAllIcons() {
        return iconRepository.findAll().stream().map(iconMapper::toDto).toList();
    }

    public Map<String, List<IconDto>> getAllIconsGroupedByDescription() {
        return this.getAllIcons().stream().collect(groupingBy(IconDto::getDescription));
    }

    @PostConstruct
    public void loadAllIcons() throws IOException {
        try (Stream<Path> paths = Files.walk(iconsDirectory.getFile().toPath())) {
            paths.filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".png"))
                .forEach(path -> {
                    try {
                        Icon icon = Icon.builder()
                            .name(path.getFileName().toString())
                            .description(path.getName(path.getNameCount() - 2).toString())
                            .png(Files.readAllBytes(path))
                            .build();
                        iconRepository.save(icon);
                    } catch (IOException e) {
                        System.out.println("Error reading icon file: " + e.getMessage());
                    }
                });
        }
    }

}
