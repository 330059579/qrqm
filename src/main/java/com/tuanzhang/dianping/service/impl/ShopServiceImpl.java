package com.tuanzhang.dianping.service.impl;

import com.tuanzhang.dianping.common.BusinessException;
import com.tuanzhang.dianping.common.EmBusinessError;
import com.tuanzhang.dianping.dal.ShopDAO;
import com.tuanzhang.dianping.model.Category;
import com.tuanzhang.dianping.model.Seller;
import com.tuanzhang.dianping.model.Shop;
import com.tuanzhang.dianping.model.ShopExample;
import com.tuanzhang.dianping.service.CategoryService;
import com.tuanzhang.dianping.service.SellerService;
import com.tuanzhang.dianping.service.ShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("shopService")
public class ShopServiceImpl implements ShopService {

    @Resource
    private ShopDAO  shopDAO;
    @Resource
    private CategoryService categoryService;
    @Resource
    private SellerService sellerService;

    @Override
    public Shop create(Shop shop) throws BusinessException {
        shop.setUpdateTime(new Date());
        shop.setUpdateTime(new Date());

        Seller seller = sellerService.get(shop.getSellerId());
        if (null == seller) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商户不存在！");
        }

        if (seller.getDisabledFlag() == 1 ){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "商户已下架！");
        }

        Category category = categoryService.get(shop.getCategoryId());
        if (null == category) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "类目不存在！");
        }

        shopDAO.insert(shop);
        return shop;
    }

    @Override
    public Shop get(Integer id) {
        Shop shop = shopDAO.selectByPrimaryKey(id);
        if (null == shop) {
            return null;
        }

        shop.setSeller(sellerService.get(shop.getSellerId()));
        shop.setCategory(categoryService.get(shop.getSellerId()));
        return shop;
    }

    @Override
    public List<Shop> selectAll() {
        ShopExample example = new ShopExample();
        List<Shop> shops = shopDAO.selectByExample(example);
        shops.forEach(u -> {
            u.setSeller(sellerService.get(u.getSellerId()));
            u.setCategory(categoryService.get(u.getCategoryId()));
        });
        return shops;
    }
}
