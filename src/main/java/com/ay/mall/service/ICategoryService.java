package com.ay.mall.service;

import com.ay.mall.common.ServerResponse;
import com.ay.mall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    public ServerResponse<String> addCategory(Integer parentId, String categoryName);

    public ServerResponse<String> updateName(Integer categoryId,String categoryName);

    public ServerResponse<List<Category>> getCategory(Integer categoryId);

    public ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(Integer categoryId);
}
