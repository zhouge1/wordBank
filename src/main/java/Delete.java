import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;

public class Delete {
    public static void main(String[] args) {
        System.out.println("-------------");
        DeleteRequest request = new DeleteRequest(
                "test_1",
                "doc",
                "1");

        RestHighLevelClient client = ClientUtils.client;
        try {
            DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
            String index = deleteResponse.getIndex();
            String type = deleteResponse.getType();
            String id = deleteResponse.getId();
            long version = deleteResponse.getVersion();
            RestStatus restStatus = deleteResponse.status();
            System.out.println("restStatus: "+restStatus);
            System.out.println("index: " + index + " type: " + type + " id: " + id + " version: " + version);
            ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                System.err.println("shard number != shard success number!");
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    String reason = failure.reason();
                    System.err.println("reason: " + reason);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
