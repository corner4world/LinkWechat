package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeShortLinkPromotion;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionAddQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionQuery;
import com.linkwechat.domain.shortlink.query.WeShortLinkPromotionUpdateQuery;
import com.linkwechat.domain.shortlink.vo.WeShortLinkPromotionGetVo;
import com.linkwechat.domain.shortlink.vo.WeShortLinkPromotionVo;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 短链推广 服务类
 * </p>
 *
 * @author WangYX
 * @since 2023-03-07
 */
public interface IWeShortLinkPromotionService extends IService<WeShortLinkPromotion> {

    /**
     * 短链推广列表
     *
     * @param query 查询条件
     * @return {@link List< WeShortLinkPromotionVo>}
     * @author WangYX
     * @date 2023/03/07 15:41
     */
    List<WeShortLinkPromotionVo> selectList(WeShortLinkPromotionQuery query);


    /**
     * 新增短链推广
     *
     * @param query
     * @return {@link Long}
     * @throws IOException
     * @author WangYX
     * @date 2023/03/07 16:44
     */
    Long add(WeShortLinkPromotionAddQuery query) throws IOException;


    /**
     * 暂存推广短链
     *
     * @param query
     * @return {@link Long}
     * @throws IOException
     * @author WangYX
     * @date 2023/03/09 14:00
     */
    Long ts(WeShortLinkPromotionAddQuery query) throws IOException;

    /**
     * 修改短链推广
     *
     * @param query
     * @return
     * @author WangYX
     * @date 2023/03/09 14:12
     */
    void edit(WeShortLinkPromotionUpdateQuery query) throws IOException;

    /**
     * 详情
     *
     * @param id 短链推广ID
     * @return {@link WeShortLinkPromotionGetVo}
     * @author WangYX
     * @date 2023/03/16 10:19
     */
    WeShortLinkPromotionGetVo get(Long id);


    /**
     * 删除
     *
     * @param id 短链推广Id
     * @return
     * @author WangYX
     * @date 2023/03/21 18:02
     */
    void delete(Long id);

    /**
     * 批量删除
     *
     * @param ids 短链推广Id集合
     * @return
     * @author WangYX
     * @date 2023/03/21 18:02
     */
    void batchDelete(Long[] ids);


}
