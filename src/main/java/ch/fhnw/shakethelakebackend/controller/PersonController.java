package ch.fhnw.shakethelakebackend.controller;

import ch.fhnw.shakethelakebackend.model.dto.CreatePersonDto;
import ch.fhnw.shakethelakebackend.model.dto.PersonDto;
import ch.fhnw.shakethelakebackend.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/person")
@Validated
public class PersonController {
    private final PersonService personService;

    @Operation(summary = "Get a person by id", description = "Returns a person as per the id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved a person by id"),
        @ApiResponse(responseCode = "404", description = "Not found - The person was not found")
    })
    @GetMapping("/{id}")
    public PersonDto getPerson(@PathVariable Long id) {
        return personService.getPersonDto(id);
    }

    @Operation(summary = "Create a person", description = "Creates a person")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successfully created a person")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public PersonDto createPerson(@Valid @RequestBody CreatePersonDto createPersonDto) {
        return personService.createPerson(createPersonDto);
    }

    @Operation(summary = "Update a person by id", description = "Updates a person as per the id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated a person by id"),
        @ApiResponse(responseCode = "404", description = "Not found - The person was not found")
    })
    @PutMapping("/{id}")
    public PersonDto updatePerson(@PathVariable Long id, @RequestBody @Valid CreatePersonDto createPersonDto) {
        return personService.updatePerson(id, createPersonDto);
    }


    @Operation(summary = "Delete a person by id", description = "Deletes a person as per the id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted a person by id"),
        @ApiResponse(responseCode = "404", description = "Not found - The person was not found")
    })
    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
    }


    @Operation(summary = "Get all persons", description = "Returns all persons")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all persons")
    })
    @GetMapping()
    public List<PersonDto> getAllPersons() {
        return personService.getAllPersonsDto();
    }
}
