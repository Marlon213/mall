package com.ay.mall.service;

import com.ay.mall.common.ServerResponse;
import com.ay.mall.pojo.Product;
import com.ay.mall.vo.ProductVO;
import com.github.pagehelper.PageInfo;

public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductVO> getDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(Integer pageSize, Integer pageNum);

    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    ServerResponse<ProductVO> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
