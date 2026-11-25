package com.riyaindustries.service;

import com.riyaindustries.entity.WebsiteSetting;
import com.riyaindustries.repository.WebsiteSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebsiteSettingService {

    private final WebsiteSettingRepository repository;

    public WebsiteSettingService(WebsiteSettingRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Map<String, String> getAllAsMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (WebsiteSetting s : repository.findAllByOrderBySettingGroupAscSettingKeyAsc()) {
            map.put(s.getSettingKey(), s.getSettingValue() != null ? s.getSettingValue() : "");
        }
        map.putIfAbsent("business_name", "Riya Industries");
        map.putIfAbsent("business_tagline", "Precision Lathe Job-Work & Industrial Machining");
        map.putIfAbsent("contact_phone", "+91-00000-00000");
        map.putIfAbsent("whatsapp_number", "910000000000");
        map.putIfAbsent("contact_email", "contact@riyaindustries.example");
        map.putIfAbsent("workshop_address", "Update workshop address in Admin -> Website Settings");
        map.putIfAbsent("working_hours", "Mon - Sat: 9:00 AM - 7:00 PM");
        map.putIfAbsent("google_maps_embed_url", "");
        map.putIfAbsent("ecommerce_enabled", "false");
        return map;
    }

    @Transactional(readOnly = true)
    public List<WebsiteSetting> findAll() {
        return repository.findAllByOrderBySettingGroupAscSettingKeyAsc();
    }

    @Transactional(readOnly = true)
    public boolean isEcommerceEnabled() {
        return repository.findBySettingKey("ecommerce_enabled")
            .map(s -> "true".equalsIgnoreCase(s.getSettingValue()))
            .orElse(false);
    }

    @Transactional
    public void updateSettings(Map<String, String> updates) {
        for (Map.Entry<String, String> entry : updates.entrySet()) {
            WebsiteSetting setting = repository.findBySettingKey(entry.getKey())
                .orElseGet(() -> {
                    WebsiteSetting s = new WebsiteSetting();
                    s.setSettingKey(entry.getKey());
                    s.setSettingGroup("GENERAL");
                    return s;
                });
            setting.setSettingValue(entry.getValue());
            repository.save(setting);
        }
    }
}
