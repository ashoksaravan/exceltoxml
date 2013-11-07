package com.sample;

import java.util.ArrayList;
import java.util.List;

public class EventTO {

	/**
	 * compTxns.
	 */
	private List compTxns = new ArrayList();

	/**
	 * @return the compTxns
	 */
	public List getCompTxns() {
		return compTxns;
	}

	/**
	 * @param compTxnsIn the compTxns to set
	 */
	public void setCompTxns(List compTxnsIn) {
		this.compTxns = compTxnsIn;
	}
}
