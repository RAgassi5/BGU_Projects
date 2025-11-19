package DTO.SupplierDTO;

import java.time.LocalDate;

public record agreementDTO(
        int agreementID,
        int supplierID,
        String paymentMethod,
        LocalDate paymentDate,
        boolean agreementStatus
) {}