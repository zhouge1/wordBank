package com.word.bank;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import utils.EsClient;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class WordBankService {

    private static final Logger logger = Logger.getLogger(WordBankService.class);
    private static final BulkProcessor bulkProcessor = EsClient.getBulkProcessorsec();
    private static final String WORD_BANK = "word_bank";
    private static final String WORD_BANK_TYPE = "doc";

    /**
     * create index form word_bank mapping
     */
    public static void createWordBank() {
        //String indexName = "word_bank";
        if (!EsClient.indexExists(WORD_BANK)) {
            EsClient.createCluterName(WORD_BANK);
            XContentBuilder builder = null;
            try {
                builder = XContentFactory.jsonBuilder()
                        .startObject()
                        .startObject("properties")
                        .startObject("word")
                        .field("type", "keyword")
                        .endObject()
                        .startObject("wordType")
                        .field("type", "keyword")
                        .endObject()
                        .startObject("number")
                        .field("type", "integer")
                        .endObject()
                        .endObject()
                        .endObject();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
            PutMappingRequest mapping = Requests.putMappingRequest(WORD_BANK).type(WORD_BANK_TYPE).source(builder);
            EsClient.getClient().admin().indices().putMapping(mapping).actionGet();
            logger.info("create word_bank index");
        } else {
            logger.info("word_bank index exist");
        }

    }

    /**
     * insert into es from datas
     */
    public static void addDataToES(JSONArray datas) {
        Random rand = new Random();
        for (Object object : datas) {
            JSONObject wordBank = JSONObject.parseObject(object.toString());
            String type = wordBank.getString("type");
            JSONArray data = wordBank.getJSONArray("data");
            for (Object word : data) {
                JSONObject esObject = new JSONObject();
                esObject.put("word", word);
                esObject.put("number", rand.nextInt(1000));
                esObject.put("wordType", type);
                bulkProcessor.add(new IndexRequest(WORD_BANK, WORD_BANK_TYPE, UUID.randomUUID().toString()).source(esObject));
            }
        }
        bulkProcessor.flush();
        bulkProcessor.close();
    }

}
