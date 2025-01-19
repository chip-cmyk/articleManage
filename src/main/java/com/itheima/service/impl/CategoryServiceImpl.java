package com.itheima.service.impl;

import com.itheima.mapper.ArticleMapper;
import com.itheima.mapper.CategoryMapper;
import com.itheima.pojo.Category;
import com.itheima.service.CategoryService;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(Category category) {
        Map<String, Object> map = ThreadLocalUtil.get();
        category.setCreateUser((Integer) map.get("id"));
        categoryMapper.add(category);
    }

    @Override
    public List<Category> list() {
        Map<String, Object> map = ThreadLocalUtil.get();
        return categoryMapper.list((Integer) map.get("id"));
    }

    @Override
    public Category findById(Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer createUserId = ((Integer) map.get("id"));
        Category category = categoryMapper.findById(id, createUserId);
        return category;
    }

    @Override
    public Integer update(Category category) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer createUserId = ((Integer) map.get("id"));
        try {
            return categoryMapper.update(category, createUserId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        articleMapper.deleteByCategoryId(id);
        categoryMapper.delete(id);
    }
}
