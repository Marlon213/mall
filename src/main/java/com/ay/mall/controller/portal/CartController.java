package com.ay.mall.controller.portal;

import com.ay.mall.common.Const;
import com.ay.mall.common.ResponseCode;
import com.ay.mall.common.ServerResponse;
import com.ay.mall.pojo.User;
import com.ay.mall.service.ICartService;
import com.ay.mall.util.CookieUtil;
import com.ay.mall.util.JSONUtil;
import com.ay.mall.util.RedisShardedPoolUtil;
import com.ay.mall.vo.CartVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private ICartService iCartService;

    @RequestMapping("list")
    public ServerResponse<CartVO> list(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JSONUtil.string2Obj(userJsonStr,User.class);

        return iCartService.list(user.getId());
    }

    @RequestMapping("add")
    public ServerResponse<CartVO> add(HttpServletRequest httpServletRequest,Integer productId,Integer count){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JSONUtil.string2Obj(userJsonStr,User.class);
        return iCartService.add(user.getId(),productId,count);
    }

    @RequestMapping("update")
    public ServerResponse<CartVO> update(HttpServletRequest httpServletRequest, Integer count, Integer productId){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JSONUtil.string2Obj(userJsonStr,User.class);

        return iCartService.update(user.getId(),productId,count);
    }

    @RequestMapping("delete_product")
    public ServerResponse<CartVO> deleteProduct(HttpServletRequest httpServletRequest,String productIds){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JSONUtil.string2Obj(userJsonStr,User.class);

        return iCartService.deleteProduct(user.getId(),productIds);
    }

    @RequestMapping("select_all")
    public ServerResponse<CartVO> selectAll(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JSONUtil.string2Obj(userJsonStr,User.class);
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);
    }

    @RequestMapping("un_select_all")
    public ServerResponse<CartVO> unSelectAll(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JSONUtil.string2Obj(userJsonStr,User.class);
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.UN_CHECKED);
    }



    @RequestMapping("select")
    public ServerResponse<CartVO> select(HttpServletRequest httpServletRequest,Integer productId){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JSONUtil.string2Obj(userJsonStr,User.class);
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);
    }

    @RequestMapping("un_select")
    public ServerResponse<CartVO> unSelect(HttpServletRequest httpServletRequest,Integer productId){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JSONUtil.string2Obj(userJsonStr,User.class);
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);
    }



    @RequestMapping("get_cart_product_count")
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest httpServletRequest){
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        String userJsonStr = RedisShardedPoolUtil.get(loginToken);
        User user = JSONUtil.string2Obj(userJsonStr,User.class);
        if(user ==null){
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }

}
