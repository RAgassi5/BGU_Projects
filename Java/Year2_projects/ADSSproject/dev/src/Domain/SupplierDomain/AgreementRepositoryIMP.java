package Domain.SupplierDomain;

import DTO.SupplierDTO.agreementDetailsDTO;
import DataAccess.SupplierDAO.AgreementDAO;
import DTO.SupplierDTO.agreementDTO;
import DTO.SupplierDTO.DiscountDTO;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class AgreementRepositoryIMP implements agreementRepository {

    private final Map<Integer, ArrayList<Agreement>> agreements = new HashMap<>();
    private final Map<Integer, ArrayList<FixedDeliveryAgreement>> fixedAgreements = new HashMap<>();
    private final Map<Integer, ArrayList<FlexibleDeliveryAgreement>> flexibleAgreements = new HashMap<>();
    private final Map<Integer, ArrayList<PickUpAgreement>> pickUpAgreements = new HashMap<>();
    private final Map<Integer, Discount> productsDiscounts = new HashMap<>();


    private AgreementDAO agreementDAO;

    public AgreementRepositoryIMP(AgreementDAO dao) {
        this.agreementDAO = dao;
    }

    @Override
    public int createPickUp(int supplierID, String paymentMethod, String address) {
        try {
            PickUpAgreement agreement = new PickUpAgreement(supplierID, paymentMethod, address);
            int agreementID = agreementDAO.insertAgreement(toDTO(agreement));
            agreement.setAgreementID(agreementID);
            agreementDAO.insertPickupAgreement(agreementID, address);

            agreements.computeIfAbsent(supplierID, k -> new ArrayList<>()).add(agreement);
            pickUpAgreements.computeIfAbsent(supplierID, k -> new ArrayList<>()).add(agreement);
            return agreementID;
        } catch (SQLException e) {
            System.err.println("Failed to create PickUp agreement: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public int createFixed(String paymentMethod, int supplierID, ArrayList<DayOfWeek> supplyDays) {
        try {
            FixedDeliveryAgreement agreement = new FixedDeliveryAgreement(supplierID, paymentMethod, supplyDays);
            int agreementID = agreementDAO.insertAgreement(toDTO(agreement));
            agreement.setAgreementID(agreementID);
            String binaryDays = supplyDaysToBinaryString(supplyDays);
            agreementDAO.insertFixedAgreement(agreementID, binaryDays);

            agreements.computeIfAbsent(supplierID, k -> new ArrayList<>()).add(agreement);
            fixedAgreements.computeIfAbsent(supplierID, k -> new ArrayList<>()).add(agreement);
            return agreementID;
        } catch (SQLException e) {
            System.err.println("Failed to create Fixed agreement: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public int createFlexible(String paymentMethod, int supplierID, int supplyDays) {
        try {
            FlexibleDeliveryAgreement agreement = new FlexibleDeliveryAgreement(supplierID, paymentMethod, supplyDays);
            int agreementID = agreementDAO.insertAgreement(toDTO(agreement));
            agreement.setAgreementID(agreementID);
            agreementDAO.insertFlexibleAgreement(agreementID, supplyDays);

            agreements.computeIfAbsent(supplierID, k -> new ArrayList<>()).add(agreement);
            flexibleAgreements.computeIfAbsent(supplierID, k -> new ArrayList<>()).add(agreement);
            return agreementID;
        } catch (SQLException e) {
            System.err.println("Failed to create Flexible agreement: " + e.getMessage());
            return -1;
        }
    }


    @Override
    public boolean agreementExists(int agreementID) {
        for (ArrayList<Agreement> list : agreements.values()) {
            for (Agreement agreement : list) {
                if (agreement.getAgreementID() == agreementID) return true;
            }
        }
        return false;
    }

    @Override
    public void addDiscount(int agreementID, int catalogID, int supplierID, int quantityCond, double percentage) {
        try {
            Discount discount = new Discount(catalogID, quantityCond, percentage, agreementID);
            productsDiscounts.put(catalogID, discount);
            agreementDAO.insertDiscount(toDTO(discount));
        } catch (SQLException e) {
            System.err.println("Failed to insert discount: " + e.getMessage());
        }
    }


    @Override
    public void changeStatus(int supplierID, int agreementID, boolean agreementStatus) {
        ArrayList<Agreement> list = agreements.get(supplierID);
        if (list == null) return;

        for (Agreement a : list) {
            if (a.getAgreementID() == agreementID) {
                a.setAgreementStatus(agreementStatus);
                try {
                    agreementDAO.updateAgreement(toDTO(a));
                } catch (SQLException e) {
                    System.err.println("❌ Failed to update agreement status (ID: " + agreementID + "): " + e.getMessage());
                }
                return;
            }
        }
    }


    @Override
    public double getPercentage(int catalogID, int amount) {
        if (productsDiscounts.containsKey(catalogID)) {
            return productsDiscounts.get(catalogID).getPrecentage(amount);
        }
        return 0;
    }

    @Override
    public boolean checkValid(String type, int supplierID) {
        ArrayList<Agreement> list = agreements.get(supplierID);
        if (list == null) return true;
        for (Agreement a : list) {
            if (a.getAgreementType().equals(type) && a.getAgreementStatus()) return false;
        }
        return true;
    }

    @Override
    public boolean existAgreement(int supplierID, int agreementID) {
        ArrayList<Agreement> list = agreements.get(supplierID);
        if (list == null) return false;
        for (Agreement a : list) {
            if (a.getAgreementID() == agreementID) return true;
        }
        return false;
    }

    @Override
    public void changePrecentageInDiscount(int supplierID, int agreementID, double percentage) {
        for (Discount discount : productsDiscounts.values()) {
            if (discount.getAgreementID() == agreementID) {
                discount.setPrecentage(percentage);
                try {
                    agreementDAO.updateDiscount(toDTO(discount));
                } catch (SQLException e) {
                    System.err.println("Failed to update discount percentage: " + e.getMessage());
                }
                return;
            }
        }
    }

    @Override
    public void changeQuantityCondInDiscount(int supplierID, int agreementID, int quantityCond) {
        for (Discount discount : productsDiscounts.values()) {
            if (discount.getAgreementID() == agreementID) {
                discount.setQuantityCond(quantityCond);
                try {
                    agreementDAO.updateDiscount(toDTO(discount));
                } catch (SQLException e) {
                    System.err.println("Failed to update discount quantity condition: " + e.getMessage());
                }
                return;
            }
        }
    }

    @Override
    public void displayAgreement(int supplierID, int agreementID) {
        ArrayList<Agreement> list = agreements.get(supplierID);
        if (list != null) {
            for (Agreement a : list) {
                if (a.getAgreementID() == agreementID) {
                    System.out.println(a);
                    return;
                }
            }
        }
        System.out.println("Agreement not found.");
    }

    private String supplyDaysToBinaryString(ArrayList<DayOfWeek> supplyDays) {
        DayOfWeek[] daysOfWeek = {
                DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY
        };
        StringBuilder sb = new StringBuilder();
        for (DayOfWeek day : daysOfWeek) {
            if (supplyDays != null && supplyDays.contains(day)) sb.append('1');
            else sb.append('0');
        }
        return sb.toString();
    }

    private ArrayList<DayOfWeek> binaryStringToSupplyDays(String binary) {
        ArrayList<DayOfWeek> result = new ArrayList<>();
        if (binary == null || binary.length() != 7) return result;
        DayOfWeek[] daysOfWeek = {
                DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY
        };
        for (int i = 0; i < 7; i++) {
            if (binary.charAt(i) == '1') result.add(daysOfWeek[i]);
        }
        return result;
    }

    public void loadAgreementsFromDatabase(int supplierID) {
        try {
            List<agreementDTO> dtos = agreementDAO.getAllAgreementsForSupplier(supplierID);
            ArrayList<Agreement> domainAgreements = new ArrayList<>();
            for (agreementDTO dto : dtos) {
                domainAgreements.add(fromDTO(dto));
            }
            agreements.put(supplierID, domainAgreements);

            for (Agreement agreement : domainAgreements) {
                int agreementID = agreement.getAgreementID();
                switch (agreement.getAgreementType()) {
                    case "Fixed" -> {
                        ArrayList<DayOfWeek> days = agreementDAO.getFixedSupplyDays(agreementID);
                        FixedDeliveryAgreement fixed = new FixedDeliveryAgreement(
                                agreement.getAgreementID(),
                                agreement.getSupplierID(),
                                agreement.getPaymentMethod(),
                                agreement.getPaymentDate(),
                                agreement.getAgreementStatus(),
                                days
                        );
                        fixedAgreements.computeIfAbsent(supplierID, k -> new ArrayList<>()).add(fixed);
                    }
                    case "Flexible" -> {
                        int supplyDays = agreementDAO.getFlexibleSupplyDays(agreementID);
                        FlexibleDeliveryAgreement flexible = new FlexibleDeliveryAgreement(
                                agreement.getAgreementID(),
                                agreement.getSupplierID(),
                                agreement.getPaymentMethod(),
                                agreement.getPaymentDate(),
                                agreement.getAgreementStatus(),
                                supplyDays
                        );
                        flexibleAgreements.computeIfAbsent(supplierID, k -> new ArrayList<>()).add(flexible);
                    }
                    case "PickUp" -> {
                        String address = agreementDAO.getPickupAddress(agreementID);
                        PickUpAgreement pickup = new PickUpAgreement(
                                agreement.getAgreementID(),
                                agreement.getSupplierID(),
                                agreement.getPaymentMethod(),
                                agreement.getPaymentDate(),
                                agreement.getAgreementStatus(),
                                address
                        );
                        pickUpAgreements.computeIfAbsent(supplierID, k -> new ArrayList<>()).add(pickup);
                    }
                }
            }

            List<DiscountDTO> discountDTOs = agreementDAO.getDiscountsForSupplier(supplierID);
            for (DiscountDTO dto : discountDTOs) {
                Discount discount = fromDTO(dto);
                productsDiscounts.put(discount.getCatalogID(), discount);
            }
        } catch (SQLException e) {
            System.err.println("Error loading agreements for supplier ID " + supplierID + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public agreementDetailsDTO getAgreementDetails(int agreementID, int supplierID, LocalDate orderDate) {
        ArrayList<Agreement> list = agreements.get(supplierID);
        if (list == null) return null;

        for (Agreement a : list) {
            if (a.getAgreementID() == agreementID) {

                String address = null;
                LocalDate orderRecDate = null;
                String agreementType = null;

                switch (findAgreementType(supplierID, a.getAgreementID())) {
                    case "PickUp" -> {
                        for (PickUpAgreement p : pickUpAgreements.getOrDefault(supplierID, new ArrayList<>())) {
                            if (p.getAgreementID() == agreementID) {
                                address = p.getSupplierAddress();
                                agreementType = p.getAgreementType();
                                // orderRecDate stays null
                            }
                        }
                    }

                    case "Flexible" -> {
                        for (FlexibleDeliveryAgreement f : flexibleAgreements.getOrDefault(supplierID, new ArrayList<>())) {
                            if (f.getAgreementID() == agreementID) {
                                orderRecDate = orderDate.plusDays(f.getSupplyDays());
                                agreementType = f.getAgreementType();
                            }
                        }
                    }

                    case "Fixed" -> {
                        for (FixedDeliveryAgreement f : fixedAgreements.getOrDefault(supplierID, new ArrayList<>())) {
                            if (f.getAgreementID() == agreementID) {
                                ArrayList<DayOfWeek> supplyDays = f.getSupplyDays();
                                orderRecDate = calculateNextSupplyDay(orderDate, supplyDays);
                                agreementType = f.getAgreementType();
                            }
                        }
                    }
                }

                return new agreementDetailsDTO(orderRecDate, address,agreementType);
            }
        }

        return null;
    }

    private String findAgreementType(int supplierId, int agreementId) {

        for (FixedDeliveryAgreement f : fixedAgreements.get(supplierId)){
            if (f.getAgreementID() == agreementId) {
                return "Fixed";
            }
        }

        for (FlexibleDeliveryAgreement f
                : flexibleAgreements.get(supplierId)) {
            if (f.getAgreementID() == agreementId) {
                return "Flexible";
            }
        }

        for (PickUpAgreement p : pickUpAgreements.get(supplierId)) {
            if (p.getAgreementID() == agreementId) {
                return "PickUp";
            }
        }
        return "";
    }




    private agreementDTO toDTO(Agreement agreement) {
        return new agreementDTO(
                agreement.getAgreementID(),
                agreement.getSupplierID(),
                agreement.getPaymentMethod(),
                agreement.getPaymentDate(),
                agreement.getAgreementStatus()
        );
    }

    private Agreement fromDTO(agreementDTO dto) {
        return new Agreement(
                dto.agreementID(),
                dto.supplierID(),
                dto.paymentMethod(),
                dto.paymentDate(),
                dto.agreementStatus()
        );
    }

    private DiscountDTO toDTO(Discount discount) {
        return new DiscountDTO(
                discount.getCatalogID(),
                discount.getQuantityCond(),
                discount.getPrecentage(-1),
                discount.getAgreementID()
        );
    }

    private Discount fromDTO(DiscountDTO dto) {
        return new Discount(
                dto.catalogID(),
                dto.quantityCond(),
                dto.precentage(),
                dto.agreementID()
        );
    }

    private LocalDate calculateNextSupplyDay(LocalDate from, List<DayOfWeek> supplyDays) {
        if (supplyDays == null || supplyDays.isEmpty()) {
            throw new IllegalArgumentException("supplyDays must not be empty");
        }

        int today = from.getDayOfWeek().getValue();
        int minDelta = 8;

        for (DayOfWeek d : supplyDays) {
            int dow = d.getValue();
            int delta = (dow - today + 7) % 7;
            if (delta == 0) delta = 7;
            minDelta = Math.min(minDelta, delta);
        }
        return from.plusDays(minDelta);
    }

    private Agreement findAgreementById(int id) {
        for (ArrayList<Agreement> list : agreements.values()) {
            for (Agreement a : list) {
                if (a.getAgreementID() == id) return a;
            }
        }
        return null;
    }

    public List<Integer> getAgreementsIdsForTomorrow() {
        int tomorrowZeroBased = LocalDate.now()
                .plusDays(1)
                .getDayOfWeek()
                .getValue() % 7;

        try {
            return agreementDAO.getAgreementsWithSupplyTomorrow(tomorrowZeroBased);
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    public String getAgreementType(int agreementID) {
        for (ArrayList<FixedDeliveryAgreement> fixedList : fixedAgreements.values()) {
            for (FixedDeliveryAgreement a : fixedList) {
                if (a.getAgreementID() == agreementID) {
                    return "Fixed";
                }
            }
        }

        for (ArrayList<FlexibleDeliveryAgreement> flexList : flexibleAgreements.values()) {
            for (FlexibleDeliveryAgreement a : flexList) {
                if (a.getAgreementID() == agreementID) {
                    return "Flexible";
                }
            }
        }

        for (ArrayList<PickUpAgreement> pickupList : pickUpAgreements.values()) {
            for (PickUpAgreement a : pickupList) {
                if (a.getAgreementID() == agreementID) {
                    return "PickUp";
                }
            }
        }

        return null;
    }

}
