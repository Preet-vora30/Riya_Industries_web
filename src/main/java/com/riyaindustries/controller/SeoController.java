package com.riyaindustries.controller;

import com.riyaindustries.entity.MachiningService;
import com.riyaindustries.service.CatalogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SeoController {

    private final CatalogService catalogService;

    public SeoController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String robotsTxt(HttpServletRequest request) {
        String baseUrl = resolveBaseUrl(request);
        return """
            User-agent: *
            Disallow: /admin/
            Allow: /
            Sitemap: %s/sitemap.xml
            """.formatted(baseUrl);
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemapXml(HttpServletRequest request) {
        String baseUrl = resolveBaseUrl(request);
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        List<String> staticPaths = List.of("/", "/about", "/services", "/machinery", "/our-work", "/request-quote", "/contact");
        for (String path : staticPaths) {
            appendUrl(xml, baseUrl + path);
        }
        for (MachiningService s : catalogService.getPublishedServices(null)) {
            appendUrl(xml, baseUrl + "/services/" + s.getSlug());
        }
        xml.append("</urlset>");
        return xml.toString();
    }

    private void appendUrl(StringBuilder xml, String loc) {
        xml.append("  <url><loc>").append(loc).append("</loc></url>\n");
    }

    private String resolveBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int port = request.getServerPort();
        boolean standardPort = ("http".equalsIgnoreCase(scheme) && port == 80)
            || ("https".equalsIgnoreCase(scheme) && port == 443);
        return standardPort ? scheme + "://" + serverName : scheme + "://" + serverName + ":" + port;
    }
}
