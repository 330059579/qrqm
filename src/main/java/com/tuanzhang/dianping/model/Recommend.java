package com.tuanzhang.dianping.model;

import java.io.Serializable;

public class Recommend implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String recommend;

    /**
     * recommend
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
     * 
     * @return recommend 
     */
    public String getRecommend() {
        return recommend;
    }

    /**
     * 
     * @param recommend 
     */
    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }
}