package com.sample;

import java.io.Serializable;

public class CompTxnTO implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private String OrderID;

	/**
	 * @return the orderID
	 */
	public String getOrderID() {
		return OrderID;
	}

	/**
	 * @param orderIDIn the orderID to set
	 */
	public void setOrderID(String orderIDIn) {
		OrderID = orderIDIn;
	}

}
