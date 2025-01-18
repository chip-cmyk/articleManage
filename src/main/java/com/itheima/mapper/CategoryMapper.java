package com.itheima.mapper;

import com.itheima.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Insert("insert into category(category_name, category_alias, create_user, create_time, update_time) " +
            "values (#{categoryName}, #{categoryAlias}, #{createUser}, now(), now())")
    void add(Category category);

    @Select("select * from category where create_user=#{userId}")
    List<Category> list(Integer id);

    @Select("select * from category where id=#{id} and create_user=#{createUserId}")
    Category findById(Integer id, Integer createUserId);

    @Update("update category set category_name=#{category.categoryName}, category_alias=#{category.categoryAlias}, update_time=now() where id=#{category.id} and create_user=#{createUserId}")
    Integer update(@Param("category") Category category, @Param("createUserId") Integer createUserId);

    @Delete("delete from category where id=#{id}")
    void delete(Integer id);
}
