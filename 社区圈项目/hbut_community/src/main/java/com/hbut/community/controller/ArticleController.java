package com.hbut.community.controller;

import VO.*;
import VO.Void;
import args.PageArg;
import com.hbut.community.entity.Article;
import com.hbut.community.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "社区交流管理接口")
@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**发布帖子**/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("发布帖子，一定要传user_id")
    @RequestMapping(value = "/publishArticle",method = RequestMethod.POST)
    public Result<Void> publishArticle(@RequestBody Article article){
        return articleService.publishArticle(article);
    }

    /**分页获取帖子**/
    @ApiOperation("分页获取全部帖子信息")
    @RequestMapping(value = "/getArticleByPage",method = RequestMethod.POST)
    public Result<PageVO<ArticleVO>> getArticleByPage(@RequestBody PageArg arg){
        return articleService.listArticleByPage(arg);
    }

    /**通过id获取帖子**/
    @ApiOperation("通过id获取帖子信息")
    @RequestMapping(value = "/getArticleById",method = RequestMethod.POST)
    public Result<ArticleVO> getArticleById(Integer id){
        return articleService.getArticleById(id);
    }

    /**分页获取通过审核帖子**/
    @ApiOperation("分页获取通过审核帖子")
    @RequestMapping(value = "/listCheckedArticle",method = RequestMethod.POST)
    public Result<PageVO<ArticleVO>> listCheckedArticle(@RequestBody PageArg arg){
        return articleService.listCheckedArticle(arg);
    }

    /**通过id删除帖子**/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("通过id删除帖子")
    @RequestMapping(value = "/deleteById",method = RequestMethod.POST)
    public Result<Void> deleteById(Integer id){
        return articleService.deleteById(id);
    }

    /**获取到用户发布的所有帖子**/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("获取到该用户发布的所有帖子")
    @RequestMapping(value = "/findPublishedArticle",method = RequestMethod.POST)
    public Result<PageVO<ArticleVO>> findPublishedArticle(Integer userId,@RequestBody PageArg arg){
        return articleService.findPublishedArticle(userId,arg);
    }
}
