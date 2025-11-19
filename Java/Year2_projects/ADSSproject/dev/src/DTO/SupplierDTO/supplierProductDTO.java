package DTO.SupplierDTO;

public record supplierProductDTO(
        int catalogID,
        int productID,
        String manufacturer,
        int supplierID,
        int agreementID,
        double price,
        String agreementType,
        String productName,
        String productType
) {}