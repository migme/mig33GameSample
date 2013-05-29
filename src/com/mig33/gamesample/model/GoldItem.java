/**
 * Mig33 Pte. Ltd.
 *
 * Copyright (c) 2012 mig33. All rights reserved.
 */
package com.mig33.gamesample.model;

import java.util.Locale;

/**
 * GoldItem.java
 * 
 * @author warrenbalcos on May 22, 2013
 * 
 */
public class GoldItem {
	
	/**
	 * Price in USD
	 */
	private int	price;
	
	/**
	 * Gold pieces
	 */
	private int	amount;
	
	/**
	 * @param price
	 * @param amount
	 */
	public GoldItem(int price, int amount) {
		this.price = price;
		this.amount = amount;
	}
	
	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}
	
	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	public String getDescription() {
		return String.format(Locale.ENGLISH, "%d gold for %d USD", new Object[] { amount,
				price });
	}
}
