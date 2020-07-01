package com.ay.mall.service;

import com.ay.mall.common.ServerResponse;
import com.ay.mall.vo.CartVO;

public interface ICartService {
    ServerResponse<CartVO> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVO> update(Integer userId,Integer productId,Integer count);

    ServerResponse<CartVO> deleteProduct(Integer userId,String productIds);

    ServerResponse<CartVO> list (Integer userId);

    ServerResponse<CartVO> selectOrUnSelect (Integer userId,Integer productId,Integer checked);
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
