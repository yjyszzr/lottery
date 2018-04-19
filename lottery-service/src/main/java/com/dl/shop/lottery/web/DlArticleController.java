package com.dl.shop.lottery.web;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.lottery.dto.DLArticleDTO;
import com.dl.lottery.dto.DLArticleDetailDTO;
import com.dl.lottery.param.ListArticleParam;
import com.dl.shop.lottery.service.DlArticleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
* Created by CodeGenerator on 2018/04/19.
*/
@RestController
@RequestMapping("/dl/article")
public class DlArticleController {
    @Resource
    private DlArticleService dlArticleService;

   /* @PostMapping("/add")
    public BaseResult add(DlArticle dlArticle) {
        dlArticleService.save(dlArticle);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public BaseResult delete(@RequestParam Integer id) {
        dlArticleService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public BaseResult update(DlArticle dlArticle) {
        dlArticleService.update(dlArticle);
        return ResultGenerator.genSuccessResult();
    }*/

    @PostMapping("/detail")
    public BaseResult<DLArticleDetailDTO> detail(@RequestParam Integer id) {
    	DLArticleDetailDTO dlArticle = dlArticleService.findArticleById(id);
        return ResultGenerator.genSuccessResult(null,dlArticle);
    }

    @PostMapping("/list")
    public BaseResult<PageInfo<DLArticleDTO>> list(@RequestBody ListArticleParam param) {
    	Integer page = param.getPage();
    	page = null == page?1:page;
    	Integer size = param.getSize();
    	size = null == size?20:size;
        PageHelper.startPage(page, size);
        List<DLArticleDTO> list = dlArticleService.findArticles();
        PageInfo<DLArticleDTO> pageInfo = new PageInfo<DLArticleDTO>(list);
        return ResultGenerator.genSuccessResult(null,pageInfo);
    }
}
