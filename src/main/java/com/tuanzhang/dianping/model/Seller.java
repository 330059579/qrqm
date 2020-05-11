package com.tuanzhang.dianping.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Seller implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 商户名称
     */
    private String name;

    /**
     * 商户评分，表示商户的服务能力，其值为所有门店评分的平均值
     */
    private BigDecimal remarkScore;

    /**
     * 0启用，1禁用
     */
    private Integer disabledFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    /**
     * seller
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     * @return id 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 主键
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 商户名称
     * @return name 商户名称
     */
    public String getName() {
        return name;
    }

    /**
     * 商户名称
     * @param name 商户名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 商户评分，表示商户的服务能力，其值为所有门店评分的平均值
     * @return remark_score 商户评分，表示商户的服务能力，其值为所有门店评分的平均值
     */
    public BigDecimal getRemarkScore() {
        return remarkScore;
    }

    /**
     * 商户评分，表示商户的服务能力，其值为所有门店评分的平均值
     * @param remarkScore 商户评分，表示商户的服务能力，其值为所有门店评分的平均值
     */
    public void setRemarkScore(BigDecimal remarkScore) {
        this.remarkScore = remarkScore;
    }

    /**
     * 0启用，1禁用
     * @return disabled_flag 0启用，1禁用
     */
    public Integer getDisabledFlag() {
        return disabledFlag;
    }

    /**
     * 0启用，1禁用
     * @param disabledFlag 0启用，1禁用
     */
    public void setDisabledFlag(Integer disabledFlag) {
        this.disabledFlag = disabledFlag;
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 
     * @return update_time 
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 
     * @param updateTime 
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}