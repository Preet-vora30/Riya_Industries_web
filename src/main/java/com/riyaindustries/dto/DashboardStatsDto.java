package com.riyaindustries.dto;

public record DashboardStatsDto(
    long totalEnquiries,
    long newEnquiries,
    long pendingQuotations,
    long completedProjects,
    long totalServices,
    long totalMachines,
    long unreadContactMessages
) {
}
