package com.riyaindustries.controller;

import com.riyaindustries.service.WebsiteSettingService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@ControllerAdvice
public class GlobalModelAdvice {

    private final WebsiteSettingService settingService;

    public GlobalModelAdvice(WebsiteSettingService settingService) {
        this.settingService = settingService;
    }

    @ModelAttribute("site")
    public Map<String, String> siteSettings() {
        return settingService.getAllAsMap();
    }

    @ModelAttribute("ecommerceEnabled")
    public boolean ecommerceEnabled() {
        return settingService.isEcommerceEnabled();
    }
}
