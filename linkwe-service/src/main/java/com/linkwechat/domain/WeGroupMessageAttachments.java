package com.linkwechat.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.linkwechat.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;


import lombok.Data;

/**
 * 群发消息附件表(WeGroupMessageAttachments)
 *
 * @author danmo
 * @since 2022-04-06 22:29:03
 */
@ApiModel
@Data
@SuppressWarnings("serial")
@TableName("we_group_message_attachments")
public class WeGroupMessageAttachments extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; //1

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    @TableId
    private Long id;


    /**
     * 消息模板id
     */
    @ApiModelProperty(value = "消息模板id")
    @TableField("msg_template_id")
    private Long msgTemplateId;


    /**
     * 企业群发消息的id
     */
    @ApiModelProperty(value = "企业群发消息的id")
    @TableField("msg_id")
    private String msgId;


    /**
     * 消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file
     */
    @ApiModelProperty(value = "消息类型 文本:text 图片:image 图文:link 小程序:miniprogram 视频:video 文件:file ")
    @TableField("msg_type")
    private String msgType;


    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    @TableField("content")
    private String content;


    /**
     * 媒体id
     */
    @ApiModelProperty(value = "媒体id")
    @TableField("media_id")
    private String mediaId;


    /**
     * 消息标题
     */
    @ApiModelProperty(value = "消息标题")
    @TableField("title")
    private String title;


    /**
     * 消息描述
     */
    @ApiModelProperty(value = "消息描述")
    @TableField("description")
    private String description;


    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径")
    @TableField("file_url")
    private String fileUrl;


    /**
     * 消息链接
     */
    @ApiModelProperty(value = "消息链接")
    @TableField("link_url")
    private String linkUrl;


    /**
     * 消息图片地址
     */
    @ApiModelProperty(value = "消息图片地址")
    @TableField("pic_url")
    private String picUrl;


    /**
     * 小程序appid
     */
    @ApiModelProperty(value = "小程序appid")
    @TableField("app_id")
    private String appId;


    
    
    


    /**
     * 删除标识 0 有效 1删除
     */
    @ApiModelProperty(value = "删除标识 0 有效 1删除")
    @TableField("del_flag")
    private Integer delFlag;


    /**
     * 真实数据类型，（其他类型的数据，转成链接之后的真是的数据类型）
     */
    @ApiModelProperty(value = "真实数据类型")
    @TableField("real_type")
    private Integer realType;

    /**
     * 素材中心Id
     */
    @ApiModelProperty(value = "素材中心Id")
    @TableField("material_id")
    private Long materialId;
}
