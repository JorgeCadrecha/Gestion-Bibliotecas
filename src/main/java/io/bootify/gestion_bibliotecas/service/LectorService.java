package io.bootify.gestion_bibliotecas.service;

import io.bootify.gestion_bibliotecas.domain.Lector;
import io.bootify.gestion_bibliotecas.domain.LibroPrestado;
import io.bootify.gestion_bibliotecas.model.LectorDTO;
import io.bootify.gestion_bibliotecas.repos.LectorRepository;
import io.bootify.gestion_bibliotecas.repos.LibroPrestadoRepository;
import io.bootify.gestion_bibliotecas.util.NotFoundException;
import io.bootify.gestion_bibliotecas.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class LectorService {

    private final LectorRepository lectorRepository;
    private final LibroPrestadoRepository libroPrestadoRepository;

    public LectorService(final LectorRepository lectorRepository,
            final LibroPrestadoRepository libroPrestadoRepository) {
        this.lectorRepository = lectorRepository;
        this.libroPrestadoRepository = libroPrestadoRepository;
    }

    public List<LectorDTO> findAll() {
        final List<Lector> lectors = lectorRepository.findAll(Sort.by("id"));
        return lectors.stream()
                .map(lector -> mapToDTO(lector, new LectorDTO()))
                .toList();
    }

    public LectorDTO get(final Long id) {
        return lectorRepository.findById(id)
                .map(lector -> mapToDTO(lector, new LectorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final LectorDTO lectorDTO) {
        final Lector lector = new Lector();
        mapToEntity(lectorDTO, lector);
        return lectorRepository.save(lector).getId();
    }

    public void update(final Long id, final LectorDTO lectorDTO) {
        final Lector lector = lectorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(lectorDTO, lector);
        lectorRepository.save(lector);
    }

    public void delete(final Long id) {
        lectorRepository.deleteById(id);
    }

    private LectorDTO mapToDTO(final Lector lector, final LectorDTO lectorDTO) {
        lectorDTO.setId(lector.getId());
        lectorDTO.setNombre(lector.getNombre());
        lectorDTO.setApellido(lector.getApellido());
        lectorDTO.setLibro(lector.getLibro());
        lectorDTO.setCorreo(lector.getCorreo());
        return lectorDTO;
    }

    private Lector mapToEntity(final LectorDTO lectorDTO, final Lector lector) {
        lector.setNombre(lectorDTO.getNombre());
        lector.setApellido(lectorDTO.getApellido());
        lector.setLibro(lectorDTO.getLibro());
        lector.setCorreo(lectorDTO.getCorreo());
        return lector;
    }

    public String getReferencedWarning(final Long id) {
        final Lector lector = lectorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final LibroPrestado lectorLibroPrestado = libroPrestadoRepository.findFirstByLector(lector);
        if (lectorLibroPrestado != null) {
            return WebUtils.getMessage("lector.libroPrestado.lector.referenced", lectorLibroPrestado.getId());
        }
        final LibroPrestado lectoresLibroPrestado = libroPrestadoRepository.findFirstByLectores(lector);
        if (lectoresLibroPrestado != null) {
            return WebUtils.getMessage("lector.libroPrestado.lectores.referenced", lectoresLibroPrestado.getId());
        }
        return null;
    }

}
