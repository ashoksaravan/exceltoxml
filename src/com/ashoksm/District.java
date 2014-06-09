package com.ashoksm;

public class District {

	private String stateName;
	
	private String districtName;
	
	private String bankName;

	/**
	 * @return the stateName
	 */
	public final String getStateName() {
		return stateName;
	}

	/**
	 * @param stateName the stateName to set
	 */
	public final void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/**
	 * @return the districtName
	 */
	public final String getDistrictName() {
		return districtName;
	}

	/**
	 * @param districtName the districtName to set
	 */
	public final void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	
	@Override
	public String toString() {
		return stateName + " : " + districtName;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
}
