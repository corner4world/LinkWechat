package com.linkwechat.controller;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomerLink;
import com.linkwechat.service.IWeCustomerLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 获客助手
 */
@Slf4j
@RestController
@RequestMapping("/customerlink")
public class WeCustomerLinkController  extends BaseController {


    @Autowired
    private IWeCustomerLinkService iWeCustomerLinkService;


    /**
     * 获取获客助手列表
     * @param weCustomerLink
     * @return
     */
    @GetMapping("/list")
    public TableDataInfo<WeCustomerLink> list(WeCustomerLink weCustomerLink){
         startPage();
        return getDataTable(
                iWeCustomerLinkService.list(new LambdaQueryWrapper<WeCustomerLink>()
                        .like(StringUtils.isNotEmpty(weCustomerLink.getLinkName()),WeCustomerLink::getLinkName,weCustomerLink.getLinkName()))
        );

    }


    /**
     * 获取详情
     * @param id
     * @return
     */
    @GetMapping("/getCustomerLinkById/{id}")
    public AjaxResult<WeCustomerLink> getCustomerLinkById(@PathVariable Long id){


        return AjaxResult.success(
                iWeCustomerLinkService.findWeCustomerLinkById(id)
        );
    }


    /**
     * 创建获客助手
     * @param customerLink
     * @return
     */
    @PostMapping("/createCustomerLink")
    public AjaxResult createCustomerLink(@RequestBody WeCustomerLink customerLink){


        iWeCustomerLinkService.createOrUpdateCustomerLink(customerLink,true);


        return  AjaxResult.success();
    }





    /**
     * 更新获客助手
     * @param customerLink
     * @return
     */
    @PostMapping("/updateCustomerLink")
    public AjaxResult updateCustomerLink(@RequestBody WeCustomerLink customerLink){


        iWeCustomerLinkService.createOrUpdateCustomerLink(customerLink,false);

        return  AjaxResult.success();
    }



    /**
     * 删除获客助手链接
     */
    @DeleteMapping("/{ids}")
    public AjaxResult batchRemove(@PathVariable Long[] ids) {
        iWeCustomerLinkService.removeByIds(
                ListUtil.toList(ids)
        );
        return AjaxResult.success();
    }



}
