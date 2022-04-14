package com.example.civiladvocacyapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CivicInformation implements Serializable{
    private final String normalizedInput_line1;
    private final String normalizedInput_city;
    private final String normalizedInput_state;
    private final String normalizedInput_zip;
    private final ArrayList<Offices> offices;
    private final ArrayList<Officials> officials;

    public CivicInformation(String normalizedInput_line1, String normalizedInput_city, String normalizedInput_state, String normalizedInput_zip, ArrayList<Offices> offices, ArrayList<Officials> officials) {
        this.normalizedInput_line1 = normalizedInput_line1;
        this.normalizedInput_city = normalizedInput_city;
        this.normalizedInput_state = normalizedInput_state;
        this.normalizedInput_zip = normalizedInput_zip;
        this.offices = offices;
        this.officials = officials;
    }

    public String getNormalizedInput_line1() {
        return normalizedInput_line1;
    }

    public String getNormalizedInput_city() {
        return normalizedInput_city;
    }

    public String getNormalizedInput_state() {
        return normalizedInput_state;
    }

    public String getNormalizedInput_zip() {
        return normalizedInput_zip;
    }

    public ArrayList<Offices> getOffices() {
        return offices;
    }

    public ArrayList<Officials> getOfficials() {
        return officials;
    }
}

class Offices implements Serializable{
    private final String offices_name;
    private final ArrayList<Integer> offices_officialIndices;

    Offices(String offices_name, ArrayList<Integer> offices_officialIndices) {
        this.offices_name = offices_name;
        this.offices_officialIndices = offices_officialIndices;
    }

    public String getOffices_name() {
        return offices_name;
    }

    public ArrayList<Integer> getOffices_officialIndices() {
        return offices_officialIndices;
    }
}

class Officials implements Serializable {
    private final String officials_name;
    private final String officials_address;
    private final String officials_city;
    private final String officials_state;
    private final String officials_zip;
    private final String officials_party;
    private final String officials_phone;
    private final String officials_url;
    private final String officials_email;
    private final String officials_photourl;
    private final ArrayList<Channels> officials_channels;

    Officials(String officials_name, String officials_address, String officials_city, String officials_state, String officials_zip, String officials_party, String officials_phone, String officials_url, String officials_email, String officials_photourl, ArrayList<Channels> officials_channels) {
        this.officials_name = officials_name;
        this.officials_address = officials_address;
        this.officials_city = officials_city;
        this.officials_state = officials_state;
        this.officials_zip = officials_zip;
        this.officials_party = officials_party;
        this.officials_phone = officials_phone;
        this.officials_url = officials_url;
        this.officials_email = officials_email;
        this.officials_photourl = officials_photourl;
        this.officials_channels = officials_channels;
    }

    public String getOfficials_name() {
        return officials_name;
    }

    public String getOfficials_address() {
        return officials_address;
    }

    public String getOfficials_city() {
        return officials_city;
    }

    public String getOfficials_state() {
        return officials_state;
    }

    public String getOfficials_zip() {
        return officials_zip;
    }

    public String getOfficials_party() {
        return officials_party;
    }

    public String getOfficials_phone() {
        return officials_phone;
    }

    public String getOfficials_url() {
        return officials_url;
    }

    public String getOfficials_email() {
        return officials_email;
    }

    public String getOfficials_photourl() {
        return officials_photourl;
    }

    public ArrayList<Channels> getOfficials_channels() {
        return officials_channels;
    }
}

class Channels implements Serializable{
    private final String channels_type;
    private final String channels_id;

    Channels(String channels_type, String channels_id) {
        this.channels_type = channels_type;
        this.channels_id = channels_id;
    }

    public String getChannels_type() {
        return channels_type;
    }

    public String getChannels_id() {
        return channels_id;
    }
}
