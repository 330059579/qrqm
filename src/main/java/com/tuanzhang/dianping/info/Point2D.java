package com.tuanzhang.dianping.info;

import java.math.BigDecimal;

public class Point2D {

    BigDecimal x;

    BigDecimal y;

    public Point2D() {
    }

    public Point2D(BigDecimal x, BigDecimal y) {
        this.x = x;
        this.y = y;
    }

    public BigDecimal getX() {
        return x;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }
}
