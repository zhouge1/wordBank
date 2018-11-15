import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

public class ClientUtils {
    public static RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http")));
    /**
     *  饿汉模式
     */
//    private ClientUtils(){
//        client = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("localhost", 9200, "http")));
//    }
//    private static ClientUtils instance = new ClientUtils();
//    public static ClientUtils getInstance(){
//        return instance;
//    }
}
