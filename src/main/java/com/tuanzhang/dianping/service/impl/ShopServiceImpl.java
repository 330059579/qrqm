package com.tuanzhang.dianping.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tuanzhang.dianping.common.BusinessException;
import com.tuanzhang.dianping.common.CommonUtil;
import com.tuanzhang.dianping.common.EmBusinessError;
import com.tuanzhang.dianping.dal.ShopDAO;
import com.tuanzhang.dianping.info.Point2D;
import com.tuanzhang.dianping.model.*;
import com.tuanzhang.dianping.recommend.RecommendService;
import com.tuanzhang.dianping.recommend.RecommendSortService;
import com.tuanzhang.dianping.service.CategoryService;
import com.tuanzhang.dianping.service.SellerService;
import com.tuanzhang.dianping.service.ShopService;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service("shopService")
public class ShopServiceImpl implements ShopService {

    @Resource
    private ShopDAO  shopDAO;
    @Resource
    private CategoryService categoryService;
    @Resource
    private SellerService sellerService;
    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private RecommendService recommendService;
    @Resource
    private RecommendSortService recommendSortService;

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

    @Override
    public Integer countAllShop() {
        ShopExample example = new ShopExample();
        return shopDAO.countByExample(example);
    }

    @Override
    public List<Shop> recommend(BigDecimal longitude, BigDecimal latitude) {
        List<Integer> shopIdList = recommendService.recall(148);
        System.out.println(shopIdList);
        shopIdList = recommendSortService.sort(shopIdList, 148);
        System.out.println(shopIdList);
        List<Shop> shops  = shopIdList.stream().map(id ->{
            return get(id);
        }).collect(Collectors.toList());
     /*   ShopExample example = new ShopExample();
        List<Shop> shops = shopDAO.selectByExample(example);*/
        shops.forEach(u -> {
            Point2D pointA = new Point2D(longitude, latitude);
            Point2D pointb = new Point2D(u.getLongitude(), u.getLatitude());
            double distance = CommonUtil.getDistance(pointA, pointb);
            u.setDistance((int)distance);
            u.setSort(0.95 * 1 / Math.log10(u.getDistance()) + 0.05 * u.getRemarkScore().doubleValue() / 5);
            u.setSeller(sellerService.get(u.getSellerId()));
            u.setCategory(categoryService.get(u.getCategoryId()));
        });

        /* return shops.stream().sorted(Comparator.comparingDouble(Shop::getSort).reversed()).collect(Collectors.toList());*/
       return shops;
    }

