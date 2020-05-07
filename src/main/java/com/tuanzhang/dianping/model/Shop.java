package com.tuanzhang.dianping.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Shop implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 门店名称
     */
    private String name;

    /**
     * 
     */
    private BigDecimal remarkScore;

    /**
     * 平均消费
     */
    private Integer pricePerMan;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 类目
     */
    private Integer categoryId;

    /**
     * 标签
     */
    private String tags;

    /**
     * 营业开始时间
     */
    private String startTime;

    /**
     * 营业结束时间
     */
    private String endTime;

    /**
     * 
     */
    private String address;

    /**
     * 
     */
    private Integer sellerId;

    /**
     * 
     */
    private String iconUrl;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    private Category category;

    private Seller seller;
    /**
     * shop
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 门店名称
     * @return name 门店名称
     */
    public String getName() {
        return name;
    }

    /**
     * 门店名称
     * @param name 门店名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return remark_score 
     */
    public BigDecimal getRemarkScore() {
        return remarkScore;
    }

    /**
     * 
     * @param remarkScore 
     */
    public void setRemarkScore(BigDecimal remarkScore) {
        this.remarkScore = remarkScore;
    }

    /**
     * 平均消费
     * @return price_per_man 平均消费
     */
    public Integer getPricePerMan() {
        return pricePerMan;
    }

    /**
     * 平均消费
     * @param pricePerMan 平均消费
     */
    public void setPricePerMan(Integer pricePerMan) {
        this.pricePerMan = pricePerMan;
    }

    /**
     * 纬度
     * @return latitude 纬度
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * 纬度
     * @param latitude 纬度
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * 经度
     * @return longitude 经度
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * 经度
     * @param longitude 经度
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * 类目
     * @return category_id 类目
     */
    public Integer getCategoryId() {
        return categoryId;
    }

    /**
     * 类目
     * @param categoryId 类目
     */
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 标签
     * @return tags 标签
     */
    public String getTags() {
        return tags;
    }

    /**
     * 标签
     * @param tags 标签
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * 营业开始时间
     * @return start_time 营业开始时间
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 营业开始时间
     * @param startTime 营业开始时间
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * 营业结束时间
     * @return end_time 营业结束时间
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 营业结束时间
     * @param endTime 营业结束时间
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 
     * @return address 
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address 
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 
     * @return seller_id 
     */
    public Integer getSellerId() {
        return sellerId;
    }

    /**
     * 
     * @param sellerId 
     */
    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * 
     * @return icon_url 
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * 
     * @param iconUrl 
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    /**
     * 
     * @return create_time 
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 
     * @param createTime 
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}