package com.riyaindustries.repository;

import com.riyaindustries.entity.WebsiteSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WebsiteSettingRepository extends JpaRepository<WebsiteSetting, Long> {
    Optional<WebsiteSetting> findBySettingKey(String settingKey);
    List<WebsiteSetting> findAllByOrderBySettingGroupAscSettingKeyAsc();
}
