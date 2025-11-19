package Domain.InventoryDomain;

import DTO.InventoryDTO.InventoryStatus;

import java.time.DateTimeException;
import java.time.LocalDate;

public class DateOrganizer {
    //Fields
    private LocalDate currentDate;

    //Constructor
    public DateOrganizer(){
        this.currentDate = LocalDate.now();
    }

    //Functions
    public LocalDate getDate(){return currentDate;}

    //need to set date manually
    public InventoryStatus setDate(int day, int month, int year) {
        if (day > 31 || day < 1 || month > 12 || month < 1)
            return InventoryStatus.Failure;
        try {
            this.currentDate = LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            return InventoryStatus.Failure;
        }
        return InventoryStatus.Success;
    }
    
    //given 3 dates, check if checkDate is in range (include edges).
    public boolean inRange(LocalDate startDate, LocalDate endDate, LocalDate checkDate ){
        return (checkDate.isEqual(startDate) || (checkDate.isAfter(startDate) &&
                checkDate.isBefore(endDate)) || checkDate.isEqual(endDate));
    }
}
