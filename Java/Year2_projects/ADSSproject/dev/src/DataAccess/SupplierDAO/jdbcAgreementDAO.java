package DataAccess.SupplierDAO;

import DTO.SupplierDTO.agreementDTO;
import DTO.SupplierDTO.DiscountDTO;
import Util.Database;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class jdbcAgreementDAO implements AgreementDAO {

    @Override
    public int insertAgreement(agreementDTO agreement) throws SQLException {
        String sql = "INSERT INTO agreements(supplierID, paymentMethod, paymentDate, agreementStatus) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, agreement.supplierID());
            ps.setString(2, agreement.paymentMethod());

            LocalDate ld = agreement.paymentDate();
            int paymentDateInt = ld.getYear() * 10000 + ld.getMonthValue() * 100 + ld.getDayOfMonth();
            ps.setInt(3, paymentDateInt);

            ps.setBoolean(4, agreement.agreementStatus());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Creating agreement failed, no ID obtained.");
                }
            }
        }
    }


    @Override
    public void updateAgreement(agreementDTO agreement) throws SQLException {
        String sql = "UPDATE agreements SET supplierID = ?, paymentMethod = ?, paymentDate = ?, agreementStatus = ? WHERE agreementID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, agreement.supplierID());
            ps.setString(2, agreement.paymentMethod());
            LocalDate ld = agreement.paymentDate();
            int paymentDateInt = ld.getYear() * 10000 + ld.getMonthValue() * 100 + ld.getDayOfMonth();
            ps.setInt(3, paymentDateInt);
            ps.setBoolean(4, agreement.agreementStatus());
            ps.setInt(5, agreement.agreementID());
            ps.executeUpdate();
        }
    }

    @Override
    public void insertFixedAgreement(int agreementID, String supplyDaysCSV) throws SQLException {
        String sql = "INSERT INTO fixedDeliveryAgreement(agreementID, DayOfSupply) VALUES (?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, agreementID);
            ps.setString(2, supplyDaysCSV);
            ps.executeUpdate();
        }
    }

    @Override
    public void insertFlexibleAgreement(int agreementID, int supplyDays) throws SQLException {
        String sql = "INSERT INTO flexibleDeliveryAgreement(agreementID, SupplyDays) VALUES (?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, agreementID);
            ps.setInt(2, supplyDays);
            ps.executeUpdate();
        }
    }

    @Override
    public void insertPickupAgreement(int agreementID, String address) throws SQLException {
        String sql = "INSERT INTO pickupAgreement(agreementID, address) VALUES (?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, agreementID);
            ps.setString(2, address);
            ps.executeUpdate();
        }
    }

    @Override
    public void insertDiscount(DiscountDTO discount) throws SQLException {
        String sql = "INSERT INTO discounts(catalogID, quantityCond, precentage, agreementID) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, discount.catalogID());
            ps.setInt(2, discount.quantityCond());
            ps.setDouble(3, discount.precentage());
            ps.setInt(4, discount.agreementID());
            ps.executeUpdate();
        }
    }

    @Override
    public void updateDiscount(DiscountDTO discount) throws SQLException {
        String sql = "UPDATE discounts SET quantityCond = ?, precentage = ? WHERE agreementID = ? AND catalogID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, discount.quantityCond());
            ps.setDouble(2, discount.precentage());
            ps.setInt(3, discount.agreementID());
            ps.setInt(4, discount.catalogID());
            ps.executeUpdate();
        }
    }

    @Override
    public List<agreementDTO> getAllAgreementsForSupplier(int supplierID) throws SQLException {
        List<agreementDTO> agreements = new ArrayList<>();
        String sql = "SELECT * FROM agreements WHERE supplierID = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplierID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int agreementID = rs.getInt("agreementID");
                    String paymentMethod = rs.getString("paymentMethod");

                    int dateInt = rs.getInt("paymentDate");
                    LocalDate paymentDate = parseDateFromInt(dateInt);

                    boolean status = rs.getBoolean("agreementStatus");

                    agreements.add(new agreementDTO(agreementID, supplierID, paymentMethod, paymentDate, status));
                }
            }
        }

        return agreements;
    }

    private LocalDate parseDateFromInt(int dateInt) {
        int year = dateInt / 10000;
        int month = (dateInt / 100) % 100;
        int day = dateInt % 100;
        return LocalDate.of(year, month, day);
    }


    @Override
    public List<DiscountDTO> getDiscountsForSupplier(int supplierID) throws SQLException {
        List<DiscountDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM discounts WHERE agreementID IN (SELECT agreementID FROM agreements WHERE supplierID = ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplierID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DiscountDTO dto = new DiscountDTO(
                            rs.getInt("catalogID"),
                            rs.getInt("quantityCond"),
                            rs.getDouble("precentage"),
                            rs.getInt("agreementID")
                    );
                    list.add(dto);
                }
            }
        }
        return list;
    }

    @Override
    public ArrayList<DayOfWeek> getFixedSupplyDays(int agreementID) throws SQLException {
        ArrayList<DayOfWeek> days = new ArrayList<>();
        String sql = "SELECT DayOfSupply FROM fixedDeliveryAgreement WHERE agreementID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, agreementID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String[] split = rs.getString("DayOfSupply").split(",");
                    for (String s : split) {
                        days.add(DayOfWeek.valueOf(s.trim().toUpperCase()));
                    }
                }
            }
        }
        return days;
    }

    @Override
    public int getFlexibleSupplyDays(int agreementID) throws SQLException {
        String sql = "SELECT SupplyDays FROM flexibleDeliveryAgreement WHERE agreementID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, agreementID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("SupplyDays");
            }
        }
        return 0;
    }

    @Override
    public String getPickupAddress(int agreementID) throws SQLException {
        String sql = "SELECT locationID FROM pickupAgreement WHERE agreementID = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, agreementID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("locationID");
            }
        }
        return null;
    }


    @Override
    public List<Integer> getAgreementsWithSupplyTomorrow(int tomorrowDay) throws SQLException {
        if (tomorrowDay < 0 || tomorrowDay > 6) {
            throw new IllegalArgumentException("dayOfWeek must be 0–6 (Sun–Sat)");
        }
        final String sql =
                "SELECT agreementID " +
                        "FROM fixedDeliveryAgreement " +
                        "WHERE SUBSTR(DayOfSupply, ?, 1) = '1'";

        List<Integer> ids = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, tomorrowDay );      // +1 → 1-based עבור SUBSTR
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("agreementID"));
                }
            }
        }
        return ids;
    }




}
