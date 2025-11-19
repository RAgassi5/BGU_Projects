package DataAccess.SupplierDAO;

import DTO.SupplierDTO.supplierDTO;

import java.sql.SQLException;
import java.util.List;

public interface supplierDAO {
    supplierDTO getSupplier(int supplierID) throws SQLException;
    int insertSupplier(supplierDTO supplier) throws SQLException ;
    public void updateSupplier(supplierDTO supplier) throws SQLException ;
    int insertContact(int supplierID, String name, String phone) throws SQLException;
    void insertProductType(int supplierID, String productType) throws SQLException;
    void insertManufacturer(int supplierID, String manufacturer) throws SQLException;

    List<String> getSupplierProductTypes(int supplierID) throws SQLException;
    List<String> getmanufacturersBySupplier(int supplierID) throws SQLException;
    List<supplierDTO> getActiveSuppliers() throws SQLException ;

    }

