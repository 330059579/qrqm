package com.tuanzhang.dianping.service.impl;

import com.tuanzhang.dianping.common.BusinessException;
import com.tuanzhang.dianping.dal.SellerDAO;
import com.tuanzhang.dianping.model.Seller;
import com.tuanzhang.dianping.model.SellerExample;
import com.tuanzhang.dianping.service.SellerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Resource
    private SellerDAO sellerDAO;


    @Override
    public Seller create(Seller sellerModel) {
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
}
