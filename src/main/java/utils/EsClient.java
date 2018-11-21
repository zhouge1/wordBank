package utils;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import org.apache.log4j.Logger;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class EsClient {

    private static final Logger logger = Logger.getLogger(EsClient.class);
    // 连接
    private static TransportClient client = null;
    private static BulkProcessor bulkProcessor = null;
    private static String host = "localhost";
    //private static String host = "192.168.72.6";
    private static int port = 9300;
    static {
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
            bulkProcessor = getBulkProcessor();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
//    /**
//     * get phones by location and distance
//     *
//     * @param index    index of es
//     * @param type     type of es
//     * @param lat      Centre of circle
//     * @param lon      Centre of circle
//     * @param distance Radius of circle
//     * @param maxSize  Max size of search's result
//     */
//    public SearchResponse getPhonesByLocationAndDistance(String index, String type, double lat, double lon, double distance, int maxSize) {
//        SearchRequestBuilder builder = client.prepareSearch(index).setTypes(type);
//        SearchResponse searchResponse = builder.setQuery(QueryBuilders.geoDistanceQuery("location").point(lat, lon).distance(distance, DistanceUnit.KILOMETERS)).setSize(maxSize).get();
//        return searchResponse;
//    }

    /**
     * get SearchResponse by geoDistanceQueryBuilder
     *
     * @param index
     * @param type
     * @param geoDistanceQueryBuilder
     * @return
     */
    public SearchResponse getSearchResponseByGeo(String index, String type, GeoDistanceQueryBuilder geoDistanceQueryBuilder) {
        SearchRequestBuilder builder = client.prepareSearch(index).setTypes(type);
        SearchResponse searchResponse = builder.setQuery(geoDistanceQueryBuilder).setSize(10000).get();
        return searchResponse;
    }
//    /**
//     * get phones by a location and range of time
//     * @param index name of index   eg."test"
//     * @param type  type of index   eg."doc"
//     * @param lat   latitude of location    eg."34.91539764"
//     * @param lon   longitude of location   eg."112.77053833"
//     * @param distance  radius of circle    eg."100.0" unit:km
//     * @param start beginTime   eg."2015年02月25 09:18:00"
//     * @param end   stopTime    eg."2015年02月25 09:18:00"
//     * @param maxSize Maximum number of results
//     * @return SearchResponse
//     */
//    public SearchResponse getPhonesByLocationAndTime(String index, String type, double lat, double lon, double distance, String start, String end,int maxSize) {
//        SearchRequestBuilder builder = client.prepareSearch(index).setTypes(type);
//        QueryBuilder geoDistanceQueryBuilder = QueryBuilders.geoDistanceQuery("location").point(lat, lon).distance(distance, DistanceUnit.KILOMETERS);
//        QueryBuilder startRangeQueryBuilder = QueryBuilders.rangeQuery("start").from(start).to(end).includeLower(true).includeUpper(true);
//        QueryBuilder endRangeQueryBuilder = QueryBuilders.rangeQuery("end").from(start).to(end).includeLower(true).includeUpper(true);
//        SearchResponse searchResponse = builder.setQuery(QueryBuilders.boolQuery()
//                .must(geoDistanceQueryBuilder)
//                .must(startRangeQueryBuilder)
//                .must(endRangeQueryBuilder))
//                .setSize(maxSize).get();
//        return searchResponse;
//    }


    /**
     * get SearchResponse by a location and range of time
     *
     * @param index
     * @param type
     * @param geoDistanceQueryBuilder
     * @param startRangeQueryBuilder
     * @param endRangeQueryBuilder
     * @return
     */
    public SearchResponse getSearchResponseByLocationAndRangeTime(String index, String type, QueryBuilder geoDistanceQueryBuilder, QueryBuilder startRangeQueryBuilder, QueryBuilder endRangeQueryBuilder) {
        SearchRequestBuilder builder = client.prepareSearch(index).setTypes(type);
        SearchResponse searchResponse = builder.setQuery(QueryBuilders.boolQuery()
                .must(geoDistanceQueryBuilder)
                .must(startRangeQueryBuilder)
                .must(endRangeQueryBuilder))
                .setSize(10000).get();
        return searchResponse;
    }

    /**
     * Intersection is obtained through multiple conditions
     *
     * @param index
     * @param type
     * @param builders
     * @return
     */
    public SearchResponse getSearchResponseBybuilders(String index, String type, List<QueryBuilder> builders) throws Exception {
        SearchRequestBuilder builder = client.prepareSearch(index).setTypes(type);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (QueryBuilder build : builders) {
            boolQueryBuilder.must(build);
        }
        SearchResponse searchResponse = builder.setQuery(boolQueryBuilder)
                .setSize(10000).get();
        return searchResponse;
    }

    /**
     * Intersection is obtained through multiple conditions
     *
     * @param index
     * @param type
     * @param agg
     * @return
     */
    public SearchResponse getSearchResponseAggsBybuilders(String index, String type, AbstractAggregationBuilder agg) throws Exception {
        SearchRequestBuilder builder = client.prepareSearch(index).setTypes(type);
        SearchResponse response = builder.addAggregation(agg)
                .execute()
                .actionGet();
        return response;
    }


    public AdminClient admin() {
        return client.admin();
    }

    /**
     * @Description: 关闭连接
     * @author lxk
     * @date 2017年11月11日
     */
    public void close() {
        client.close();
    }

    /**
     * @Description: 验证链接是否正常
     * @author lxk
     * @date 2017年11月11日
     */
    public boolean validate() {
        return client.connectedNodes().size() == 0 ? false : true;
    }

    /**
     * @Description:添加文档
     * @author lxk
     * @date 2017年11月3日
     */
    public void addDoc(String index, String type, Object id, Object object) {
        client.prepareIndex(index, type, id.toString()).setSource(JSON.toJSONString(object)).get();
    }

    /**
     * @Description:更新文档
     * @author lxk
     * @date 2017年11月3日
     */
    public void updateDoc(String index, String type, Object id, Object object) {
        client.prepareUpdate(index, type, id.toString()).setDoc(JSON.toJSONString(object)).get();
    }

    /**
     * @Description:删除文档
     * @author lxk
     * @date 2017年11月3日
     */
    public void delDoc(String index, String type, Object id) {
        client.prepareDelete(index, type, id.toString()).get();
    }

    /**
     * Judge whether or not a index that is indexName exists
     *
     * @param indexName
     * @return true:exists
     * false:not exists
     */
    public boolean existsIndex(String indexName) {
        IndicesExistsRequest inExistsRequest = new IndicesExistsRequest(indexName);
        IndicesExistsResponse inExistsResponse = client.admin().indices()
                .exists(inExistsRequest).actionGet();
        return inExistsResponse.isExists();
    }

    /**
     * delete index that is indexName
     *
     * @param indexName
     * @return true:success
     * false:fail
     */
    public  boolean deleteIndex(String indexName) {
        if (!existsIndex(indexName)) {
            return true;
        } else {
            DeleteIndexResponse dResponse = client.admin().indices().prepareDelete(indexName)
                    .execute().actionGet();
            if (dResponse.isAcknowledged()) {
                //client.close();
                return true;
            } else {
                //client.close();
                return false;
            }
        }
    }

    /**
     * create  index
     *
     * @param indexName
     */
    public static void createCluterName(String indexName) {
        client.admin().indices().prepareCreate(indexName).execute().actionGet();
        //client.close();
    }

    /**
     * create phone of mapping
     *
     * @param indexName
     * @param type
     */
    public void createPhoneMapping(String indexName, String type) {
        createCluterName(indexName);
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("location")
                    .field("type", "geo_point")
                    .endObject()
                    .startObject("phone")
                    .field("type", "text")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("start")
                    .field("type", "date")
                    .field("format", "yyyy年MM月dd HH:mm:ss")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("end")
                    .field("type", "date")
                    .field("format", "yyyy年MM月dd HH:mm:ss")
                    .field("index", "not_analyzed")
                    .endObject()
                    .endObject()
                    .endObject();
            PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(type).source(builder);
            client.admin().indices().putMapping(mapping).actionGet();
            //client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * create connection of mapping
     *
     * @param indexName
     * @param type
     */
    public void createConnectionMapping(String indexName, String type) {
        createCluterName(indexName);
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("other")
                    .field("type", "text")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("time")
                    .field("type", "date")
                    .field("format", "yyyy-MM-dd")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("ctype")
                    .field("type", "text")
                    .field("index", "not_analyzed")
                    .endObject()
                    .startObject("self")
                    .field("type", "text")
                    .field("index", "not_analyzed")
                    .endObject()
                    .endObject()
                    .endObject();
            PutMappingRequest mapping = Requests.putMappingRequest(indexName).type(type).source(builder);
            client.admin().indices().putMapping(mapping).actionGet();
            //client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BulkRequestBuilder getBulkRequestBuilder() {
        return client.prepareBulk();
    }

    public IndexRequestBuilder getIndexRequestBuilder(String index, String type, String id) {
        return client.prepareIndex(index, type, id);
    }

    private static BulkProcessor getBulkProcessor(){
        return BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    public void beforeBulk(long executionId, BulkRequest request) {
                        logger.info("---尝试插入" + request.numberOfActions() + "条数据---");
                    }

                    public void afterBulk(long executionId,
                                          BulkRequest request, BulkResponse response) {
                        logger.info("---插入" + request.numberOfActions() + "条数据成功---");
                    }

                    public void afterBulk(long executionId,
                                          BulkRequest request, Throwable failure) {

                    }
                })
                .setBulkActions(5000)
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
    }

    /**
     * @return the client
     */
    public static TransportClient getClient() {
        return client;
    }

    /**
     *
     * @return bulkProcessor
     */
    public static BulkProcessor getBulkProcessorsec() {
        return bulkProcessor;
    }


    public static boolean indexExists(String index){
        IndicesExistsRequest request = new IndicesExistsRequest(index);
        IndicesExistsResponse response = client.admin().indices().exists(request).actionGet();
        if (response.isExists()) {
            return true;
        }
        return false;
    }
}
