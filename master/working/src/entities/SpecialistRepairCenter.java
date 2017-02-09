package entities;

import logic.Criterion;

/**
 * @author muradahmed
 * @version 0.1
 * @since : 0.1
 */

public class SpecialistRepairCenter implements Criterion
{


        private String spcID;
        private String name;
        private String address;
        private String phone;
        private String email;


        public SpecialistRepairCenter(String spcID, String name, String address, String phone, String email)
        {
            this.spcID = spcID;
            this.name = name;
            this.address = address;
            this.phone = phone;
            this.email = email;
        }

    public String getSpcID() {
        return this.spcID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getEmail() {
        return this.email;
    }

}

