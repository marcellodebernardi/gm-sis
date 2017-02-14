package entities;

import java.util.Date;

/**
 * Created by shakib on 14/02/2017.
 */
public class Installation {

    private int installationID;
    private Date installationDate;
    private Date endWarrantyDate;
    private String vehicleRegNumber;
    private String customerSurname;
    private String customerFirstName;

    private Installation(int installationID, Date installationDate, Date endWarrantyDate, String vehicleRegNumber, String customerSurname, String customerFirstName){
        this.installationID = installationID;
        this.installationDate = installationDate;
        this.endWarrantyDate = endWarrantyDate;
        this.vehicleRegNumber = vehicleRegNumber;
        this.customerSurname = customerSurname;
        this.customerFirstName = customerFirstName;

    }

    private int installationID(){
        return installationID;
    };
    private Date installationDate(){
        return installationDate;
    }
    private Date endWarrantyDate(){
        return endWarrantyDate;
    }
    private String vehicleRegNumber(){
        return vehicleRegNumber;
    }
    private String customerSurname(){
        return customerSurname();
    }
    private String customerFirstName(){
        return customerFirstName;
    }



}
