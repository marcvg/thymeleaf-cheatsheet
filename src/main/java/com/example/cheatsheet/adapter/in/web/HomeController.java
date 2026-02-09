package com.example.cheatsheet.adapter.in.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        addCommonAttributes(model);
        return "home";
    }

    @PostMapping("/")
    public String handlePost(@ModelAttribute SearchFilter searchFilter, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("toastMessage",
                "Filter applied for: " + searchFilter.getFirstName() + " " + searchFilter.getLastName());
        return "redirect:/";
    }

    private void addCommonAttributes(Model model) {
        model.addAttribute("title", "Thymeleaf & Pico.css Cheat Sheet");
        model.addAttribute("userActive", true);
        model.addAttribute("searchFilter", new SearchFilter("John", "Doe", ""));
        model.addAttribute("customers", aCustomerList());
        model.addAttribute("todaysDate", LocalDate.now());

        // For switch/case demo
        model.addAttribute("orderStatus", "SHIPPED");

        // For elvis operator demo (nullable)
        model.addAttribute("nickname", null);
        model.addAttribute("firstName", "John");
        model.addAttribute("lastName", "Doe");

        // For dynamic attributes demo
        model.addAttribute("progress", 75);
        model.addAttribute("highlightRow", true);
    }

    List<Customer> aCustomerList() {
        return List.of(
                new Customer("John", "Doe", "Lost street", "122", true),
                new Customer("Jane", "Doe", "Corner street", "242", false),
                new Customer("Alex", "Agnew", "Comedy street", "701", true),
                new Customer("Jennifer", "Lawerence", "Mockingbird street", "5000", true));
    }

    record Customer(String firstName, String lastName, String street, String houseNumber, boolean active) {
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchFilter {
        private String firstName;
        private String lastName;
        private String streetName;
    }
}
