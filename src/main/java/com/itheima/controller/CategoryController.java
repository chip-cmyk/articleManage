package com.itheima.controller;

import com.itheima.pojo.Category;
import com.itheima.pojo.Result;
import com.itheima.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result add(@RequestBody @Validated Category category) {
        categoryService.add(category);
        return Result.success();
    }

    @GetMapping
    public Result<List<Category>> list() {
        List<Category> list = categoryService.list();
        return Result.success(list);
    }

    @GetMapping("/detail")
    public Result<Category> detail(Integer id) {
        Category category = categoryService.findById(id);
        return Result.success(category);
    }

    @PutMapping
    public Result update(@RequestBody @Validated Category category) {
        if (category.getId() == null) {
            return Result.error("id不能为空");
        }
        if (categoryService.update(category) == 1) {
            return Result.success();
        }
        return Result.error("更新失败或id不存在/用户无权限");
    }

    @DeleteMapping
    public Result delete(@RequestParam Integer id) {
        Category c = categoryService.findById(id);
        if (c == null) {
            return Result.error("不存在此分类或该用户无权限");
        }
        categoryService.delete(id);
        return Result.success();
    }
}
