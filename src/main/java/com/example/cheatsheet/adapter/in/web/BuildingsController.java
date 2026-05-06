package com.example.cheatsheet.adapter.in.web;

import com.example.cheatsheet.domain.Address;
import com.example.cheatsheet.domain.Asset;
import com.example.cheatsheet.domain.AssetRepository;
import com.example.cheatsheet.domain.BuildingInfo;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ui/buildings")
public class BuildingsController {

    private final AssetRepository repository;

    public BuildingsController(AssetRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public String view(@RequestParam(required = false) Long selected, Model model) {
        return showPage(selected, model);
    }

    @PostMapping("/{id}/address")
    public String saveAddress(@PathVariable Long id,
                              @Valid @ModelAttribute("addressForm") Address addressForm,
                              BindingResult bindingResult,
                              RedirectAttributes flash,
                              Model model) {
        if (bindingResult.hasErrors()) {
            // Re-render with the failed form preserved (Spring keeps it + the BindingResult
            // in the model). showPage() seeds the OTHER pane from canonical data.
            return showPage(id, model);
        }
        repository.updateAddress(id, addressForm);
        flash.addFlashAttribute("toastMessage", "Address saved.");
        return "redirect:/ui/buildings?selected=" + id;
    }

    @PostMapping("/{id}/info")
    public String saveInfo(@PathVariable Long id,
                           @Valid @ModelAttribute("buildingInfoForm") BuildingInfo infoForm,
                           BindingResult bindingResult,
                           RedirectAttributes flash,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return showPage(id, model);
        }
        repository.updateInfo(id, infoForm);
        flash.addFlashAttribute("toastMessage", "Building info saved.");
        return "redirect:/ui/buildings?selected=" + id;
    }

    @PostMapping("/{id}/deactivate")
    public String deactivate(@PathVariable Long id, RedirectAttributes flash) {
        try {
            repository.deactivate(id);
            flash.addFlashAttribute("toastMessage", "Building deactivated.");
        } catch (IllegalStateException ex) {
            flash.addFlashAttribute("toastError", ex.getMessage());
        }
        return "redirect:/ui/buildings?selected=" + id;
    }

    private String showPage(Long selectedId, Model model) {
        List<Asset> all = repository.findAll();
        Asset current = (selectedId == null
                ? all.stream().findFirst()
                : repository.findById(selectedId).or(() -> all.stream().findFirst()))
                .orElseThrow();

        model.addAttribute("assets", all);
        model.addAttribute("current", current);
        model.addAttribute("selectedId", current.getId());

        // Seed forms only if not already present — preserves submitted-but-failed input.
        if (!model.containsAttribute("addressForm")) {
            model.addAttribute("addressForm", new Address(current.getAddress()));
        }
        if (!model.containsAttribute("buildingInfoForm")) {
            model.addAttribute("buildingInfoForm", new BuildingInfo(current.getBuildingInfo()));
        }
        return "buildings";
    }
}
