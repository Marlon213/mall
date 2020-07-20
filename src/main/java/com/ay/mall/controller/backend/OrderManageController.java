package com.ay.mall.controller.backend;

import com.ay.mall.common.Const;
import com.ay.mall.common.ServerResponse;
import com.ay.mall.pojo.User;
import com.ay.mall.service.IOrderService;
import com.ay.mall.service.IUserService;
import com.ay.mall.vo.OrderVO;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/order")
public class OrderManageController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("list")
    public ServerResponse<PageInfo> orderList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
            return iOrderService.manageList(pageNum,pageSize);
    }

    @RequestMapping("detail")
    public ServerResponse<OrderVO> orderDetail(HttpSession session, Long orderNo){
            return iOrderService.manageDetail(orderNo);
    }



    @RequestMapping("search")
    public ServerResponse<PageInfo> orderSearch(HttpSession session, Long orderNo,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
            return ServerResponse.createByErrorMessage("无权限操作");
    }



    @RequestMapping("send_goods")
    public ServerResponse<String> orderSendGoods(HttpSession session, Long orderNo){
            return iOrderService.manageSendGoods(orderNo);
    }
}
