package DTO.SupplierDTO;

public record orderItemDTO(
        int orderID,
        int productID,
        int quantity,
        int price,
        String name
) {}