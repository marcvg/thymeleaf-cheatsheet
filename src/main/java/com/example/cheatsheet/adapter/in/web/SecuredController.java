package com.example.cheatsheet.adapter.in.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

/**
 * Thymeleaf page protected by Azure AD SSO (App Reg 2).
 * Spring Security redirects unauthenticated requests straight to Azure AD login.
 */
@Controller
public class SecuredController {

    @GetMapping("/secured")
    public String secured(Model model, @AuthenticationPrincipal OidcUser principal) {
        model.addAttribute("username",
            Optional.ofNullable(principal.getPreferredUsername())
                    .orElse(principal.getName()));
        model.addAttribute("fullName", principal.getFullName());
        model.addAttribute("email", principal.getEmail());
        return "secured";
    }
}
