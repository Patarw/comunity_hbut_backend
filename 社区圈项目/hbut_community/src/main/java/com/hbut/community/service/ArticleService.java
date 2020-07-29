package com.hbut.community.service;

import VO.*;
import VO.Void;
import args.PageArg;
import com.hbut.community.client.UserClient;
import com.hbut.community.dao.ArticleRepository;
import com.hbut.community.entity.Article;
import enums.ResultEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.JwtUtil;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserClient userClient;

    /**发布帖子**/
    public Result<Void> publishArticle(Article article){
        //验证token
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if(article == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        article.setCheckStatus(1);  //先直接过审，后面开发了admin之后再搞审核系统
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        article.setViews(0);
        article.setLikes(0);
        articleRepository.save(article);
        return Result.newSuccess();
    }

    /**分页获取全部帖子**/
    public Result<PageVO<ArticleVO>> listArticleByPage(PageArg arg){
        arg.validate();
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<Article> articlePage = articleRepository.findAll(pageable);
        //获取分页数据
        List<Article> articleList = articlePage.getContent();
        List<ArticleVO> articleVOList = new ArrayList<>();
        for (Article article : articleList){
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article,articleVO);
            UserVO userVO = userClient.getUserById(article.getUserId()).getData();
            if (userVO == null){
                return Result.newResult(ResultEnum.REMOTE_ERROR);
            }
            articleVO.setUserVO(userVO);
            articleVOList.add(articleVO);
        }
        int total = articlePage.getTotalPages();

        //构建pageVo对象
        PageVO<ArticleVO> pageVo = PageVO.<ArticleVO>builder()
                .totalPage(total)
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(articleVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**通过id获取帖子**/
    public Result<ArticleVO> getArticleById(Integer id){
        Article article = articleRepository.findArticleById(id);
        if (article == null){
            return Result.newResult(ResultEnum.NO_ARTICLE_MSG);
        }
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article,articleVO);
        UserVO userVO = userClient.getUserById(article.getUserId()).getData();
        if (userVO == null){
            return Result.newResult(ResultEnum.REMOTE_ERROR);
        }
        articleVO.setUserVO(userVO);
        return Result.newSuccess(articleVO);
    }

    /**分页获取通过审核帖子**/
    public Result<PageVO<ArticleVO>> listCheckedArticle(PageArg arg){
        arg.validate();
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<Article> articlePage = articleRepository.findArticleByCheckStatusOrderByCreateTimeDesc(1,pageable);
        //获取分页数据
        List<Article> articleList = articlePage.getContent();
        List<ArticleVO> articleVOList = new ArrayList<>();
        for (Article article : articleList){
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article,articleVO);
            UserVO userVO = userClient.getUserById(article.getUserId()).getData();
            if (userVO == null){
                return Result.newResult(ResultEnum.REMOTE_ERROR);
            }
            articleVO.setUserVO(userVO);
            articleVOList.add(articleVO);
        }
        int total = articlePage.getTotalPages();

        //构建pageVo对象
        PageVO<ArticleVO> pageVo = PageVO.<ArticleVO>builder()
                .totalPage(total)
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(articleVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**通过id删除帖子**/
    public Result<Void> deleteById(Integer id){
        //验证token
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if(id == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Article article = articleRepository.findArticleById(id);
        if (article == null){
            return Result.newResult(ResultEnum.NO_ARTICLE_MSG);
        }
        articleRepository.deleteById(id);
        return Result.newSuccess();
    }

    /**获取到用户发布的所有帖子**/
    public Result<PageVO<ArticleVO>> findPublishedArticle(Integer userId,PageArg arg){
        //验证token
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        arg.validate();
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        if (userId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Page<Article> articlePage = articleRepository.findArticleByUserIdAndCheckStatusOrderByUpdateTimeDesc(userId,1,pageable);

        //获取分页数据
        List<Article> articleList = articlePage.getContent();
        List<ArticleVO> articleVOList = new ArrayList<>();
        for (Article article : articleList){
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(article,articleVO);
            UserVO userVO = userClient.getUserById(article.getUserId()).getData();
            if (userVO == null){
                return Result.newResult(ResultEnum.REMOTE_ERROR);
            }
            articleVO.setUserVO(userVO);
            articleVOList.add(articleVO);
        }
        int total = articlePage.getTotalPages();

        //构建pageVo对象
        PageVO<ArticleVO> pageVo = PageVO.<ArticleVO>builder()
                .totalPage(total)
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(articleVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

}
