package com.riyaindustries.service;

import com.riyaindustries.dto.ContactFormDto;
import com.riyaindustries.dto.DashboardStatsDto;
import com.riyaindustries.dto.EnquiryFormDto;
import com.riyaindustries.dto.QuotationFormDto;
import com.riyaindustries.entity.ContactMessage;
import com.riyaindustries.entity.Customer;
import com.riyaindustries.entity.Enquiry;
import com.riyaindustries.entity.QuotationAttachment;
import com.riyaindustries.entity.QuotationRequest;
import com.riyaindustries.entity.WorkStatus;
import com.riyaindustries.exception.ResourceNotFoundException;
import com.riyaindustries.repository.ContactMessageRepository;
import com.riyaindustries.repository.CustomerRepository;
import com.riyaindustries.repository.EnquiryRepository;
import com.riyaindustries.repository.MachineRepository;
import com.riyaindustries.repository.MachiningServiceRepository;
import com.riyaindustries.repository.ProjectRepository;
import com.riyaindustries.repository.QuotationAttachmentRepository;
import com.riyaindustries.repository.QuotationRequestRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerLeadService {

    private final CustomerRepository customerRepository;
    private final EnquiryRepository enquiryRepository;
    private final QuotationRequestRepository quotationRepository;
    private final QuotationAttachmentRepository attachmentRepository;
    private final ContactMessageRepository contactMessageRepository;
    private final MachiningServiceRepository serviceRepository;
    private final MachineRepository machineRepository;
    private final ProjectRepository projectRepository;
    private final FileStorageService fileStorageService;

    public CustomerLeadService(
        CustomerRepository customerRepository,
        EnquiryRepository enquiryRepository,
        QuotationRequestRepository quotationRepository,
        QuotationAttachmentRepository attachmentRepository,
        ContactMessageRepository contactMessageRepository,
        MachiningServiceRepository serviceRepository,
        MachineRepository machineRepository,
        ProjectRepository projectRepository,
        FileStorageService fileStorageService
    ) {
        this.customerRepository = customerRepository;
        this.enquiryRepository = enquiryRepository;
        this.quotationRepository = quotationRepository;
        this.attachmentRepository = attachmentRepository;
        this.contactMessageRepository = contactMessageRepository;
        this.serviceRepository = serviceRepository;
        this.machineRepository = machineRepository;
        this.projectRepository = projectRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public QuotationRequest submitQuotation(QuotationFormDto dto, MultipartFile[] attachments) {
        Customer customer = upsertCustomer(dto.getCustomerName(), dto.getCompanyName(), dto.getPhone(), dto.getEmail());

        QuotationRequest qr = new QuotationRequest();
        qr.setReferenceCode(generateReferenceCode());
        qr.setCustomer(customer);
        if (dto.getServiceId() != null) {
            qr.setService(serviceRepository.findById(dto.getServiceId()).orElse(null));
        }
        qr.setCustomerName(dto.getCustomerName().trim());
        qr.setCompanyName(dto.getCompanyName() != null ? dto.getCompanyName().trim() : null);
        qr.setPhone(dto.getPhone().trim());
        qr.setEmail(dto.getEmail() != null ? dto.getEmail().trim() : null);
        qr.setRequiredServiceName(dto.getRequiredServiceName().trim());
        qr.setComponentDescription(dto.getComponentDescription().trim());
        qr.setQuantity(dto.getQuantity());
        qr.setMaterial(dto.getMaterial());
        qr.setTechnicalRequirements(dto.getTechnicalRequirements());
        qr.setStatus(WorkStatus.NEW);

        if (attachments != null) {
            for (MultipartFile file : attachments) {
                if (file != null && !file.isEmpty()) {
                    FileStorageService.StoredFile stored = fileStorageService.storeQuotationDrawing(file);
                    QuotationAttachment att = new QuotationAttachment();
                    att.setQuotationRequest(qr);
                    att.setOriginalFilename(stored.originalFilename());
                    att.setStoredFilename(stored.storedFilename());
                    att.setContentType(stored.contentType());
                    att.setFileSizeBytes(stored.fileSizeBytes());
                    att.setStoragePath(stored.relativePath());
                    qr.getAttachments().add(att);
                }
            }
        }

        return quotationRepository.save(qr);
    }

    @Transactional
    public Enquiry submitEnquiry(EnquiryFormDto dto) {
        Customer customer = upsertCustomer(dto.getName(), dto.getCompanyName(), dto.getPhone(), dto.getEmail());

        Enquiry enquiry = new Enquiry();
        enquiry.setCustomer(customer);
        enquiry.setName(dto.getName().trim());
        enquiry.setCompanyName(dto.getCompanyName() != null ? dto.getCompanyName().trim() : null);
        enquiry.setPhone(dto.getPhone().trim());
        enquiry.setEmail(dto.getEmail() != null ? dto.getEmail().trim() : null);
        enquiry.setSubject(dto.getSubject().trim());
        enquiry.setMessage(dto.getMessage().trim());
        enquiry.setSourcePage(dto.getSourcePage());
        enquiry.setStatus(WorkStatus.NEW);
        return enquiryRepository.save(enquiry);
    }

    @Transactional
    public ContactMessage submitContactMessage(ContactFormDto dto) {
        ContactMessage msg = new ContactMessage();
        msg.setName(dto.getName().trim());
        msg.setPhone(dto.getPhone().trim());
        msg.setEmail(dto.getEmail() != null ? dto.getEmail().trim() : null);
        msg.setSubject(dto.getSubject().trim());
        msg.setMessage(dto.getMessage().trim());
        msg.setRead(false);
        return contactMessageRepository.save(msg);
    }

    @Transactional(readOnly = true)
    public DashboardStatsDto getDashboardStats() {
        long totalEnquiries = enquiryRepository.count();
        long newEnquiries = enquiryRepository.countByStatus(WorkStatus.NEW);
        long pendingQuotations = quotationRepository.countByStatusIn(List.of(WorkStatus.NEW, WorkStatus.PENDING));
        long completedProjects = projectRepository.countByPublishedTrue();
        long totalServices = serviceRepository.count();
        long totalMachines = machineRepository.count();
        long unreadContacts = contactMessageRepository.countByReadFalse();

        return new DashboardStatsDto(
            totalEnquiries, newEnquiries, pendingQuotations,
            completedProjects, totalServices, totalMachines, unreadContacts
        );
    }

    @Transactional(readOnly = true)
    public Page<Enquiry> searchEnquiries(String query, WorkStatus status, Pageable pageable) {
        if (query != null && !query.isBlank()) {
            String q = query.trim();
            return enquiryRepository
                .findByNameContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrPhoneContainingIgnoreCaseOrderByCreatedAtDesc(
                    q, q, q, pageable
                );
        }
        if (status != null) {
            return enquiryRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        }
        return enquiryRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional
    public void updateEnquiryStatus(Long id, WorkStatus status, String internalNotes) {
        Enquiry enquiry = enquiryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Enquiry not found"));
        enquiry.setStatus(status);
        enquiry.setInternalNotes(internalNotes);
        enquiryRepository.save(enquiry);
    }

    @Transactional(readOnly = true)
    public Page<QuotationRequest> searchQuotations(String query, WorkStatus status, Pageable pageable) {
        if (query != null && !query.isBlank()) {
            String q = query.trim();
            return quotationRepository
                .findByCustomerNameContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrReferenceCodeContainingIgnoreCaseOrderByCreatedAtDesc(
                    q, q, q, pageable
                );
        }
        if (status != null) {
            return quotationRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        }
        return quotationRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional(readOnly = true)
    public QuotationRequest getQuotationWithAttachments(Long id) {
        return quotationRepository.findByIdWithAttachments(id)
            .orElseThrow(() -> new ResourceNotFoundException("Quotation request not found"));
    }

    @Transactional(readOnly = true)
    public QuotationAttachment getAttachment(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));
    }

    @Transactional
    public void updateQuotationStatus(Long id, WorkStatus status, String internalNotes) {
        QuotationRequest qr = quotationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Quotation request not found"));
        qr.setStatus(status);
        qr.setInternalNotes(internalNotes);
        quotationRepository.save(qr);
    }

    @Transactional(readOnly = true)
    public Page<ContactMessage> getContactMessages(Pageable pageable) {
        return contactMessageRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional
    public void markContactMessageRead(Long id) {
        contactMessageRepository.findById(id).ifPresent(m -> {
            m.setRead(true);
            contactMessageRepository.save(m);
        });
    }

    private Customer upsertCustomer(String fullName, String companyName, String phone, String email) {
        String cleanPhone = phone.trim();
        return customerRepository.findFirstByPhone(cleanPhone).map(existing -> {
            if (companyName != null && !companyName.isBlank()) {
                existing.setCompanyName(companyName.trim());
            }
            if (email != null && !email.isBlank()) {
                existing.setEmail(email.trim());
            }
            return customerRepository.save(existing);
        }).orElseGet(() -> {
            Customer c = new Customer();
            c.setFullName(fullName.trim());
            c.setCompanyName(companyName != null ? companyName.trim() : null);
            c.setPhone(cleanPhone);
            c.setEmail(email != null ? email.trim() : null);
            return customerRepository.save(c);
        });
    }

    private String generateReferenceCode() {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String shortUuid = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        return "RIQ-" + datePart + "-" + shortUuid;
    }
}
