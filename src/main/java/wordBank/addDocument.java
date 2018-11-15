package wordBank;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import java.io.IOException;


public class addDocument {
    public static void main(String[] args){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));

        BulkRequest request = new BulkRequest();
        JSONObject object = new JSONObject();
        object.put("word","杨幂");
        JSONObject labels = new JSONObject();
        //"word":"杨幂","labels":{"word_class":"名词","word_feeling":"中性词","word_origin":"杨幂，1986年9月12日出生于北京市，中国内地影视女演员、流行乐歌手、影视制片人。"}
        labels.put("word_class","名词");
        labels.put("word_feeling","中性词");
        labels.put("word_origin","杨幂，1986年9月12日出生于北京市，中国内地影视女演员、流行乐歌手、影视制片人。");
        object.put("word_class",labels);
        request.add(new IndexRequest("word_bank", "doc", "1")
                .source(object.toString(),XContentType.JSON));

        try {
            BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                DocWriteResponse itemResponse = bulkItemResponse.getResponse();

                if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                        || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
                    IndexResponse indexResponse = (IndexResponse) itemResponse;

                } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
                    UpdateResponse updateResponse = (UpdateResponse) itemResponse;

                } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
                    DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                }
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("----already end---");
    }

}
