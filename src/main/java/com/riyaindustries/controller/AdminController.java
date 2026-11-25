package com.riyaindustries.controller;

import com.riyaindustries.entity.Machine;
import com.riyaindustries.entity.MachiningService;
import com.riyaindustries.entity.Product;
import com.riyaindustries.entity.Project;
import com.riyaindustries.entity.QuotationAttachment;
import com.riyaindustries.entity.ServiceCategory;
import com.riyaindustries.entity.WorkStatus;
import com.riyaindustries.service.CatalogService;
import com.riyaindustries.service.CustomerLeadService;
import com.riyaindustries.service.FileStorageService;
import com.riyaindustries.service.WebsiteSettingService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CatalogService catalogService;
    private final CustomerLeadService leadService;
    private final WebsiteSettingService settingService;
    private final FileStorageService fileStorageService;

    public AdminController(
        CatalogService catalogService,
        CustomerLeadService leadService,
        WebsiteSettingService settingService,
        FileStorageService fileStorageService
    ) {
        this.catalogService = catalogService;
        this.leadService = leadService;
        this.settingService = settingService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("stats", leadService.getDashboardStats());
        model.addAttribute("recentQuotations", leadService.searchQuotations(null, null, PageRequest.of(0, 5)).getContent());
        model.addAttribute("recentEnquiries", leadService.searchEnquiries(null, null, PageRequest.of(0, 5)).getContent());
        return "admin/dashboard";
    }

    // ---- Services & Categories ----
    @GetMapping("/services")
    public String listServices(Model model) {
        model.addAttribute("services", catalogService.getAllServicesForAdmin());
        model.addAttribute("categories", catalogService.getAllCategories());
        model.addAttribute("newService", new MachiningService());
        model.addAttribute("newCategory", new ServiceCategory());
        return "admin/services";
    }

    @PostMapping("/services/save")
    public String saveService(
        @ModelAttribute MachiningService service,
        @RequestParam Long categoryId,
        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
        RedirectAttributes ra
    ) {
        if (service.getId() != null) {
            MachiningService existing = catalogService.getServiceById(service.getId());
            if (imageFile == null || imageFile.isEmpty()) {
                service.setPrimaryImageUrl(existing.getPrimaryImageUrl());
            }
        }
        catalogService.saveService(service, categoryId, imageFile);
        ra.addFlashAttribute("successMessage", "Service saved successfully.");
        return "redirect:/admin/services";
    }

    @PostMapping("/services/delete/{id}")
    public String deleteService(@PathVariable Long id, RedirectAttributes ra) {
        catalogService.deleteService(id);
        ra.addFlashAttribute("successMessage", "Service deleted.");
        return "redirect:/admin/services";
    }

    @PostMapping("/categories/save")
    public String saveCategory(
        @ModelAttribute ServiceCategory category,
        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
        RedirectAttributes ra
    ) {
        catalogService.saveCategory(category, imageFile);
        ra.addFlashAttribute("successMessage", "Category saved successfully.");
        return "redirect:/admin/services";
    }

    // ---- Machinery ----
    @GetMapping("/machinery")
    public String listMachinery(Model model) {
        model.addAttribute("machines", catalogService.getAllMachinesForAdmin());
        model.addAttribute("newMachine", new Machine());
        return "admin/machinery";
    }

    @PostMapping("/machinery/save")
    public String saveMachine(
        @ModelAttribute Machine machine,
        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
        RedirectAttributes ra
    ) {
        if (machine.getId() != null) {
            Machine existing = catalogService.getMachineById(machine.getId());
            if (imageFile == null || imageFile.isEmpty()) {
                machine.setPrimaryImageUrl(existing.getPrimaryImageUrl());
            }
        }
        catalogService.saveMachine(machine, imageFile);
        ra.addFlashAttribute("successMessage", "Machine saved successfully.");
        return "redirect:/admin/machinery";
    }

    @PostMapping("/machinery/delete/{id}")
    public String deleteMachine(@PathVariable Long id, RedirectAttributes ra) {
        catalogService.deleteMachine(id);
        ra.addFlashAttribute("successMessage", "Machine deleted.");
        return "redirect:/admin/machinery";
    }

    // ---- Projects / Gallery ----
    @GetMapping("/projects")
    public String listProjects(Model model) {
        model.addAttribute("projects", catalogService.getAllProjectsForAdmin());
        model.addAttribute("services", catalogService.getAllServicesForAdmin());
        model.addAttribute("newProject", new Project());
        return "admin/projects";
    }

    @PostMapping("/projects/save")
    public String saveProject(
        @ModelAttribute Project project,
        @RequestParam(required = false) Long serviceId,
        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
        RedirectAttributes ra
    ) {
        if (project.getId() != null) {
            Project existing = catalogService.getProjectById(project.getId());
            if (imageFile == null || imageFile.isEmpty()) {
                project.setPrimaryImageUrl(existing.getPrimaryImageUrl());
            }
        }
        catalogService.saveProject(project, serviceId, imageFile);
        ra.addFlashAttribute("successMessage", "Project saved successfully.");
        return "redirect:/admin/projects";
    }

    @PostMapping("/projects/delete/{id}")
    public String deleteProject(@PathVariable Long id, RedirectAttributes ra) {
        catalogService.deleteProject(id);
        ra.addFlashAttribute("successMessage", "Project deleted.");
        return "redirect:/admin/projects";
    }

    // ---- Enquiries ----
    @GetMapping("/enquiries")
    public String listEnquiries(
        @RequestParam(required = false) String q,
        @RequestParam(required = false) WorkStatus status,
        @RequestParam(defaultValue = "0") int page,
        Model model
    ) {
        model.addAttribute("enquiryPage", leadService.searchEnquiries(q, status, PageRequest.of(page, 15)));
        model.addAttribute("statuses", WorkStatus.values());
        model.addAttribute("q", q);
        model.addAttribute("selectedStatus", status);
        return "admin/enquiries";
    }

    @PostMapping("/enquiries/{id}/status")
    public String updateEnquiryStatus(
        @PathVariable Long id,
        @RequestParam WorkStatus status,
        @RequestParam(required = false) String internalNotes,
        RedirectAttributes ra
    ) {
        leadService.updateEnquiryStatus(id, status, internalNotes);
        ra.addFlashAttribute("successMessage", "Enquiry updated.");
        return "redirect:/admin/enquiries";
    }

    // ---- Quotations ----
    @GetMapping("/quotations")
    public String listQuotations(
        @RequestParam(required = false) String q,
        @RequestParam(required = false) WorkStatus status,
        @RequestParam(defaultValue = "0") int page,
        Model model
    ) {
        model.addAttribute("quotationPage", leadService.searchQuotations(q, status, PageRequest.of(page, 15)));
        model.addAttribute("statuses", WorkStatus.values());
        model.addAttribute("q", q);
        model.addAttribute("selectedStatus", status);
        return "admin/quotations";
    }

    @GetMapping("/quotations/{id}")
    public String viewQuotation(@PathVariable Long id, Model model) {
        model.addAttribute("quotation", leadService.getQuotationWithAttachments(id));
        model.addAttribute("statuses", WorkStatus.values());
        return "admin/quotation-detail";
    }

    @PostMapping("/quotations/{id}/status")
    public String updateQuotationStatus(
        @PathVariable Long id,
        @RequestParam WorkStatus status,
        @RequestParam(required = false) String internalNotes,
        RedirectAttributes ra
    ) {
        leadService.updateQuotationStatus(id, status, internalNotes);
        ra.addFlashAttribute("successMessage", "Quotation status updated.");
        return "redirect:/admin/quotations/" + id;
    }

    @GetMapping("/quotations/attachments/{attachmentId}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId) {
        QuotationAttachment att = leadService.getAttachment(attachmentId);
        Resource resource = fileStorageService.loadAsResource(att.getStoragePath());
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        try {
            mediaType = MediaType.parseMediaType(att.getContentType());
        } catch (Exception ignored) {
        }
        return ResponseEntity.ok()
            .contentType(mediaType)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + att.getOriginalFilename() + "\"")
            .body(resource);
    }

    // ---- Contact Messages ----
    @GetMapping("/messages")
    public String listMessages(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("messagePage", leadService.getContactMessages(PageRequest.of(page, 15)));
        return "admin/messages";
    }

    @PostMapping("/messages/{id}/read")
    public String markMessageRead(@PathVariable Long id) {
        leadService.markContactMessageRead(id);
        return "redirect:/admin/messages";
    }

    // ---- Future E-Commerce Products ----
    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", catalogService.getAllProductsForAdmin());
        model.addAttribute("productCategories", catalogService.getActiveProductCategories());
        model.addAttribute("newProduct", new Product());
        return "admin/products";
    }

    @PostMapping("/products/save")
    public String saveProduct(
        @ModelAttribute Product product,
        @RequestParam Long categoryId,
        @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
        RedirectAttributes ra
    ) {
        if (product.getId() != null) {
            Product existing = catalogService.getProductById(product.getId());
            if (imageFile == null || imageFile.isEmpty()) {
                product.setPrimaryImageUrl(existing.getPrimaryImageUrl());
            }
        }
        catalogService.saveProduct(product, categoryId, imageFile);
        ra.addFlashAttribute("successMessage", "Product saved successfully.");
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes ra) {
        catalogService.deleteProduct(id);
        ra.addFlashAttribute("successMessage", "Product deleted.");
        return "redirect:/admin/products";
    }

    // ---- Website Settings ----
    @GetMapping("/settings")
    public String viewSettings(Model model) {
        model.addAttribute("settingsList", settingService.findAll());
        return "admin/settings";
    }

    @PostMapping("/settings/save")
    public String saveSettings(@RequestParam Map<String, String> allParams, RedirectAttributes ra) {
        Map<String, String> filtered = new LinkedHashMap<>();
        allParams.forEach((k, v) -> {
            if (!"_csrf".equals(k)) {
                filtered.put(k, v);
            }
        });
        settingService.updateSettings(filtered);
        ra.addFlashAttribute("successMessage", "Website settings updated.");
        return "redirect:/admin/settings";
    }
}
