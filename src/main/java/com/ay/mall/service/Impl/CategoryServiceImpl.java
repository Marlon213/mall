package com.ay.mall.service.Impl;

import com.ay.mall.common.ServerResponse;
import com.ay.mall.dao.CategoryMapper;
import com.ay.mall.pojo.Category;
import com.ay.mall.service.ICategoryService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse<String> addCategory(Integer parentId,String categoryName){
        if (parentId==null||StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int count=categoryMapper.insertSelective(category);
        if (count==0){
            return ServerResponse.createByErrorMessage("添加失败");
        }
        return ServerResponse.createBySuccessMessage("添加成功");
    }

    public ServerResponse<String> updateName(Integer categoryId,String categoryName){
        if (categoryId==null||StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count==0){
            return ServerResponse.createByErrorMessage("修改失败");
        }
        return ServerResponse.createBySuccessMessage("修改成功");
    }

    public ServerResponse<List<Category>> getCategory(Integer categoryId){
        if (categoryId==null) return ServerResponse.createByErrorMessage("参数错误");
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        return ServerResponse.createBySuccess("查询成功",categoryList);
    }

    /**
     * 递归查询本节点的id及孩子节点的id
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(Integer categoryId){
        if (categoryId==null) return ServerResponse.createByErrorMessage("参数错误");
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        for (Category item : categorySet){
            categoryIdList.add(item.getId());
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    private Set<Category> findChildCategory(Set<Category> set,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category!=null){
            set.add(category);
        }
        List<Category> list = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category item:list){
            findChildCategory(set,item.getId());
        }
        return set;
    }
}
