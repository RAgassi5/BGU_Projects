package Domain.SupplierDomain;

import DTO.SupplierDTO.agreementDetailsDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface agreementRepository {
    public int createPickUp(int supplierID, String paymentMethod, String adress);

    public int createFixed(String paymentMethod, int supplierID, ArrayList<DayOfWeek> supplyDays);

    public int createFlexible(String paymentMethod,int supplierID,int supplyDays );

    public boolean agreementExists(int agreementID);

    public void addDiscount(int agreementID, int catalogID, int supplierID, int quantityCond,double percentage);

    public void changeStatus(int supplierID,int agreementID, boolean agreementStatus);

    public double getPercentage(int catalogID,int amount) ;

    public boolean checkValid(String type1,int supplierID);

    public boolean existAgreement(int supplierID,int agreementID);

    public void changePrecentageInDiscount(int supplierID,int agreementID, double percentage);

    public void changeQuantityCondInDiscount(int supplierID,int agreementID, int quantityCond);

    public void displayAgreement(int supplierID,int agreementID);

    public void loadAgreementsFromDatabase(int supplierID);
    agreementDetailsDTO getAgreementDetails(int agreementID, int supplierID, LocalDate orderDate) ;
    public List<Integer> getAgreementsIdsForTomorrow();
    public String getAgreementType(int agreementID) ;




    }

