package DTO.SupplierDTO;

import java.time.LocalDate;

public record agreementDetailsDTO(
        LocalDate recDate,
        String address,
        String type
) {

}