    @Override
    public List<Shop> search(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderBy, Integer categoryId, String tags) {
        ShopExample shopExample = new ShopExample();
        ShopExample.Criteria criteria = shopExample.createCriteria();
        criteria.andNameLike("%" + keyword+ "%");
        if (null != categoryId) {
            criteria.andCategoryIdEqualTo(categoryId);
        }

        if (!StringUtils.isEmpty(tags)) {
            criteria.andTagsEqualTo(tags);
        }
        List<Shop> shops = shopDAO.selectByExample(shopExample);
        shops.forEach(u -> {
            Point2D pointA = new Point2D(longitude, latitude);
            Point2D pointb = new Point2D(u.getLongitude(), u.getLatitude());
            double distance = CommonUtil.getDistance(pointA, pointb);
            u.setDistance((int)distance);
            u.setSort(0.95 * 1 / Math.log10(u.getDistance()) + 0.05 * u.getRemarkScore().doubleValue() / 5);
            u.setSeller(sellerService.get(u.getSellerId()));
            u.setCategory(categoryService.get(u.getCategoryId()));
        });

        if (null != orderBy && 1 == orderBy) {
            return shops.stream().sorted(Comparator.comparingDouble(Shop::getPricePerMan)).collect(Collectors.toList());
        }

        return shops.stream().sorted(Comparator.comparingDouble(Shop::getSort).reversed()).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> searchGroupByTags(String keyword, Integer categoryId, String tags) {
        ShopExample shopExample = new ShopExample();
        ShopExample.Criteria criteria = shopExample.createCriteria();
        criteria.andNameLike("%" + keyword+ "%");
        if (null != categoryId) {
            criteria.andCategoryIdEqualTo(categoryId);
        }
        if (null != tags) {
            criteria.andTagsEqualTo(tags);
        }

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        Map<String, Object> tempMap = new HashMap<>();
        List<Shop> shops = shopDAO.selectByExample(shopExample);
        shops.forEach(u -> {
            Integer num = (Integer)tempMap.get(u.getTags());
            if (num == null) {
                tempMap.put(u.getTags(), 1);
                return;
            }

            tempMap.put(u.getTags(), num + 1);
        });

        tempMap.forEach((k, v) ->{
            Map<String, Object> map = new HashMap<>();
            map.put("tags", k);
            map.put("num", v);
            result.add(map);
        });
        return result;
    }

    @Override
    public Map<String, Object> searchES(BigDecimal longitude, BigDecimal latitude, String keyword, Integer orderBy, Integer categoryId, String tags) throws IOException {
        Map<String, Object> result = new HashMap<>();
      /*  SearchRequest searchRequest = new SearchRequest("shop");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("name", keyword));
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);

        List<Integer> shopIdsList = new ArrayList<>();
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        for(SearchHit hit : hits) {
            shopIdsList.add(new Integer(hit.getSourceAsMap().get("id").toString()));
        }*/

        Request request = new Request("GET", "/shop/_search");
        JSONObject jsonRequestObj = new JSONObject();
        //构建score部分
        jsonRequestObj.put("_source", "*");

        //构建自定义距离字段
        jsonRequestObj.put("script_fields",new JSONObject());
        jsonRequestObj.getJSONObject("script_fields").put("distance", new JSONObject());
        jsonRequestObj.getJSONObject("script_fields").getJSONObject("distance").put("script", new JSONObject());
        jsonRequestObj.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script")
        .put("source", "haversin(lat, lon, doc['location'].lat, doc['location'].lon)");
        jsonRequestObj.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script")
                .put("lang","expression");
        jsonRequestObj.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script")
                .put("params",new JSONObject());
        jsonRequestObj.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script")
                .getJSONObject("params").put("lat", latitude);
        jsonRequestObj.getJSONObject("script_fields").getJSONObject("distance").getJSONObject("script")
                .getJSONObject("params").put("lon", longitude);


        //构建Query
        Map<String, Object> cixingMap = analyzeCategoryKeyword(keyword);
        boolean isAffectFilter = true;
        jsonRequestObj.put("query", new JSONObject());
        //构建function score
        jsonRequestObj.getJSONObject("query").put("function_score", new JSONObject());
        //构建 function score内的query
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("query", new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").put("bool", new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").put("must", new JSONArray());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").add(new JSONObject());

        int queryIndex = 0;
        if (cixingMap.keySet().size() > 0 && isAffectFilter) {
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).put("bool", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).getJSONObject("bool").put("should", new JSONArray());
            int filterQueryIndex = 0;
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex).put("match", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex).getJSONObject("match").put("name", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex).getJSONObject("match")
                   .getJSONObject("name").put("query", keyword);
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex).getJSONObject("match")
                    .getJSONObject("name").put("boost", 0.1);

