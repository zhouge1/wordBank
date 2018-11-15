import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Date;

public class Index {
    public static void main(String[] args) {
        System.out.println("---------------");
        RestHighLevelClient client = ClientUtils.client;
        try {

            IndexRequest request = secondDocument();
            IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);

            String index = indexResponse.getIndex();
            String type = indexResponse.getType();
            String id = indexResponse.getId();
            long version = indexResponse.getVersion();
            System.out.println(indexResponse.status().name());
            System.out.println("index: " + index + " type: " + type + " id: " + id + " version: " + version);
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                System.out.println("---Handle (if needed) the case where the document was created for the first time------");
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                System.out.println("----Handle (if needed) the case where the document was rewritten as it was already existing-----");
            }
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                System.out.println("----Handle (if needed) the case where the document was rewritten as it was already existing-----");
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    String reason = failure.reason();
                    System.out.println("reason of faile is " + reason);
                }
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static IndexRequest firstDocument() {
        IndexRequest request = new IndexRequest(
                "test_1",
                "doc",
                "1");

        String jsonString =
                "{" +
                        "\"user\":\"kimchy\"," +
                        "\"postDate\":\"2013-01-30\"," +
                        "\"message\":\"trying out Elasticsearch\"" +
                        "}";
        request.source(jsonString, XContentType.JSON);
        return request;
    }

    public static IndexRequest secondDocument() {
        IndexRequest request = new IndexRequest(
                "test_1",
                "doc",
                "2");
        JSONObject json = new JSONObject();
        json.put("user", "world is danger");
        json.put("postDate", "2013-01-31");
        json.put("message", "live on");
        String jsonString = json.toJSONString();
        request.source(jsonString, XContentType.JSON);
        return request;
    }

    public static IndexRequest thirdDocument() {
        IndexRequest request = null;
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.field("user", "What is beauty and what is truth");
                builder.timeField("postDate", new Date());
                builder.field("message", "Is that what you want ");
            }
            builder.endObject();
            request = new IndexRequest("test_1", "doc", "3")
                    .source(builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return request;
    }
}
