package DTO.SupplierDTO;


import Domain.SupplierDomain.Contact;

public record orderDTO(
        int orderID,
        int[] orderDate,        // [day, month, year]
        int[] orderRecDate,     // [day, month, year]
        int supplierID,
        boolean orderStatus,
        double totalPrice,
        String orderType,
        String address,
        int contactID
) {}
