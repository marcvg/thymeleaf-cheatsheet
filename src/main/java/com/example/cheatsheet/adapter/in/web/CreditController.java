package com.example.cheatsheet.adapter.in.web;

import com.example.cheatsheet.domain.CreditDetail;
import com.example.cheatsheet.domain.CreditDocument;
import com.example.cheatsheet.domain.CreditFinancialInfo;
import com.example.cheatsheet.domain.CreditTask;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/ui/credit")
public class CreditController {

    public enum View {
        DETAIL("detail"),
        FINANCIELE_INFO("financiele-info"),
        DOCUMENTEN("documenten"),
        TAKEN("taken");

        private final String slug;
        View(String slug) { this.slug = slug; }
        public String getSlug() { return slug; }

        static View fromSlug(String s) {
            if (s == null) return DETAIL;
            for (View v : values()) {
                if (v.slug.equalsIgnoreCase(s)) return v;
            }
            return DETAIL;
        }
    }

    private final LocaleResolver localeResolver;

    public CreditController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @GetMapping
    public String view(@RequestParam(required = false, defaultValue = "detail") String view,
                       @RequestParam(required = false, defaultValue = "000-0000000-00") String productId,
                       @RequestParam(required = false, defaultValue = "P-0001") String personId,
                       @RequestParam(required = false, defaultValue = "nl") String language,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       Model model) {

        // Caller is the SPA — each request is independent, so the language param
        // drives the response locale rather than the user's session preference.
        localeResolver.setLocale(request, response, Locale.forLanguageTag(language));

        View activeView = View.fromSlug(view);

        model.addAttribute("productId", productId);
        model.addAttribute("personId", personId);
        model.addAttribute("language", language);

        // Each view fetches its own slice of data — the only thing they share is
        // the (productId, personId) key. Real lookups would happen here.
        switch (activeView) {
            case DETAIL -> model.addAttribute("credit", sampleCredit(productId));
            case FINANCIELE_INFO -> model.addAttribute("financial", sampleFinancial(productId));
            case DOCUMENTEN -> model.addAttribute("documents", sampleDocuments(productId));
            case TAKEN -> model.addAttribute("tasks", sampleTasks(productId));
        }

        return "credit/" + activeView.getSlug();
    }

    private CreditDetail sampleCredit(String productId) {
        return new CreditDetail(
                productId,
                LocalDate.of(2019, 5, 27),
                new BigDecimal("80000.00"),
                new BigDecimal("53078.03"),
                "Beschikbaar",
                LocalDate.of(2019, 5, 29),
                "Notariële akte",
                "NOTARIS A, B & C",
                "JANE DOE",
                "Kredieten beheer",
                List.of(new CreditDetail.Waarborg(
                        "Onroerend goed",
                        "Hypothecaire inschrijving",
                        1,
                        new BigDecimal("80000.00"),
                        "VOORBEELDSTRAAT 1 bus 1, 1000 VOORBEELDSTAD",
                        List.of("John Doe", "Jane Roe")
                )),
                List.of(
                        new CreditDetail.Participant(
                                "John Doe",
                                "VOORBEELDSTRAAT 1, bus 1, 1000 VOORBEELDSTAD",
                                "Hoofdkredietnemer"),
                        new CreditDetail.Participant(
                                "Jane Roe",
                                "VOORBEELDSTRAAT 1, bus 1, 1000 VOORBEELDSTAD",
                                "Medekredietnemer")
                )
        );
    }

    private CreditFinancialInfo sampleFinancial(String productId) {
        return new CreditFinancialInfo(
                productId,
                new BigDecimal("2.15"),
                240,
                new BigDecimal("410.12"),
                new BigDecimal("26921.97"),
                new BigDecimal("53078.03"),
                LocalDate.of(2026, 6, 1)
        );
    }

    private List<CreditDocument> sampleDocuments(String productId) {
        return List.of(
                new CreditDocument("Kredietovereenkomst.pdf", "PDF", LocalDate.of(2019, 5, 27)),
                new CreditDocument("Hypothecaire akte.pdf", "PDF", LocalDate.of(2019, 5, 29))
        );
    }

    private List<CreditTask> sampleTasks(String productId) {
        return List.of(); // empty list exercises the empty-state branch in taken.html
    }
}
