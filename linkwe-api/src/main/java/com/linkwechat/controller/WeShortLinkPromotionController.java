package com.linkwechat.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.linkwechat.common.annotation.Log;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.page.TableDataInfo;
import com.linkwechat.common.core.redis.RedisService;
import com.linkwechat.common.enums.BusinessType;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.domain.material.entity.WeMaterial;
import com.linkwechat.domain.material.query.WePosterQuery;
import com.linkwechat.domain.material.vo.WeMaterialVo;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
import com.linkwechat.domain.shortlink.vo.WeShortLinkPromotionGetVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkPromotionVo;
import com.linkwechat.service.IWeMaterialService;
import com.linkwechat.service.IWeShortLinkPromotionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * 短链推广
 *
 * @author WangYX
 * @version 1.0.0
 * @date 2023/03/07 15:27
 */
@Api(tags = "短链推广")
@RestController
@RequestMapping("/short/link/promotion")
public class WeShortLinkPromotionController extends BaseController {

    @Resource
    private IWeShortLinkPromotionService weShortLinkPromotionService;
    @Resource
    private IWeMaterialService weMaterialService;
    @Resource
    private LinkWeChatConfig linkWeChatConfig;
    @Resource
    private RedisService redisService;


    /**
     * 短链推广列表
     *
     * @param query 查询条件
     * @return
     */
    @ApiOperation(value = "短链推广列表", httpMethod = "GET")
    @Log(title = "短链推广列表", businessType = BusinessType.SELECT)
    @GetMapping("/list")
    public TableDataInfo<WeShortLinkPromotionVo> list(WeShortLinkPromotionQuery query) {
        startPage();
        List<WeShortLinkPromotionVo> list = weShortLinkPromotionService.selectList(query);
        Optional.ofNullable(list).ifPresent(items -> items.stream().forEach(i -> {
            //获取短链
            String encode = Base62NumUtil.encode(i.getShortLinkId());
            String shortLinkUrl = linkWeChatConfig.getShortLinkDomainName() + encode;
            i.setShortLinkUrl(shortLinkUrl);


            String encode1 = Base62NumUtil.encode(i.getId());
            //今日PV数
            Integer tpv = redisService.getCacheObject(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.PV + encode1);
            tpv = tpv == null ? 0 : tpv;
            //今日UV数
            Long tuv = redisService.hyperLogLogCount(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.UV + encode1);
            //今日打开小程序数
            Integer topen = redisService.getCacheObject(WeConstans.WE_SHORT_LINK_PROMOTION_KEY + WeConstans.OPEN_APPLET + encode1);
            topen = topen == null ? 0 : topen;


            i.setPvNum(i.getPvNum() != null ? i.getPvNum() + tpv : tpv);
            i.setUvNum(i.getUvNum() != null ? i.getUvNum() + tuv.intValue() : tuv.intValue());
            i.setOpenNum(i.getOpenNum() != null ? i.getOpenNum() + topen : topen);

        }));
        return getDataTable(list);
    }

    /**
     * 新增短链推广
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "新增短链推广", httpMethod = "POST")
    @Log(title = "新增短链推广", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody WeShortLinkPromotionAddQuery query) {
        try {
            Long id = weShortLinkPromotionService.add(query);
            return AjaxResult.success(id);
        } catch (IOException e) {
            logger.error("新建短链推广失败：{}", e.getMessage());
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    /**
     * 暂存短链推广
     *
     * @param query
     * @return
     */
    @Log(title = "暂存短链推广", businessType = BusinessType.INSERT)
    @ApiOperation(value = "暂存短链推广", httpMethod = "POST")
    @PostMapping("/ts")
    public AjaxResult temporaryStorage(@Validated @RequestBody WeShortLinkPromotionAddQuery query) {
        try {
            Long id = weShortLinkPromotionService.ts(query);
            return AjaxResult.success(id);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("暂存推广短链失败：{}", e.getMessage());
            return AjaxResult.error();
        }
    }

    /**
     * 修改短链推广
     *
     * @param query
     * @return
     */
    @Log(title = "修改短链推广", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "修改", httpMethod = "PUT")
    @PutMapping("/edit")
    public AjaxResult edit(@Validated @RequestBody WeShortLinkPromotionUpdateQuery query) {
        try {
            weShortLinkPromotionService.edit(query);
            return AjaxResult.success();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("修改短链推广失败：{}", e.getMessage());
            return AjaxResult.error();
        }
    }

    /**
     * 删除短链推广
     *
     * @param id
     * @return
     */
    @Log(title = "删除短链推广", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除", httpMethod = "DELETE")
    @DeleteMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        weShortLinkPromotionService.delete(id);
        return AjaxResult.success();
    }

    /**
     * 批量删除短链推广
     *
     * @param ids
     * @return
     */
    @Log(title = "删除短链推广", businessType = BusinessType.DELETE)
    @ApiOperation(value = "删除", httpMethod = "DELETE")
    @DeleteMapping("/batch/delete/{ids}")
    public AjaxResult batchDelete(@PathVariable("ids") Long[] ids) {
        weShortLinkPromotionService.batchDelete(ids);
        return AjaxResult.success();
    }

    /**
     * 获取包含占位码组件的海报
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "获取包含占位码组件的海报", httpMethod = "GET")
    @Log(title = "获取包含占位码组件的海报", businessType = BusinessType.SELECT)
    @GetMapping("/poster/list")
    public TableDataInfo<WeMaterialVo> posterList(WePosterQuery query) {
        startPage();
        LambdaQueryWrapper<WeMaterial> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(WeMaterial::getId, WeMaterial::getMaterialUrl, WeMaterial::getMaterialName);
        //只取带有占位码组件的海报
        queryWrapper.eq(WeMaterial::getPosterQrType, 3);
        queryWrapper.eq(WeMaterial::getDelFlag, 0);
        queryWrapper.like(StrUtil.isNotBlank(query.getTitle()), WeMaterial::getMaterialName, query.getTitle());
        List<WeMaterial> list = weMaterialService.list(queryWrapper);
        List<WeMaterialVo> weMaterialVos = BeanUtil.copyToList(list, WeMaterialVo.class);
        return getDataTable(weMaterialVos);
    }

    /**
     * 获取短链推广
     */
    @ApiOperation(value = "获取短链推广", httpMethod = "GET")
    @Log(title = "获取短链推广", businessType = BusinessType.SELECT)
    @GetMapping("/{id}")
    public AjaxResult get(@PathVariable("id") Long id) {
        WeShortLinkPromotionGetVo vo = weShortLinkPromotionService.get(id);
        return AjaxResult.success(vo);
    }


}

