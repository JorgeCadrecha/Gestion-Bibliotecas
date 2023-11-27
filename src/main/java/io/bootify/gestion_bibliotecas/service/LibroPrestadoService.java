package io.bootify.gestion_bibliotecas.service;

import io.bootify.gestion_bibliotecas.domain.Lector;
import io.bootify.gestion_bibliotecas.domain.Libro;
import io.bootify.gestion_bibliotecas.domain.LibroPrestado;
import io.bootify.gestion_bibliotecas.domain.Prestamo;
import io.bootify.gestion_bibliotecas.model.LibroPrestadoDTO;
import io.bootify.gestion_bibliotecas.repos.LectorRepository;
import io.bootify.gestion_bibliotecas.repos.LibroPrestadoRepository;
import io.bootify.gestion_bibliotecas.repos.LibroRepository;
import io.bootify.gestion_bibliotecas.repos.PrestamoRepository;
import io.bootify.gestion_bibliotecas.util.NotFoundException;
import io.bootify.gestion_bibliotecas.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LibroPrestadoService {

    private final LibroPrestadoRepository libroPrestadoRepository;
    private final LibroRepository libroRepository;
    private final LectorRepository lectorRepository;
    private final PrestamoRepository prestamoRepository;

    public LibroPrestadoService(final LibroPrestadoRepository libroPrestadoRepository,
            final LibroRepository libroRepository, final LectorRepository lectorRepository,
            final PrestamoRepository prestamoRepository) {
        this.libroPrestadoRepository = libroPrestadoRepository;
        this.libroRepository = libroRepository;
        this.lectorRepository = lectorRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public List<LibroPrestadoDTO> findAll() {
        final List<LibroPrestado> libroPrestadoes = libroPrestadoRepository.findAll(Sort.by("id"));
        return libroPrestadoes.stream()
                .map(libroPrestado -> mapToDTO(libroPrestado, new LibroPrestadoDTO()))
                .toList();
    }

    public LibroPrestadoDTO get(final Long id) {
        return libroPrestadoRepository.findById(id)
                .map(libroPrestado -> mapToDTO(libroPrestado, new LibroPrestadoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final LibroPrestadoDTO libroPrestadoDTO) {
        final LibroPrestado libroPrestado = new LibroPrestado();
        mapToEntity(libroPrestadoDTO, libroPrestado);
        return libroPrestadoRepository.save(libroPrestado).getId();
    }

    public void update(final Long id, final LibroPrestadoDTO libroPrestadoDTO) {
        final LibroPrestado libroPrestado = libroPrestadoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(libroPrestadoDTO, libroPrestado);
        libroPrestadoRepository.save(libroPrestado);
    }

    public void delete(final Long id) {
        libroPrestadoRepository.deleteById(id);
    }

    private LibroPrestadoDTO mapToDTO(final LibroPrestado libroPrestado,
            final LibroPrestadoDTO libroPrestadoDTO) {
        libroPrestadoDTO.setId(libroPrestado.getId());
        libroPrestadoDTO.setIdLibro(libroPrestado.getIdLibro());
        libroPrestadoDTO.setIdLector(libroPrestado.getIdLector());
        libroPrestadoDTO.setIdPrestamo(libroPrestado.getIdPrestamo());
        libroPrestadoDTO.setLibro(libroPrestado.getLibro() == null ? null : libroPrestado.getLibro().getId());
        libroPrestadoDTO.setLector(libroPrestado.getLector() == null ? null : libroPrestado.getLector().getId());
        libroPrestadoDTO.setLibros(libroPrestado.getLibros() == null ? null : libroPrestado.getLibros().getId());
        libroPrestadoDTO.setLectores(libroPrestado.getLectores() == null ? null : libroPrestado.getLectores().getId());
        return libroPrestadoDTO;
    }

    private LibroPrestado mapToEntity(final LibroPrestadoDTO libroPrestadoDTO,
            final LibroPrestado libroPrestado) {
        libroPrestado.setIdLibro(libroPrestadoDTO.getIdLibro());
        libroPrestado.setIdLector(libroPrestadoDTO.getIdLector());
        libroPrestado.setIdPrestamo(libroPrestadoDTO.getIdPrestamo());
        final Libro libro = libroPrestadoDTO.getLibro() == null ? null : libroRepository.findById(libroPrestadoDTO.getLibro())
                .orElseThrow(() -> new NotFoundException("libro not found"));
        libroPrestado.setLibro(libro);
        final Lector lector = libroPrestadoDTO.getLector() == null ? null : lectorRepository.findById(libroPrestadoDTO.getLector())
                .orElseThrow(() -> new NotFoundException("lector not found"));
        libroPrestado.setLector(lector);
        final Libro libros = libroPrestadoDTO.getLibros() == null ? null : libroRepository.findById(libroPrestadoDTO.getLibros())
                .orElseThrow(() -> new NotFoundException("libros not found"));
        libroPrestado.setLibros(libros);
        final Lector lectores = libroPrestadoDTO.getLectores() == null ? null : lectorRepository.findById(libroPrestadoDTO.getLectores())
                .orElseThrow(() -> new NotFoundException("lectores not found"));
        libroPrestado.setLectores(lectores);
        return libroPrestado;
    }

    public String getReferencedWarning(final Long id) {
        final LibroPrestado libroPrestado = libroPrestadoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Prestamo libroPrestadoPrestamo = prestamoRepository.findFirstByLibroPrestado(libroPrestado);
        if (libroPrestadoPrestamo != null) {
            return WebUtils.getMessage("libroPrestado.prestamo.libroPrestado.referenced", libroPrestadoPrestamo.getId());
        }
        return null;
    }

}