            for (String key : cixingMap.keySet()) {
                filterQueryIndex ++;
                Integer cixingCategoryId = (Integer)cixingMap.get(key);
                jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                        .getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").add(new JSONObject());
                jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                        .getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex).put("term", new JSONObject());
                jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                        .getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
                        .getJSONObject("term").put("category_id", new JSONObject());
                jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                        .getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
                        .getJSONObject("term").getJSONObject("category_id").put("value", cixingCategoryId);
                jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                        .getJSONObject(queryIndex).getJSONObject("bool").getJSONArray("should").getJSONObject(filterQueryIndex)
                        .getJSONObject("term").getJSONObject("category_id").put("boost", 0.1);

            }
        }else {
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).put("match", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).getJSONObject("match").put("name", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).getJSONObject("match").getJSONObject("name").put("query", keyword);
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).getJSONObject("match").getJSONObject("name").put("boost", 0.1);
        }

        queryIndex ++;
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").add(new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                .getJSONObject(queryIndex).put("term", new JSONObject());
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                .getJSONObject(queryIndex).getJSONObject("term").put("seller_disabled_flag", 0);
        if (tags != null) {
            queryIndex ++;
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).put("term", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).getJSONObject("term").put("tags", tags);
        }

        if (categoryId != null) {
            queryIndex ++;
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).put("term", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONObject("query").getJSONObject("bool").getJSONArray("must")
                    .getJSONObject(queryIndex).getJSONObject("term").put("category_id", categoryId);

        }

        //构建function部分
        jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("functions", new JSONArray());
        int functionIndex = 0;
        if (orderBy == null) {
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("gauss", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("gauss").put("location", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("gauss")
                    .getJSONObject("location").put("origin", latitude.toString() + "," + longitude.toString());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("gauss")
                    .getJSONObject("location").put("scale", "100km");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("gauss")
                    .getJSONObject("location").put("offset", "0km");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("gauss")
                    .getJSONObject("location").put("decay", "0.5");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("weight",9);

            functionIndex ++;
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("field_value_factor", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor")
                    .put("field", "remark_score");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("weight",0.2);

            functionIndex ++;
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("field_value_factor", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor")
                    .put("field", "seller_remark_score");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("weight",0.1);

            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("score_mode", "sum");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("boost_mode", "sum");
        }else {
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").add(new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("field_value_factor", new JSONObject());
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).getJSONObject("field_value_factor")
                    .put("field", "price_per_man");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").getJSONArray("functions").getJSONObject(functionIndex).put("weight",1);

            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("score_mode", "sum");
            jsonRequestObj.getJSONObject("query").getJSONObject("function_score").put("boost_mode", "replace");
        }

        //构建排序字段
        jsonRequestObj.put("sort", new JSONArray());
        jsonRequestObj.getJSONArray("sort").add(new JSONObject());
        jsonRequestObj.getJSONArray("sort").getJSONObject(0).put("_score", new JSONObject());
        if (orderBy == null) {
            jsonRequestObj.getJSONArray("sort").getJSONObject(0).getJSONObject("_score").put("order", "desc");
        }else {
            jsonRequestObj.getJSONArray("sort").getJSONObject(0).getJSONObject("_score").put("order", "asc");
        }

        //聚合字段
        jsonRequestObj.put("aggs", new JSONObject());
        jsonRequestObj.getJSONObject("aggs").put("group_by_tags", new JSONObject());
        jsonRequestObj.getJSONObject("aggs").getJSONObject("group_by_tags").put("terms", new JSONObject());
        jsonRequestObj.getJSONObject("aggs").getJSONObject("group_by_tags").getJSONObject("terms").put("field", "tags");

        String requestJson = jsonRequestObj.toJSONString();
        System.out.println(requestJson);
        request.setJsonEntity(requestJson);
        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        String responseStr = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        JSONArray jsonArray = jsonObject.getJSONObject("hits").getJSONArray("hits");
        List<Shop> shopList = new ArrayList<>();
        for (int i=0; i< jsonArray.size(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Integer id = new Integer(jsonObj.get("_id").toString());
            BigDecimal distance = new BigDecimal(jsonObj.getJSONObject("fields").getJSONArray("distance").get(0).toString());
            Shop shop = get(id);
            shop.setDistance(distance.multiply(new BigDecimal(1000).setScale(0, BigDecimal.ROUND_CEILING)).intValue());
            shopList.add(shop);
        }

        List<Map> tagsList = new ArrayList<>();
        JSONArray tagsJsonArray = jsonObject.getJSONObject("aggregations").getJSONObject("group_by_tags").getJSONArray("buckets");
        for (int i=0; i< tagsJsonArray.size(); i++) {
            JSONObject jsonObj = tagsJsonArray.getJSONObject(i);
            Map<String, Object> tagMap = new HashMap<>();
            tagMap.put("tags", jsonObj.getString("key"));
            tagMap.put("num", jsonObj.getString("doc_count"));
            tagsList.add(tagMap);
        }


        result.put("tags", tagsList);
        result.put("shop", shopList);
        return result;
    }



    //构造分词函数识别器
    private Map<String, Object> analyzeCategoryKeyword(String keyword) throws IOException {
        Map<String, Object> res = new HashMap<>();
        Request request = new Request("GET", "/shop/_analyze");
        request.setJsonEntity("{" + "\"field\": \"name\"," + " \"text\": \"" + keyword +"\"\n"+ "}");
        Response response = restHighLevelClient.getLowLevelClient().performRequest(request);
        String responseStr = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = JSONObject.parseObject(responseStr);
        JSONArray tokens = jsonObject.getJSONArray("tokens");
        for (int i=0; i< tokens.size(); i++) {
            String token = tokens.getJSONObject(i).getString("token");
            Integer categortId = getCategoryIdByToken(token);
            if (null != categortId) {
                res.put(token, categortId);
            }
        }
        return res;
    }
    private Integer getCategoryIdByToken(String token){
        for (Integer key : categoryWorkMap.keySet()) {
            List<String> tokenList = categoryWorkMap.get(key);
            if (tokenList.contains(token)) {
                return key;
            }
        }

        return null;
    }

    private Map<Integer, List<String>> categoryWorkMap = new HashMap<>();
    @PostConstruct //bean初始化后即可执行次函数
    public void init(){
        categoryWorkMap.put(1, new ArrayList<>());
        categoryWorkMap.put(2, new ArrayList<>());

        categoryWorkMap.get(1).add("吃饭");
        categoryWorkMap.get(1).add("下午茶");
        categoryWorkMap.get(2).add("休息");
        categoryWorkMap.get(2).add("住宿");
        categoryWorkMap.get(2).add("睡觉");
    }


}
