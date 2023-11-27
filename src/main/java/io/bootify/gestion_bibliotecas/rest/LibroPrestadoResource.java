package io.bootify.gestion_bibliotecas.rest;

import io.bootify.gestion_bibliotecas.model.LibroPrestadoDTO;
import io.bootify.gestion_bibliotecas.service.LibroPrestadoService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/libroPrestados", produces = MediaType.APPLICATION_JSON_VALUE)
public class LibroPrestadoResource {

    private final LibroPrestadoService libroPrestadoService;

    public LibroPrestadoResource(final LibroPrestadoService libroPrestadoService) {
        this.libroPrestadoService = libroPrestadoService;
    }

    @GetMapping
    public ResponseEntity<List<LibroPrestadoDTO>> getAllLibroPrestados() {
        return ResponseEntity.ok(libroPrestadoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroPrestadoDTO> getLibroPrestado(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(libroPrestadoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createLibroPrestado(
            @RequestBody @Valid final LibroPrestadoDTO libroPrestadoDTO) {
        final Long createdId = libroPrestadoService.create(libroPrestadoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateLibroPrestado(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final LibroPrestadoDTO libroPrestadoDTO) {
        libroPrestadoService.update(id, libroPrestadoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteLibroPrestado(@PathVariable(name = "id") final Long id) {
        libroPrestadoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
