/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycompany.api.endpoint.catalog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.common.media.domain.Media;
import org.broadleafcommerce.common.media.domain.MediaImpl;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.web.api.wrapper.CategoriesWrapper;
import org.broadleafcommerce.core.web.api.wrapper.CategoryAttributeWrapper;
import org.broadleafcommerce.core.web.api.wrapper.CategoryWrapper;
import org.broadleafcommerce.core.web.api.wrapper.MediaWrapper;
import org.broadleafcommerce.core.web.api.wrapper.ProductAttributeWrapper;
import org.broadleafcommerce.core.web.api.wrapper.ProductWrapper;
import org.broadleafcommerce.core.web.api.wrapper.RelatedProductWrapper;
import org.broadleafcommerce.core.web.api.wrapper.SearchResultsWrapper;
import org.broadleafcommerce.core.web.api.wrapper.SkuAttributeWrapper;
import org.broadleafcommerce.core.web.api.wrapper.SkuWrapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mycompany.api.wrapper.SageProductWrapper;
import com.mycompany.api.wrapper.SageSkuWrapper;
import com.mycompany.domain.BaseProduct;
import com.mycompany.domain.Book;
import com.mycompany.domain.Electronic;
import com.mycompany.domain.Journal;

/**
 * This is a reference REST API endpoint for catalog. This can be modified, used as is, or removed. 
 * The purpose is to provide an out of the box RESTful catalog service implementation, but also 
 * to allow the implementor to have fine control over the actual API, URIs, and general JAX-RS annotations.
 * 
 * @author Kelly Tisdell
 *
 */
