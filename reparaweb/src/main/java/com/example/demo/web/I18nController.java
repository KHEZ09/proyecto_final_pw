package com.example.demo.web;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/i18n")
public class I18nController {
    private static final String[] KEYS = {
        "app.name", "language", "login.welcome", "login.subtitle", "login.username", "login.password",
        "login.submit", "login.invalid", "login.demo", "dashboard.work", "dashboard.reports", "dashboard.new",
        "dashboard.new.subtitle", "dashboard.history", "dashboard.refresh", "dashboard.logout", "dashboard.send",
        "dashboard.empty", "landing.eyebrow", "landing.hero", "landing.lead", "landing.start",
        "landing.feature1.title", "landing.feature1.text", "landing.feature2.title", "landing.feature2.text",
        "landing.feature3.title", "landing.feature3.text", "landing.footer"
    };

    private final MessageSource messages;

    public I18nController(MessageSource messages) {
        this.messages = messages;
    }

    @GetMapping("/messages")
    public Map<String, String> messages(@RequestParam(defaultValue = "es") String lang) {
        Locale locale = switch (lang.toLowerCase(Locale.ROOT)) {
            case "en" -> Locale.ENGLISH;
            case "pt" -> Locale.forLanguageTag("pt");
            default -> Locale.forLanguageTag("es");
        };
        Map<String, String> result = new LinkedHashMap<>();
        for (String key : KEYS) {
            result.put(key, messages.getMessage(key, null, locale));
        }
        return result;
    }
}
