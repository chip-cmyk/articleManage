package com.itheima.mapper;

import com.itheima.pojo.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Insert("insert into article(title, content, cover_img, state, category_id, create_user, create_time, update_time) " +
            "values (#{title}, #{content}, #{coverImg}, #{state}, #{categoryId}, #{createUser},now(), now())")
    void add(Article article);

    @Delete("delete from article where category_id=#{categoryId}")
    void deleteByCategoryId(Integer categoryId);

    List<Article> list(Integer userId, Integer categoryId, String state);
}
