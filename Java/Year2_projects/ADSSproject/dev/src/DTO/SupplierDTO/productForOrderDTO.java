package DTO.SupplierDTO;

public record productForOrderDTO(
        supplierProductDTO spDTO,
        int amount,
        double price
) {}
