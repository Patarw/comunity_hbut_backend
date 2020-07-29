package com.hbut.community.dao;

import com.hbut.community.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article,Integer> {
    Article findArticleById(Integer id);
    Page<Article> findArticleByCheckStatusOrderByCreateTimeDesc(Integer checkStatus, Pageable pageable);

    @Query(value = "SELECT * FROM article ORDER BY create_time DESC ",nativeQuery = true)
    Page<Article> findAll(Pageable pageable);

    Page<Article> findArticleByUserIdAndCheckStatusOrderByUpdateTimeDesc(Integer userId, Integer checkStatus, Pageable pageable);
}
