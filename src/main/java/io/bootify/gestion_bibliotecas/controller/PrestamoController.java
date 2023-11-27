package io.bootify.gestion_bibliotecas.controller;

import io.bootify.gestion_bibliotecas.domain.LibroPrestado;
import io.bootify.gestion_bibliotecas.model.PrestamoDTO;
import io.bootify.gestion_bibliotecas.repos.LibroPrestadoRepository;
import io.bootify.gestion_bibliotecas.service.PrestamoService;
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
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;
    private final LibroPrestadoRepository libroPrestadoRepository;

    public PrestamoController(final PrestamoService prestamoService,
            final LibroPrestadoRepository libroPrestadoRepository) {
        this.prestamoService = prestamoService;
        this.libroPrestadoRepository = libroPrestadoRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("libroPrestadoValues", libroPrestadoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(LibroPrestado::getId, LibroPrestado::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("prestamoes", prestamoService.findAll());
        return "prestamo/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("prestamo") final PrestamoDTO prestamoDTO) {
        return "prestamo/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("prestamo") @Valid final PrestamoDTO prestamoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("libroPrestado") && prestamoDTO.getLibroPrestado() != null && prestamoService.libroPrestadoExists(prestamoDTO.getLibroPrestado())) {
            bindingResult.rejectValue("libroPrestado", "Exists.prestamo.libroPrestado");
        }
        if (bindingResult.hasErrors()) {
            return "prestamo/add";
        }
        prestamoService.create(prestamoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("prestamo.create.success"));
        return "redirect:/prestamos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("prestamo", prestamoService.get(id));
        return "prestamo/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("prestamo") @Valid final PrestamoDTO prestamoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final PrestamoDTO currentPrestamoDTO = prestamoService.get(id);
        if (!bindingResult.hasFieldErrors("libroPrestado") && prestamoDTO.getLibroPrestado() != null &&
                !prestamoDTO.getLibroPrestado().equals(currentPrestamoDTO.getLibroPrestado()) &&
                prestamoService.libroPrestadoExists(prestamoDTO.getLibroPrestado())) {
            bindingResult.rejectValue("libroPrestado", "Exists.prestamo.libroPrestado");
        }
        if (bindingResult.hasErrors()) {
            return "prestamo/edit";
        }
        prestamoService.update(id, prestamoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("prestamo.update.success"));
        return "redirect:/prestamos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        prestamoService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("prestamo.delete.success"));
        return "redirect:/prestamos";
    }

}
