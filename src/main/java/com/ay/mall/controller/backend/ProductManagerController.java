package com.ay.mall.controller.backend;

import com.ay.mall.common.Const;
import com.ay.mall.common.ServerResponse;
import com.ay.mall.pojo.Product;
import com.ay.mall.pojo.User;
import com.ay.mall.service.IFileService;
import com.ay.mall.service.IProductService;
import com.ay.mall.util.PropertiesUtil;
import com.ay.mall.vo.ProductVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/manage/product/")
@Slf4j
public class ProductManagerController {

    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    @RequestMapping(value = "save" ,method = RequestMethod.GET)
    public ServerResponse productSave(HttpSession session, Product product){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user.getRole()==Const.Role.ROLE_ADMIN){
            //填充我们增加产品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("set_sale_status")
    public ServerResponse setSaleStatus(HttpSession session, Integer productId,Integer status){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user.getRole()==Const.Role.ROLE_ADMIN){
            return iProductService.setSaleStatus(productId,status);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("detail")
    public ServerResponse<ProductVO> getDetail(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user.getRole()!=Const.Role.ROLE_ADMIN){
            return ServerResponse.createByErrorMessage("无权限操作");
        }
        return iProductService.getDetail(productId);
    }

    @RequestMapping(value = "list",method = RequestMethod.GET)
    public ServerResponse<PageInfo> getProductList(HttpSession session,
                                                   @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                                   @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user.getRole()!=Const.Role.ROLE_ADMIN){
            return ServerResponse.createByErrorMessage("无权限操作");
        }
        return iProductService.getProductList(pageSize,pageNum);
    }

    @RequestMapping("search")
    public ServerResponse productSearch(HttpSession session,String productName,Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user.getRole()!=Const.Role.ROLE_ADMIN){
            return ServerResponse.createByErrorMessage("无权限操作");
        }
        return iProductService.searchProduct(productName,productId,pageNum,pageSize);
    }

    @RequestMapping(value = "upload" ,method = RequestMethod.POST)
    public ServerResponse<Map<String,String>> upload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user.getRole()!=Const.Role.ROLE_ADMIN){
            return ServerResponse.createByErrorMessage("无权限操作");
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iFileService.upload(file,path);
    }

    //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
    @RequestMapping("richtext_img_upload")
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> resultMap = Maps.newHashMap();
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user.getRole() == Const.Role.ROLE_ADMIN) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path).getData().get("uri");
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("src/main/resources.dev/mall.properties", "ftp.server.http.prefix") + targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }
    }
}
