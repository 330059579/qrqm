package com.tuanzhang.dianping.service;

import com.tuanzhang.dianping.common.BusinessException;
import com.tuanzhang.dianping.model.Seller;

import java.util.List;

public interface SellerService {

    Seller create(Seller sellerModel);
    Seller get(Integer id);
    List<Seller> selectAll();
    Seller changeStatus(Integer id, Integer disabledFlag) throws BusinessException;

    Integer countAllSeller();
}
