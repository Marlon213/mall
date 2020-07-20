package com.ay.mall.controller.portal;

import com.ay.mall.common.ServerResponse;
import com.ay.mall.service.IProductService;
import com.ay.mall.vo.ProductVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;


// @RequestMapping(value = "/{productId}", method = RequestMethod.GET)  restful
    @RequestMapping("detail")
    public ServerResponse<ProductVO> detail(Integer productId){
        return iProductService.getProductDetail(productId);
    }
//    @RequestMapping(value = "/{keyword}/{categoryId}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
//对于默认的
    @RequestMapping("list")
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword",required = false)String keyword,
                                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return iProductService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }





}
