package com.riyaindustries.controller;

import com.riyaindustries.dto.ContactFormDto;
import com.riyaindustries.dto.EnquiryFormDto;
import com.riyaindustries.dto.QuotationFormDto;
import com.riyaindustries.entity.MachiningService;
import com.riyaindustries.entity.QuotationRequest;
import com.riyaindustries.service.CatalogService;
import com.riyaindustries.service.CustomerLeadService;
import com.riyaindustries.service.WebsiteSettingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PublicController {

    private final CatalogService catalogService;
    private final CustomerLeadService leadService;
    private final WebsiteSettingService settingService;

    public PublicController(
        CatalogService catalogService,
        CustomerLeadService leadService,
        WebsiteSettingService settingService
    ) {
        this.catalogService = catalogService;
        this.leadService = leadService;
        this.settingService = settingService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("categories", catalogService.getActiveCategories());
        model.addAttribute("featuredServices", catalogService.getFeaturedServices());
        model.addAttribute("machines", catalogService.getActiveMachines());
        model.addAttribute("projects", catalogService.getPublishedProjects(null));
        model.addAttribute("projectTypes", catalogService.getProjectMachiningTypes());
        if (!model.containsAttribute("quotationForm")) {
            model.addAttribute("quotationForm", new QuotationFormDto());
        }
        return "public/home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("machines", catalogService.getActiveMachines());
        model.addAttribute("categories", catalogService.getActiveCategories());
        return "public/about";
    }

    @GetMapping("/services")
    public String services(@RequestParam(required = false) String category, Model model) {
        model.addAttribute("categories", catalogService.getActiveCategories());
        model.addAttribute("services", catalogService.getPublishedServices(category));
        model.addAttribute("selectedCategory", category);
        return "public/services";
    }

    @GetMapping("/services/{slug}")
    public String serviceDetail(@PathVariable String slug, Model model) {
        MachiningService service = catalogService.getServiceBySlug(slug);
        model.addAttribute("service", service);
        model.addAttribute("relatedProjects", catalogService.getPublishedProjects(null));
        return "public/service-detail";
    }

    @GetMapping("/machinery")
    public String machinery(Model model) {
        model.addAttribute("machines", catalogService.getActiveMachines());
        return "public/machinery";
    }

    @GetMapping("/our-work")
    public String ourWork(@RequestParam(required = false) String type, Model model) {
        model.addAttribute("projects", catalogService.getPublishedProjects(type));
        model.addAttribute("projectTypes", catalogService.getProjectMachiningTypes());
        model.addAttribute("selectedType", type);
        return "public/our-work";
    }

    @GetMapping("/request-quote")
    public String requestQuote(@RequestParam(required = false) Long serviceId, Model model) {
        model.addAttribute("services", catalogService.getPublishedServices(null));
        if (!model.containsAttribute("quotationForm")) {
            QuotationFormDto form = new QuotationFormDto();
            if (serviceId != null) {
                MachiningService svc = catalogService.getServiceById(serviceId);
                form.setServiceId(svc.getId());
                form.setRequiredServiceName(svc.getName());
            }
            model.addAttribute("quotationForm", form);
        }
        return "public/request-quote";
    }

    @PostMapping("/request-quote")
    public String submitQuote(
        @Valid @ModelAttribute("quotationForm") QuotationFormDto form,
        BindingResult bindingResult,
        @RequestParam(value = "attachments", required = false) MultipartFile[] attachments,
        @RequestParam(value = "redirectTo", defaultValue = "/request-quote") String redirectTo,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("services", catalogService.getPublishedServices(null));
            if ("/".equals(redirectTo)) {
                model.addAttribute("categories", catalogService.getActiveCategories());
                model.addAttribute("featuredServices", catalogService.getFeaturedServices());
                model.addAttribute("machines", catalogService.getActiveMachines());
                model.addAttribute("projects", catalogService.getPublishedProjects(null));
                model.addAttribute("projectTypes", catalogService.getProjectMachiningTypes());
                return "public/home";
            }
            return "public/request-quote";
        }

        QuotationRequest saved = leadService.submitQuotation(form, attachments);
        redirectAttributes.addFlashAttribute(
            "quoteSuccess",
            "Thank you! Your quotation request (" + saved.getReferenceCode() + ") has been received. We will contact you shortly."
        );
        return "redirect:" + ("/".equals(redirectTo) ? "/#request-quote" : "/request-quote");
    }

    @PostMapping("/enquiries")
    public String submitEnquiry(
        @Valid @ModelAttribute("enquiryForm") EnquiryFormDto form,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("enquiryError", "Please fill in all required fields.");
            return "redirect:/contact";
        }
        leadService.submitEnquiry(form);
        redirectAttributes.addFlashAttribute("enquirySuccess", "Your enquiry has been submitted. Our team will reach out shortly.");
        return "redirect:/contact";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        if (!model.containsAttribute("contactForm")) {
            model.addAttribute("contactForm", new ContactFormDto());
        }
        return "public/contact";
    }

    @PostMapping("/contact")
    public String submitContact(
        @Valid @ModelAttribute("contactForm") ContactFormDto form,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "public/contact";
        }
        leadService.submitContactMessage(form);
        redirectAttributes.addFlashAttribute("contactSuccess", "Thank you for contacting us. We will respond as soon as possible.");
        return "redirect:/contact";
    }

    @GetMapping("/products")
    public String products(Model model) {
        if (!settingService.isEcommerceEnabled()) {
            model.addAttribute("comingSoon", true);
        } else {
            model.addAttribute("comingSoon", false);
            model.addAttribute("products", catalogService.getPublishedProducts());
            model.addAttribute("productCategories", catalogService.getActiveProductCategories());
        }
        return "public/products";
    }
}
