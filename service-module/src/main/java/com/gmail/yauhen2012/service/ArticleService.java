package com.gmail.yauhen2012.service;

import java.util.List;

import com.gmail.yauhen2012.service.model.AddArticleDTO;
import com.gmail.yauhen2012.service.model.ArticleDTO;

public interface ArticleService {

    List<ArticleDTO> getArticlesWithoutContentByPage(String page);

    void add(AddArticleDTO addArticleDTO);

    ArticleDTO findById(Long id);

    List<ArticleDTO> findAllSorted();

    Boolean deleteArticleById(Long id);

    Boolean update(ArticleDTO articleDTO);

}
