package com.mycompany.api.wrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.catalog.domain.SkuImpl;
import org.broadleafcommerce.core.web.api.wrapper.APIUnwrapper;
import org.broadleafcommerce.core.web.api.wrapper.SkuWrapper;
import org.springframework.context.ApplicationContext;

public class SageSkuWrapper extends SkuWrapper 
		implements APIUnwrapper<Sku>, Comparable<SageSkuWrapper>{   
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getActiveStartDate() {
		return activeStartDate;
	}

	public void setActiveStartDate(Date activeStartDate) {
		this.activeStartDate = activeStartDate;
	}
	
	public Date getActiveEndDate() {
		return activeEndDate;
	}

	public void setActiveEndDate(Date activeEndDate) {
		this.activeEndDate = activeEndDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Money getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(Money retailPrice) {
		this.retailPrice = retailPrice;
	}

	public Money getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Money salePrice) {
		this.salePrice = salePrice;
	}

	@Override
	public Sku unwrap(HttpServletRequest request, ApplicationContext context) {
		Sku sku = new SkuImpl();
		sku.setId(id);
		sku.setActiveStartDate(activeStartDate);
		sku.setActiveEndDate(activeEndDate);
		sku.setName(name);
		sku.setDescription(description);
		sku.setRetailPrice(retailPrice);
		sku.setSalePrice(salePrice);
		sku.setActiveStartDate(new java.util.Date(0));
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			sku.setActiveEndDate(df.parse("2099-04-05"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sku;
	}

	@Override
	public int compareTo(SageSkuWrapper o) {
		return retailPrice.getAmount().subtract(
					o.retailPrice.getAmount())
				.intValue();
	}

}
