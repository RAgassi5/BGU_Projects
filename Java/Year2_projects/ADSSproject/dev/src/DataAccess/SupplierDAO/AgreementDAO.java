package DataAccess.SupplierDAO;

import DTO.SupplierDTO.agreementDTO;
import DTO.SupplierDTO.DiscountDTO;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public interface AgreementDAO {

    int insertAgreement(agreementDTO agreement) throws SQLException;
    void updateAgreement(agreementDTO agreement) throws SQLException;

    void insertFixedAgreement(int agreementID, String supplyDaysCSV) throws SQLException;
    void insertFlexibleAgreement(int agreementID, int supplyDays) throws SQLException;
    void insertPickupAgreement(int agreementID, String address) throws SQLException;

    void insertDiscount(DiscountDTO discount) throws SQLException;
    void updateDiscount(DiscountDTO discount) throws SQLException;

    List<agreementDTO> getAllAgreementsForSupplier(int supplierID) throws SQLException;
    List<DiscountDTO> getDiscountsForSupplier(int supplierID) throws SQLException;
    ArrayList<DayOfWeek> getFixedSupplyDays(int agreementID) throws SQLException;
    int getFlexibleSupplyDays(int agreementID) throws SQLException;
    String getPickupAddress(int agreementID) throws SQLException;
    List<Integer> getAgreementsWithSupplyTomorrow(int tomorrowDay) throws SQLException ;

    }
