package com.dankdevsstudio.osclearance;

import java.util.Date;

public class Clearance {
    private String nric;
    private String name;
    private String rank;
    private String formation;
    private String unit;
    private String company;
    private String hpNum;
    private String officeNum;
    private String purpose;
    private String remarks;
    private String pocRank;
    private String pocName;
    private String vehicleType;
    private String vehicleNum;
    private Date startDate;
    private Date endDate;

    public Clearance(Date startDate, Date endDate, String name, String nric, String rank,
                     String formation, String unit, String company, String hpNum,
                     String officeNum, String purpose, String remarks, String pocRank,
                     String pocName, String vehicleType, String vehicleNum) {
        this.name = name;
        this.nric = nric;
        this.rank = rank;
        this.formation = formation;
        this.unit = unit;
        this.company = company;
        this.hpNum = hpNum;
        this.officeNum = officeNum;
        this.purpose = purpose;
        this.remarks = remarks;
        this.pocRank = pocRank;
        this.pocName = pocName;
        this.vehicleType = vehicleType;
        this.vehicleNum = vehicleNum;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getNric() {
        return nric.toUpperCase();
    }

    public String getName() {
        return name.toUpperCase();
    }

    public String getRank() {
        return rank;
    }

    public String getFormation() {
        return formation;
    }

    public String getUnit() {
        return unit;
    }

    public String getCompany() {
        return company;
    }

    public String getHpNum() {
        return hpNum;
    }

    public String getOfficeNum() {
        return officeNum;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getPocRank() {
        return pocRank;
    }

    public String getPocName() {
        return pocName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
