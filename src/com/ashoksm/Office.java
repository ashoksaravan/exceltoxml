package com.ashoksm;

public class Office {

	private String name = "";

	private String pinCode = "";

	private String status = "";

	private String suboffice = "";

	private String headoffice = "";

	private String location = "";

	private String telephone = "";

	public Office(){
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param nameIn the name to set
	 */
	public void setName(String nameIn) {
		this.name = nameIn;
	}

	/**
	 * @return the pinCode
	 */
	public String getPinCode() {
		return pinCode;
	}

	/**
	 * @param pinCodeIn the pinCode to set
	 */
	public void setPinCode(String pinCodeIn) {
		this.pinCode = pinCodeIn;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param statusIn the status to set
	 */
	public void setStatus(String statusIn) {
		this.status = statusIn;
	}

	/**
	 * @return the suboffice
	 */
	public String getSuboffice() {
		return suboffice;
	}

	/**
	 * @param subofficeIn the suboffice to set
	 */
	public void setSuboffice(String subofficeIn) {
		this.suboffice = subofficeIn;
	}

	/**
	 * @return the headoffice
	 */
	public String getHeadoffice() {
		return headoffice;
	}

	/**
	 * @param headofficeIn the headoffice to set
	 */
	public void setHeadoffice(String headofficeIn) {
		this.headoffice = headofficeIn;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param locationIn the location to set
	 */
	public void setLocation(String locationIn) {
		this.location = locationIn;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephoneIn the telephone to set
	 */
	public void setTelephone(String telephoneIn) {
		this.telephone = telephoneIn;
	}
}
