package com.linkwechat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linkwechat.common.config.LinkWeChatConfig;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.common.utils.Base62NumUtil;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.domain.WeCustomerLink;
import com.linkwechat.domain.WeTag;
import com.linkwechat.domain.wecom.query.customer.link.WeLinkCustomerQuery;
import com.linkwechat.domain.wecom.vo.customer.link.WeLinkCustomerVo;
import com.linkwechat.fegin.QwCustomerClient;
import com.linkwechat.fegin.QwSysUserClient;
import com.linkwechat.fegin.QwUserClient;
import com.linkwechat.service.IWeCustomerLinkAttachmentsService;
import com.linkwechat.service.IWeCustomerLinkService;
import com.linkwechat.mapper.WeCustomerLinkMapper;
import com.linkwechat.service.IWeTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author robin
* @description 针对表【we_customer_link(获客助手)】的数据库操作Service实现
* @createDate 2023-07-04 17:41:13
*/
@Service
public class WeCustomerLinkServiceImpl extends ServiceImpl<WeCustomerLinkMapper, WeCustomerLink>
    implements IWeCustomerLinkService {

    @Autowired
    private IWeCustomerLinkAttachmentsService iWeCustomerLinkAttachmentsService;

    @Autowired
    private IWeTagService iWeTagService;

    @Autowired
    private QwCustomerClient qwCustomerClient;


    @Autowired
    private LinkWeChatConfig linkWeChatConfig;

    @Autowired
    private QwSysUserClient qwSysUserClient;

    @Override
    @Transactional
    public void createOrUpdateCustomerLink(WeCustomerLink customerLink,boolean createOrUpdate) {

        if(StringUtils.isNotEmpty(customerLink.getWeUserList())){

            WeLinkCustomerQuery customerQuery = WeLinkCustomerQuery.builder()
                    .link_id(customerLink.getLinkId())
                    .link_name(customerLink.getLinkName())
                    .range(
                            WeLinkCustomerQuery.Range.builder()
                                    .user_list(customerLink.getWeUserList().split(","))
                                    .build()
                    )
                    .skip_verify(customerLink.getSkipVerify().equals(new Integer(1)) ? true : false)
                    .build();




                WeLinkCustomerVo weLinkCustomerVo =  createOrUpdate ? qwCustomerClient.createCustomerLink(
                        customerQuery
                ).getData():qwCustomerClient.updateCustomerLink(customerQuery).getData();




                if(!weLinkCustomerVo.getErrCode().equals(WeConstans.WE_SUCCESS_CODE)){
                    throw new WeComException(weLinkCustomerVo.getErrMsg());
                }


            if(null != weLinkCustomerVo){
                WeLinkCustomerVo.Link link = weLinkCustomerVo.getLink();
                if(null != link){
                    customerLink.setLinkId(link.getLink_id());
                    customerLink.setLinkUrl(link.getUrl());
                    customerLink.setLinkShortUrl(
                            linkWeChatConfig.getShortLinkDomainName() + Base62NumUtil.encode(customerLink.getId())
                    );
                }
            }

            if(saveOrUpdate(customerLink)){
                if(createOrUpdate){
                    iWeCustomerLinkAttachmentsService.saveBatchByCustomerLinkId(customerLink.getId(),customerLink.getAttachments());

                }else{
                    iWeCustomerLinkAttachmentsService.updateBatchByCustomerLinkId(customerLink.getId(),customerLink.getAttachments());
                }
            }

        }else{
            throw new WeComException("链接员工不可为空");

        }


    }

    @Override
    public WeCustomerLink findWeCustomerLinkById(Long id) {

        WeCustomerLink weCustomerLink
                = this.getById(id);

        //获取附件等信息
        if(null != weCustomerLink){

            if(StringUtils.isNotEmpty(weCustomerLink.getTagIds())){
                List<WeTag> weTags = iWeTagService.list(new LambdaQueryWrapper<WeTag>()
                        .in(WeTag::getTagId, weCustomerLink.getTagIds().split(",")));
                if(CollectionUtil.isNotEmpty(weTags)){
                    weCustomerLink.setTagNames(
                            weTags.stream().map(WeTag::getName).collect(Collectors.joining(","))
                    );
                }
            }


            if(StringUtils.isNotEmpty(weCustomerLink.getWeUserList())){
                List<SysUser> allSysUsers =
                        qwSysUserClient.findAllSysUser(weCustomerLink.getWeUserList(), null, null).getData();

                if(CollectionUtil.isNotEmpty(allSysUsers)){
                    weCustomerLink.setWeUserNames(
                            allSysUsers.stream().map(SysUser::getUserName).collect(Collectors.joining(","))
                    );
                }
            }


        }





        return weCustomerLink;
    }

}




