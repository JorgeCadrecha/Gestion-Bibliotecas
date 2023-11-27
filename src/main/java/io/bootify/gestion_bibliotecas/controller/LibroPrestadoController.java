package io.bootify.gestion_bibliotecas.controller;

import io.bootify.gestion_bibliotecas.domain.Lector;
import io.bootify.gestion_bibliotecas.domain.Libro;
import io.bootify.gestion_bibliotecas.model.LibroPrestadoDTO;
import io.bootify.gestion_bibliotecas.repos.LectorRepository;
import io.bootify.gestion_bibliotecas.repos.LibroRepository;
import io.bootify.gestion_bibliotecas.service.LibroPrestadoService;
import io.bootify.gestion_bibliotecas.util.CustomCollectors;
import io.bootify.gestion_bibliotecas.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/libroPrestados")
public class LibroPrestadoController {

    private final LibroPrestadoService libroPrestadoService;
    private final LibroRepository libroRepository;
    private final LectorRepository lectorRepository;

    public LibroPrestadoController(final LibroPrestadoService libroPrestadoService,
            final LibroRepository libroRepository, final LectorRepository lectorRepository) {
        this.libroPrestadoService = libroPrestadoService;
        this.libroRepository = libroRepository;
        this.lectorRepository = lectorRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("libroValues", libroRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Libro::getId, Libro::getTitulo)));
        model.addAttribute("lectorValues", lectorRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Lector::getId, Lector::getNombre)));
        model.addAttribute("librosValues", libroRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Libro::getId, Libro::getTitulo)));
        model.addAttribute("lectoresValues", lectorRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Lector::getId, Lector::getNombre)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("libroPrestadoes", libroPrestadoService.findAll());
        return "libroPrestado/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("libroPrestado") final LibroPrestadoDTO libroPrestadoDTO) {
        return "libroPrestado/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("libroPrestado") @Valid final LibroPrestadoDTO libroPrestadoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "libroPrestado/add";
        }
        libroPrestadoService.create(libroPrestadoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("libroPrestado.create.success"));
        return "redirect:/libroPrestados";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("libroPrestado", libroPrestadoService.get(id));
        return "libroPrestado/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("libroPrestado") @Valid final LibroPrestadoDTO libroPrestadoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "libroPrestado/edit";
        }
        libroPrestadoService.update(id, libroPrestadoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("libroPrestado.update.success"));
        return "redirect:/libroPrestados";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = libroPrestadoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            libroPrestadoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("libroPrestado.delete.success"));
        }
        return "redirect:/libroPrestados";
    }

}
