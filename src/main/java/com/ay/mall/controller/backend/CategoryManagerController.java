package com.ay.mall.controller.backend;

import com.ay.mall.common.Const;
import com.ay.mall.common.ServerResponse;
import com.ay.mall.pojo.Category;
import com.ay.mall.pojo.User;
import com.ay.mall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/manage/category/")
public class CategoryManagerController {
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add",method = RequestMethod.POST)
    public ServerResponse<String> addCategory(@RequestParam(defaultValue = "0") Integer parentId, String categoryName, HttpSession session){
        return iCategoryService.addCategory(parentId, categoryName);
    }

    @RequestMapping(value = "update_name",method = RequestMethod.PUT)
    public ServerResponse<String> updateName(HttpSession session,String categoryName,Integer categoryId){
        return iCategoryService.updateName(categoryId,categoryName);
    }

    @RequestMapping(value = "get_category",method = RequestMethod.GET)
    public ServerResponse<List<Category>> getCategory(HttpSession session,@RequestParam(defaultValue = "0") Integer categoryId){
        return iCategoryService.getCategory(categoryId);
    }

    @RequestMapping(value = "get_deep_category",method = RequestMethod.GET)
    public ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(defaultValue = "0") Integer categoryId){
        return iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);
    }
}
