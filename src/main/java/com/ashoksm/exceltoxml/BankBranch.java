package com.ashoksm.exceltoxml;

public class BankBranch {

    public BankBranch() {
    }

    /**
     * name.
     */
    private String name;

    /**
     * city.
     */
    private String city;

    /**
     * address.
     */
    private String address;

    /**
     * contact.
     */
    private String contact;

    /**
     * micrCode.
     */
    private String micrCode;

    /**
     * ifscCode.
     */
    private String ifscCode;

    /**
     * bankLoc.
     */
    private Integer bankLoc;

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * @return the city
     */
    public final String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public final void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the address
     */
    public final String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public final void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the contact
     */
    public final String getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public final void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return the micrCode
     */
    public final String getMicrCode() {
        return micrCode;
    }

    /**
     * @param micrCode the micrCode to set
     */
    public final void setMicrCode(String micrCode) {
        this.micrCode = micrCode;
    }

    /**
     * @return the ifscCode
     */
    public final String getIfscCode() {
        return ifscCode;
    }

    /**
     * @param ifscCode the ifscCode to set
     */
    public final void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    /**
     * @return the bankLoc
     */
    public Integer getBankLoc() {
        return bankLoc;
    }

    /**
     * @param bankLoc the bankLoc to set
     */
    public void setBankLoc(Integer bankLoc) {
        this.bankLoc = bankLoc;
    }
}
