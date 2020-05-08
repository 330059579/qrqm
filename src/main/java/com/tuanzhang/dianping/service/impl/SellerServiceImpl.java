package com.tuanzhang.dianping.service.impl;

import com.tuanzhang.dianping.common.BusinessException;
import com.tuanzhang.dianping.dal.SellerDAO;
import com.tuanzhang.dianping.model.Seller;
import com.tuanzhang.dianping.model.SellerExample;
import com.tuanzhang.dianping.model.UserExample;
import com.tuanzhang.dianping.service.SellerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Resource
    private SellerDAO sellerDAO;


    @Override
    @Transactional
    public Seller create(Seller sellerModel) {
        sellerModel.setRemarkScore(new BigDecimal(0));
        sellerModel.setDisabledFlag(0);
        sellerDAO.insertSelective(sellerModel);
        return sellerModel;
    }

    @Override
    public Seller get(Integer id) {
        return sellerDAO.selectByPrimaryKey(id);
    }

    @Override
    public List<Seller> selectAll() {
        SellerExample sellerExample = new SellerExample();
        return sellerDAO.selectByExample(sellerExample);
    }

    @Override
    public Seller changeStatus(Integer id, Integer disabledFlag) throws BusinessException {
        Seller seller = new Seller();
        seller.setId(id);
        seller.setDisabledFlag(disabledFlag);
        sellerDAO.updateByPrimaryKeySelective(seller);
        return seller;
    }

    @Override
    public Integer countAllSeller() {
        SellerExample example = new SellerExample();
        return sellerDAO.countByExample(example);
    }
}