@Component
@Scope("singleton")
@Path("/catalog/")
@Produces(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes(value = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class CatalogEndpoint extends
        org.broadleafcommerce.core.web.api.endpoint.catalog.CatalogEndpoint {
	
	private static final Log log = LogFactory.getLog(CatalogEndpoint.class);

    @POST
	@Path("product")
    public SageProductWrapper createProduct(@Context HttpServletRequest request, SageProductWrapper wrapper) throws Exception{
    	
    	String type = wrapper.getType();
    	
    	
    	BaseProduct prd = type.equalsIgnoreCase("Electronic") ? new Electronic() 
    						: ( type.equalsIgnoreCase("Journal") ? new Journal() 
    							: new Book() );

    	Collections.sort(wrapper.getSkus());
    	
    	prd.setUrl("/"  + type.toLowerCase() + "s/" + wrapper.getProductId());
    	
    	List<Sku> skus = new ArrayList<Sku>();
    	for(SageSkuWrapper sw : wrapper.getSkus()){
    		skus.add(sw.unwrap(request, this.context));
    	}
    	
    	prd.setDefaultSku(skus.get(0));
    	skus.remove(0);
    	prd.setAdditionalSkus(skus);

    	if(!StringUtils.isEmpty(wrapper.getThumb())){
    		Media m = new MediaImpl();
    		m.setUrl(wrapper.getThumb());
    		
    		Map<String,Media> media = new HashMap<String, Media>();
    		media.put("primary", m);
    		prd.setMedia(media);
    	}
    	
    	wrapper.populate(prd);
    	
    	catalogService.saveProduct(prd);

    	log.info("product saved");
    	
    	return null;
    }
     

    @Override
    @GET
    @Path("product/{id}")
    public ProductWrapper findProductById(@Context HttpServletRequest request, @PathParam("id") Long id) {
        return super.findProductById(request, id);
    }

    @Override
    @GET
    @Path("search/products")
    public SearchResultsWrapper findProductsByQuery(@Context HttpServletRequest request,
            @QueryParam("q") String q,
            @QueryParam("pageSize") @DefaultValue("15") Integer pageSize,
            @QueryParam("page") @DefaultValue("1") Integer page) {
        return super.findProductsByQuery(request, q, pageSize, page);
    }

    @Override
    @GET
    @Path("search/category/{categoryId}/products")
    public SearchResultsWrapper findProductsByCategoryAndQuery(@Context HttpServletRequest request,
            @PathParam("categoryId") Long categoryId,
            @QueryParam("q") String q,
            @QueryParam("pageSize") @DefaultValue("15") Integer pageSize,
            @QueryParam("page") @DefaultValue("1") Integer page) {
        return super.findProductsByCategoryAndQuery(request, categoryId, q, pageSize, page);
    }

    @Override
    @GET
    @Path("product/{id}/skus")
    public List<SkuWrapper> findSkusByProductById(@Context HttpServletRequest request, @PathParam("id") Long id) {
        return super.findSkusByProductById(request, id);
    }

    @Override
    @GET
    @Path("product/{id}/defaultSku")
    public SkuWrapper findDefaultSkuByProductId(@Context HttpServletRequest request, @PathParam("id") Long id) {
        return super.findDefaultSkuByProductId(request, id);
    }

    @Override
    @GET
    @Path("categories")
    public CategoriesWrapper findAllCategories(@Context HttpServletRequest request,
            @QueryParam("name") String name,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset) {
        return super.findAllCategories(request, name, limit, offset);
    }

    @Override
    @GET
    @Path("category/{id}/categories")
    public CategoriesWrapper findSubCategories(@Context HttpServletRequest request,
            @PathParam("id") Long id,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("active") @DefaultValue("true") boolean active) {
        return super.findSubCategories(request, id, limit, offset, active);
    }

    @Override
    @GET
    @Path("category/{id}/activeSubcategories")
    public CategoriesWrapper findActiveSubCategories(@Context HttpServletRequest request,
            @PathParam("id") Long id,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset) {
        return super.findActiveSubCategories(request, id, limit, offset);
    }

    @Override
    @GET
    @Path("category/{id}")
    public CategoryWrapper findCategoryById(@Context HttpServletRequest request,
            @PathParam("id") Long id,
            @QueryParam("productLimit") @DefaultValue("20") int productLimit,
            @QueryParam("productOffset") @DefaultValue("1") int productOffset,
            @QueryParam("subcategoryLimit") @DefaultValue("20") int subcategoryLimit,
            @QueryParam("subcategoryOffset") @DefaultValue("1") int subcategoryOffset) {
        return super.findCategoryById(request, id, productLimit, productOffset,
                subcategoryLimit, subcategoryOffset);
    }

    @Override
    @GET
    @Path("category")
    public CategoryWrapper findCategoryByIdOrName(@Context HttpServletRequest request,
            @QueryParam("searchParameter") String searchParameter,
            @QueryParam("productLimit") @DefaultValue("20") int productLimit,
            @QueryParam("productOffset") @DefaultValue("1") int productOffset,
            @QueryParam("subcategoryLimit") @DefaultValue("20") int subcategoryLimit,
            @QueryParam("subcategoryOffset") @DefaultValue("1") int subcategoryOffset) {
        return super.findCategoryByIdOrName(request, searchParameter,
                productLimit, productOffset, subcategoryLimit, subcategoryOffset);
    }

    @Override
    @GET
    @Path("category/{id}/category-attributes")
    public List<CategoryAttributeWrapper> findCategoryAttributesForCategory(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findCategoryAttributesForCategory(request, id);
    }

    @Override
    @GET
    @Path("product/{id}/related-products/upsale")
    public List<RelatedProductWrapper> findUpSaleProductsByProduct(@Context HttpServletRequest request,
            @PathParam("id") Long id,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset) {
        return super.findUpSaleProductsByProduct(request, id, limit, offset);
    }

    @Override
    @GET
    @Path("product/{id}/related-products/crosssale")
    public List<RelatedProductWrapper> findCrossSaleProductsByProduct(@Context HttpServletRequest request,
            @PathParam("id") Long id,
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset) {
        return super.findCrossSaleProductsByProduct(request, id, limit, offset);
    }

    @Override
    @GET
    @Path("product/{id}/product-attributes")
    public List<ProductAttributeWrapper> findProductAttributesForProduct(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findProductAttributesForProduct(request, id);
    }

    @Override
    @GET
    @Path("sku/{id}/sku-attributes")
    public List<SkuAttributeWrapper> findSkuAttributesForSku(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findSkuAttributesForSku(request, id);
    }

    @Override
    @GET
    @Path("sku/{id}/media")
    public List<MediaWrapper> findMediaForSku(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findMediaForSku(request, id);
    }

    @Override
    @GET
    @Path("sku/{id}")
    public SkuWrapper findSkuById(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findSkuById(request, id);
    }

    @Override
    @GET
    @Path("product/{id}/media")
    public List<MediaWrapper> findMediaForProduct(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findMediaForProduct(request, id);
    }

    @Override
    @GET
    @Path("category/{id}/media")
    public List<MediaWrapper> findMediaForCategory(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findMediaForCategory(request, id);
    }

    @Override
    @GET
    @Path("product/{id}/categories")
    public CategoriesWrapper findParentCategoriesForProduct(@Context HttpServletRequest request,
            @PathParam("id") Long id) {
        return super.findParentCategoriesForProduct(request, id);
    }
}
