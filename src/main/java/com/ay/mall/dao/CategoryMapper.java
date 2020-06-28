package com.ay.mall.dao;

import com.ay.mall.pojo.Category;
import sun.util.resources.cldr.te.TimeZoneNames_te;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> selectCategoryChildrenByParentId(Integer categoryId);
}