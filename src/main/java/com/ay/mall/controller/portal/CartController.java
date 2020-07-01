package com.ay.mall.controller.portal;

import com.ay.mall.common.Const;
import com.ay.mall.common.ResponseCode;
import com.ay.mall.common.ServerResponse;
import com.ay.mall.pojo.User;
import com.ay.mall.service.ICartService;
import com.ay.mall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private ICartService iCartService;

    @RequestMapping("list")
    public ServerResponse<CartVO> list(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.list(user.getId());
    }

    @RequestMapping("add")
    public ServerResponse<CartVO> add(HttpSession session,Integer productId,Integer count){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.add(user.getId(),productId,count);
    }

    @RequestMapping("update")
    public ServerResponse<CartVO> update(HttpSession session, Integer count, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.update(user.getId(),productId,count);
    }

    @RequestMapping("delete_product")
    public ServerResponse<CartVO> deleteProduct(HttpSession session,String productIds){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteProduct(user.getId(),productIds);
    }

    @RequestMapping("select_all")
    public ServerResponse<CartVO> selectAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
    }

    @RequestMapping("un_select_all")
    public ServerResponse<CartVO> unSelectAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
    }



    @RequestMapping("select")
    public ServerResponse<CartVO> select(HttpSession session,Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);
    }

    @RequestMapping("un_select")
    public ServerResponse<CartVO> unSelect(HttpSession session,Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);
    }



    @RequestMapping("get_cart_product_count")
    public ServerResponse<Integer> getCartProductCount(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }

}
