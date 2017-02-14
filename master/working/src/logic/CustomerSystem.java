package logic;

import entities.Customer;
import entities.CustomerType;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import java.util.*;
import static logic.CriterionOperator.*;

/**
 * Created by EBUBECHUKWU on 14/02/2017.
 */
public class CustomerSystem {

    private static CustomerSystem instance;
    private CriterionRepository persistence;


    private CustomerSystem(CriterionRepository persistence)
    {
        this.persistence = persistence;
    }

    public static CustomerSystem getInstance(CriterionRepository persistence)
    {
        if(instance == null)
        {
            instance = new CustomerSystem(persistence);
        }
        return instance;
    }

}
