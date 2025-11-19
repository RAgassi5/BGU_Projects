package DTO.SupplierDTO;

public record supplierDTO(
        int supplierID,
        String supplierName,
        String bankAccountNumber,
        boolean isActive
) {}
