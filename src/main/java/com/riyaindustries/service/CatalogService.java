package com.riyaindustries.service;

import com.riyaindustries.entity.Machine;
import com.riyaindustries.entity.MachiningService;
import com.riyaindustries.entity.Product;
import com.riyaindustries.entity.ProductCategory;
import com.riyaindustries.entity.Project;
import com.riyaindustries.entity.ServiceCategory;
import com.riyaindustries.exception.ResourceNotFoundException;
import com.riyaindustries.repository.MachineRepository;
import com.riyaindustries.repository.MachiningServiceRepository;
import com.riyaindustries.repository.ProductCategoryRepository;
import com.riyaindustries.repository.ProductRepository;
import com.riyaindustries.repository.ProjectRepository;
import com.riyaindustries.repository.ServiceCategoryRepository;
import com.riyaindustries.util.SlugUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CatalogService {

    private final ServiceCategoryRepository categoryRepository;
    private final MachiningServiceRepository serviceRepository;
    private final MachineRepository machineRepository;
    private final ProjectRepository projectRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final FileStorageService fileStorageService;

    public CatalogService(
        ServiceCategoryRepository categoryRepository,
        MachiningServiceRepository serviceRepository,
        MachineRepository machineRepository,
        ProjectRepository projectRepository,
        ProductCategoryRepository productCategoryRepository,
        ProductRepository productRepository,
        FileStorageService fileStorageService
    ) {
        this.categoryRepository = categoryRepository;
        this.serviceRepository = serviceRepository;
        this.machineRepository = machineRepository;
        this.projectRepository = projectRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.fileStorageService = fileStorageService;
    }

    // ---- Service Categories ----
    @Transactional(readOnly = true)
    public List<ServiceCategory> getActiveCategories() {
        return categoryRepository.findByActiveTrueOrderByDisplayOrderAsc();
    }

    @Transactional(readOnly = true)
    public List<ServiceCategory> getAllCategories() {
        return categoryRepository.findAllByOrderByDisplayOrderAsc();
    }

    @Transactional
    public ServiceCategory saveCategory(ServiceCategory category, MultipartFile imageFile) {
        if (category.getSlug() == null || category.getSlug().isBlank()) {
            category.setSlug(SlugUtil.toSlug(category.getName()));
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            category.setImageUrl(fileStorageService.storePublicImage(imageFile, "categories").publicUrl());
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // ---- Services ----
    @Transactional(readOnly = true)
    public List<MachiningService> getPublishedServices(String categorySlug) {
        if (categorySlug != null && !categorySlug.isBlank()) {
            return serviceRepository.findPublishedByCategorySlug(categorySlug);
        }
        return serviceRepository.findAllPublishedWithCategory();
    }

    @Transactional(readOnly = true)
    public List<MachiningService> getFeaturedServices() {
        List<MachiningService> featured = serviceRepository.findFeaturedWithCategory();
        return featured.isEmpty() ? serviceRepository.findAllPublishedWithCategory() : featured;
    }

    @Transactional(readOnly = true)
    public List<MachiningService> getAllServicesForAdmin() {
        return serviceRepository.findAllWithCategoryOrderByDisplayOrder();
    }

    @Transactional(readOnly = true)
    public MachiningService getServiceBySlug(String slug) {
        return serviceRepository.findBySlugWithCategory(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + slug));
    }

    @Transactional(readOnly = true)
    public MachiningService getServiceById(Long id) {
        return serviceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
    }

    @Transactional
    public MachiningService saveService(MachiningService service, Long categoryId, MultipartFile imageFile) {
        ServiceCategory category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Service category not found"));
        service.setCategory(category);
        if (service.getSlug() == null || service.getSlug().isBlank()) {
            service.setSlug(SlugUtil.toSlug(service.getName()));
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            service.setPrimaryImageUrl(fileStorageService.storePublicImage(imageFile, "services").publicUrl());
        }
        return serviceRepository.save(service);
    }

    @Transactional
    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    // ---- Machinery ----
    @Transactional(readOnly = true)
    public List<Machine> getActiveMachines() {
        return machineRepository.findByActiveTrueOrderByDisplayOrderAsc();
    }

    @Transactional(readOnly = true)
    public List<Machine> getAllMachinesForAdmin() {
        return machineRepository.findAllByOrderByDisplayOrderAsc();
    }

    @Transactional(readOnly = true)
    public Machine getMachineById(Long id) {
        return machineRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Machine not found"));
    }

    @Transactional
    public Machine saveMachine(Machine machine, MultipartFile imageFile) {
        if (machine.getSlug() == null || machine.getSlug().isBlank()) {
            machine.setSlug(SlugUtil.toSlug(machine.getName()));
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            machine.setPrimaryImageUrl(fileStorageService.storePublicImage(imageFile, "machines").publicUrl());
        }
        return machineRepository.save(machine);
    }

    @Transactional
    public void deleteMachine(Long id) {
        machineRepository.deleteById(id);
    }

    // ---- Projects / Our Work ----
    @Transactional(readOnly = true)
    public List<Project> getPublishedProjects(String machiningType) {
        if (machiningType != null && !machiningType.isBlank()) {
            return projectRepository.findByPublishedTrueAndMachiningTypeIgnoreCaseOrderByDisplayOrderAsc(machiningType);
        }
        return projectRepository.findByPublishedTrueOrderByDisplayOrderAsc();
    }

    @Transactional(readOnly = true)
    public List<String> getProjectMachiningTypes() {
        return projectRepository.findDistinctPublishedMachiningTypes();
    }

    @Transactional(readOnly = true)
    public List<Project> getAllProjectsForAdmin() {
        return projectRepository.findAllByOrderByDisplayOrderAsc();
    }

    @Transactional(readOnly = true)
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    @Transactional
    public Project saveProject(Project project, Long serviceId, MultipartFile imageFile) {
        if (serviceId != null) {
            project.setService(serviceRepository.findById(serviceId).orElse(null));
        } else {
            project.setService(null);
        }
        if (project.getSlug() == null || project.getSlug().isBlank()) {
            project.setSlug(SlugUtil.toSlug(project.getTitle()));
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            project.setPrimaryImageUrl(fileStorageService.storePublicImage(imageFile, "projects").publicUrl());
        }
        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    // ---- Future E-Commerce Products ----
    @Transactional(readOnly = true)
    public List<ProductCategory> getActiveProductCategories() {
        return productCategoryRepository.findByActiveTrueOrderByDisplayOrderAsc();
    }

    @Transactional(readOnly = true)
    public List<Product> getPublishedProducts() {
        return productRepository.findAllPublishedWithCategory();
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProductsForAdmin() {
        return productRepository.findAllWithCategory();
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Transactional
    public Product saveProduct(Product product, Long categoryId, MultipartFile imageFile) {
        ProductCategory category = productCategoryRepository.findById(categoryId)
            .orElseThrow(() -> new ResourceNotFoundException("Product category not found"));
        product.setCategory(category);
        if (product.getSlug() == null || product.getSlug().isBlank()) {
            product.setSlug(SlugUtil.toSlug(product.getName()));
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            product.setPrimaryImageUrl(fileStorageService.storePublicImage(imageFile, "products").publicUrl());
        }
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
