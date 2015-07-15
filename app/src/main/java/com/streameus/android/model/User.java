package com.streameus.android.model;

import java.lang.reflect.Field;
import java.util.HashMap;

public class User {
    private String  TAG = "USER-CLASS";

    private Integer Id;
    private String  Pseudo;
    private String  Email;
    private String  FirstName;
    private String  LastName;
    private Boolean Gender;
    private Double  Reputation;
    private String  BirthDay;
    private String  Phone;
    private String  Address;
    private String  City;
    private String  Country;
    private String  Website;
    private String  Profession;
    private String  Description;
    private Integer Followers;
    private Integer Followings;
    private Integer Conferences;
    private Boolean isloggedUser = false;

    HashMap<String, String> properties;
    private String[] stringPropertydisplayable = { "Pseudo", "Email",
            "FirstName", "LastName", "BirthDay", "Phone", "Address", "City",
            "Country", "Website", "Profession", "Description" };


    public HashMap<String, String> getPropertyDisplayable() {
        HashMap<String, String> out = new HashMap<String, String>();
        Object o = this;
        Class<?> c = o.getClass();
        Field f;
        String value;

        for (int i = 0; i < stringPropertydisplayable.length; ++i) {

            try {
                f = c.getDeclaredField(stringPropertydisplayable[i]);
                f.setAccessible(true);
                value = (String) f.get(o);
                if (value != null && !value.equals("") && !value.equals("null")) {
                    out.put(stringPropertydisplayable[i], value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return out;
    }

    public Integer getId() {
        return Id;
    }

    public String getPseudo() {
        return Pseudo;
    }

    public String getEmail() {
        return Email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public Boolean getGender() {
        return Gender;
    }

    public Double getReputation() {
        return Reputation;
    }

    public String getBirthDay() {
        return BirthDay;
    }

    public String getPhone() {
        return Phone;
    }

    public String getAddress() {
        return Address;
    }

    public String getCity() {
        return City;
    }

    public String getCountry() {
        return Country;
    }

    public String getWebsite() {
        return Website;
    }

    public String getProfession() {
        return Profession;
    }

    public String getDescription() {
        return Description;
    }

    public Boolean getIsloggedUser() {
        return isloggedUser;
    }

    public void setIsloggedUser(Boolean isloggedUser) {
        this.isloggedUser = isloggedUser;
    }
}
