package io.bootify.gestion_bibliotecas.service;

import io.bootify.gestion_bibliotecas.domain.LibroPrestado;
import io.bootify.gestion_bibliotecas.domain.Prestamo;
import io.bootify.gestion_bibliotecas.model.PrestamoDTO;
import io.bootify.gestion_bibliotecas.repos.LibroPrestadoRepository;
import io.bootify.gestion_bibliotecas.repos.PrestamoRepository;
import io.bootify.gestion_bibliotecas.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroPrestadoRepository libroPrestadoRepository;

    public PrestamoService(final PrestamoRepository prestamoRepository,
            final LibroPrestadoRepository libroPrestadoRepository) {
        this.prestamoRepository = prestamoRepository;
        this.libroPrestadoRepository = libroPrestadoRepository;
    }

    public List<PrestamoDTO> findAll() {
        final List<Prestamo> prestamoes = prestamoRepository.findAll(Sort.by("id"));
        return prestamoes.stream()
                .map(prestamo -> mapToDTO(prestamo, new PrestamoDTO()))
                .toList();
    }

    public PrestamoDTO get(final Long id) {
        return prestamoRepository.findById(id)
                .map(prestamo -> mapToDTO(prestamo, new PrestamoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PrestamoDTO prestamoDTO) {
        final Prestamo prestamo = new Prestamo();
        mapToEntity(prestamoDTO, prestamo);
        return prestamoRepository.save(prestamo).getId();
    }

    public void update(final Long id, final PrestamoDTO prestamoDTO) {
        final Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(prestamoDTO, prestamo);
        prestamoRepository.save(prestamo);
    }

    public void delete(final Long id) {
        prestamoRepository.deleteById(id);
    }

    private PrestamoDTO mapToDTO(final Prestamo prestamo, final PrestamoDTO prestamoDTO) {
        prestamoDTO.setId(prestamo.getId());
        prestamoDTO.setFechaDePrestamo(prestamo.getFechaDePrestamo());
        prestamoDTO.setFechaDeDevolucion(prestamo.getFechaDeDevolucion());
        prestamoDTO.setEstado(prestamo.getEstado());
        prestamoDTO.setLibroPrestado(prestamo.getLibroPrestado() == null ? null : prestamo.getLibroPrestado().getId());
        return prestamoDTO;
    }

    private Prestamo mapToEntity(final PrestamoDTO prestamoDTO, final Prestamo prestamo) {
        prestamo.setFechaDePrestamo(prestamoDTO.getFechaDePrestamo());
        prestamo.setFechaDeDevolucion(prestamoDTO.getFechaDeDevolucion());
        prestamo.setEstado(prestamoDTO.getEstado());
        final LibroPrestado libroPrestado = prestamoDTO.getLibroPrestado() == null ? null : libroPrestadoRepository.findById(prestamoDTO.getLibroPrestado())
                .orElseThrow(() -> new NotFoundException("libroPrestado not found"));
        prestamo.setLibroPrestado(libroPrestado);
        return prestamo;
    }

    public boolean libroPrestadoExists(final Long id) {
        return prestamoRepository.existsByLibroPrestadoId(id);
    }

}
