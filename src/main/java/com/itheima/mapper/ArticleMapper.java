package com.itheima.mapper;

import com.itheima.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Insert("insert into article(title, content, cover_img, state, category_id, create_user, create_time, update_time) " +
            "values (#{title}, #{content}, #{coverImg}, #{state}, #{categoryId}, #{createUser},now(), now())")
    void add(Article article);

    @Delete("delete from article where category_id=#{categoryId}")
    void deleteByCategoryId(Integer categoryId);

    List<Article> list(Integer userId, Integer categoryId, String state);

    @Select("select * from article where id=#{id}")
    Article findById(Integer id);

    @Update("update article set title=#{title},content=#{content},cover_img=#{coverImg}, state=#{state}," +
            "category_id=#{categoryId} where id=#{id}")
    void update(Article article);

    @Delete("delete from article where id=#{id}")
    void delete(Integer id);
}
