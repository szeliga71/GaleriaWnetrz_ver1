package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import szeliga71.pl.wp.galeriawnetrz_ver1.model.Product;

import java.util.List;

public class QueryResultsDto {
    private List<Product> brandResults;
    private List<Product> categoryResults;
    private List<Product> subCategoriesResults;
    private List<Product> productResults;

    public QueryResultsDto(List<Product> brandResults, List<Product> productResults, List<Product> subCategoriesResults, List<Product> categoryResults) {
        this.brandResults = brandResults;
        this.productResults = productResults;
        this.subCategoriesResults = subCategoriesResults;
        this.categoryResults = categoryResults;
    }

    public List<Product> getBrandResults() {
        return brandResults;
    }

    public void setBrandResults(List<Product> brandResults) {
        this.brandResults = brandResults;
    }

    public List<Product> getProductResults() {
        return productResults;
    }

    public void setProductResults(List<Product> productResults) {
        this.productResults = productResults;
    }

    public List<Product> getSubCategoriesResults() {
        return subCategoriesResults;
    }

    public void setSubCategoriesResults(List<Product> subCategoriesResults) {
        this.subCategoriesResults = subCategoriesResults;
    }

    public List<Product> getCategoryResults() {
        return categoryResults;
    }

    public void setCategoryResults(List<Product> categoryResults) {
        this.categoryResults = categoryResults;
    }
}

