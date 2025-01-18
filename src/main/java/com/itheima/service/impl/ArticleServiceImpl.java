package com.itheima.service.impl;

import com.itheima.mapper.ArticleMapper;
import com.itheima.pojo.Article;
import com.itheima.service.ArticleService;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(Article article) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer createUser = ((Integer) map.get("id"));
        article.setCreateUser(createUser);
        articleMapper.add(article);
    }
}
