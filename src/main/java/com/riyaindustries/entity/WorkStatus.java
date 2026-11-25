package com.riyaindustries.entity;

/**
 * Workflow statuses for customer enquiries and quotation requests.
 */
public enum WorkStatus {
    NEW("New"),
    PENDING("Pending"),
    CONTACTED("Contacted"),
    QUOTATION_SENT("Quotation Sent"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    COMPLETED("Completed");

    private final String label;

    WorkStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
