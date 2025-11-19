package DataAccess.SupplierDAO;

import DTO.SupplierDTO.supplierProductDTO;

import java.sql.SQLException;
import java.util.List;

public interface supplierProductDAO {
    void insertSupplierProduct(supplierProductDTO dto) throws SQLException;
    void updateSupplierProduct(supplierProductDTO dto) throws SQLException ;
    List<supplierProductDTO> getProductsForSupplier(int supplierID) throws SQLException;
    int getProductIdByName(String productName);
    List<supplierProductDTO> getProductsByAgreementID(int agreementID) throws SQLException ;
    }