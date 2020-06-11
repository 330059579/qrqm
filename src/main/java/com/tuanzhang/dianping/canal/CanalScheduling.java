package com.tuanzhang.dianping.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.SourceContext;
import com.tuanzhang.dianping.dal.ShopDAO;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CanalScheduling implements Runnable, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Resource
    private CanalConnector canalConnector;
    @Resource
    private ShopDAO shopDAO;
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    @Scheduled(fixedDelay = 100)  //每隔100ms拉取一次数据
    public void run() {
        long batchId = -1;
        try {
            int batchSize = 1000; //每次1000条
            Message message = canalConnector.getWithoutAck(batchSize);
            batchId= message.getId();
            List<CanalEntry.Entry> entries = message.getEntries();
            if (batchId != -1 && entries.size() > 0) {  //判断是否消费到消息
                entries.forEach(entry -> {
                    if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA){
                        //解析处理
                        publishCanalEvent(entry);
                    }
                });
            }

            canalConnector.ack(batchId);
        }catch (Exception e) {
            e.printStackTrace();
            canalConnector.rollback(batchId);
        }
    }

    private void publishCanalEvent(CanalEntry.Entry entry){
        CanalEntry.EntryType entryType = entry.getEntryType();
        String database = entry.getHeader().getSchemaName();
        String tableName = entry.getHeader().getTableName();
        CanalEntry.RowChange rowChange = null;
        try {
            rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        }catch (InvalidProtocolBufferException e) {
            e.printStackTrace();;
            return;
        }

        rowChange.getRowDatasList().forEach(rowData -> {
            List<CanalEntry.Column> columns = rowData.getAfterColumnsList();
            String primaryKey = "id";
            CanalEntry.Column idColumn = columns.stream().filter(column -> column.getIsKey() && primaryKey.equals(column.getName())).findFirst().orElse(null);
            Map<String, Object> dataMap = parseColumnsToMap(columns);
            try {
                indexEs(dataMap, database, tableName);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    Map<String, Object> parseColumnsToMap(List<CanalEntry.Column> columns) {
        Map<String, Object> jsonMap = new HashMap<>();
        columns.forEach(column -> {
            if (column == null) {
                return;
            }

            jsonMap.put(column.getName(), column.getValue());
        });
        return jsonMap;
    }

    private void  indexEs(Map<String, Object> dataMap, String database, String tableName) throws IOException {
        if (!StringUtils.equals("dianping", database)) {
            return;
        }

        List<Map<String, Object>> result = new ArrayList<>();
        if (StringUtils.equals("seller", tableName)) {
            result = shopDAO.buildEsQuery(new Integer((String)dataMap.get("id")), null, null);
        }

        if (StringUtils.equals("category", tableName)) {
            result = shopDAO.buildEsQuery(null, new Integer((String)dataMap.get("id")), null);
        }

        if (StringUtils.equals("shop", tableName)) {
            result = shopDAO.buildEsQuery(null, null, new Integer((String)dataMap.get("id")));
        }

        System.out.println(dataMap + database + tableName);
        for (Map<String, Object> map : result) {
            IndexRequest indexRequest = new IndexRequest("shop");
            indexRequest.id(String.valueOf(map.get("id")));
            indexRequest.source(map);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
