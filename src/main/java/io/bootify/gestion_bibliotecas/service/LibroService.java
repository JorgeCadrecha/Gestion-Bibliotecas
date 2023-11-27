package io.bootify.gestion_bibliotecas.service;

import io.bootify.gestion_bibliotecas.domain.Libro;
import io.bootify.gestion_bibliotecas.domain.LibroPrestado;
import io.bootify.gestion_bibliotecas.model.LibroDTO;
import io.bootify.gestion_bibliotecas.repos.LibroPrestadoRepository;
import io.bootify.gestion_bibliotecas.repos.LibroRepository;
import io.bootify.gestion_bibliotecas.util.NotFoundException;
import io.bootify.gestion_bibliotecas.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final LibroPrestadoRepository libroPrestadoRepository;

    public LibroService(final LibroRepository libroRepository,
            final LibroPrestadoRepository libroPrestadoRepository) {
        this.libroRepository = libroRepository;
        this.libroPrestadoRepository = libroPrestadoRepository;
    }

    public List<LibroDTO> findAll() {
        final List<Libro> libroes = libroRepository.findAll(Sort.by("id"));
        return libroes.stream()
                .map(libro -> mapToDTO(libro, new LibroDTO()))
                .toList();
    }

    public LibroDTO get(final Long id) {
        return libroRepository.findById(id)
                .map(libro -> mapToDTO(libro, new LibroDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final LibroDTO libroDTO) {
        final Libro libro = new Libro();
        mapToEntity(libroDTO, libro);
        return libroRepository.save(libro).getId();
    }

    public void update(final Long id, final LibroDTO libroDTO) {
        final Libro libro = libroRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(libroDTO, libro);
        libroRepository.save(libro);
    }

    public void delete(final Long id) {
        libroRepository.deleteById(id);
    }

    private LibroDTO mapToDTO(final Libro libro, final LibroDTO libroDTO) {
        libroDTO.setId(libro.getId());
        libroDTO.setTitulo(libro.getTitulo());
        libroDTO.setAutor(libro.getAutor());
        libroDTO.setCuandoSePublico(libro.getCuandoSePublico());
        return libroDTO;
    }

    private Libro mapToEntity(final LibroDTO libroDTO, final Libro libro) {
        libro.setTitulo(libroDTO.getTitulo());
        libro.setAutor(libroDTO.getAutor());
        libro.setCuandoSePublico(libroDTO.getCuandoSePublico());
        return libro;
    }

    public String getReferencedWarning(final Long id) {
        final Libro libro = libroRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final LibroPrestado libroLibroPrestado = libroPrestadoRepository.findFirstByLibro(libro);
        if (libroLibroPrestado != null) {
            return WebUtils.getMessage("libro.libroPrestado.libro.referenced", libroLibroPrestado.getId());
        }
        final LibroPrestado librosLibroPrestado = libroPrestadoRepository.findFirstByLibros(libro);
        if (librosLibroPrestado != null) {
            return WebUtils.getMessage("libro.libroPrestado.libros.referenced", librosLibroPrestado.getId());
        }
        return null;
    }

}
