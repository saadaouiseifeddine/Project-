
package com.total.gra.portlets.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class SearchForm {

//	@NotNull(message = "{FormValidation.validation_required_input}")
//	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private String priceDate;

	private String country;

	private String product;

	private String typeScalePrice;

	public String getPriceDate() {

		return priceDate;
	}

	public void setPriceDate(String priceDate) {

		this.priceDate = priceDate;
	}

	public String getCountry() {

		return country;
	}

	public void setCountry(String country) {

		this.country = country;
	}

	public String getProduct() {

		return product;
	}

	public void setProduct(String product) {

		this.product = product;
	}

	public String getTypeScalePrice() {

		return typeScalePrice;
	}

	public void setTypeScalePrice(String typeScalePrice) {

		this.typeScalePrice = typeScalePrice;
	}

	@Override
	public String toString() {

		return "UpdateForm [priceDate=" + priceDate + ", country=" + country +
			", product=" + product + ", typeScalePrice=" + typeScalePrice + "]";
	}

}
