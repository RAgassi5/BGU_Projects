package DTO.SupplierDTO;

public record DiscountDTO(
        int catalogID,
        int quantityCond,
        double precentage,
        int agreementID
) {}