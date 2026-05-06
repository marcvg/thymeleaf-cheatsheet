package com.example.cheatsheet.adapter.in.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * Simple REST endpoint secured with Azure AD Bearer token (App Reg 1).
 *
 * To call this endpoint you need a valid access token issued for:
 *   spring.cloud.azure.active-directory.app-id-uri  (e.g. api://<rest-api-client-id>)
 *
 * Example with Azure CLI:
 *   token=$(az account get-access-token --resource api://<rest-api-client-id> --query accessToken -o tsv)
 *   curl -H "Authorization: Bearer $token" http://localhost:8080/api/hello
 */
@RestController
@RequestMapping("/api")
public class HelloRestController {

    @GetMapping("/hello")
    public Map<String, String> hello(@AuthenticationPrincipal Jwt jwt) {
        String name = Optional.ofNullable(jwt.getClaimAsString("name"))
                              .orElse(jwt.getSubject());
        return Map.of(
            "message", "Hello, " + name + "!",
            "subject", jwt.getSubject()
        );
    }
}
