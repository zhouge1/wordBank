import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.Map;

public class Get {
    public static void main(String[] args) {
        RestHighLevelClient client = ClientUtils.client;
        GetRequest getRequest = new GetRequest(
                "test_1",
                "doc",
                "1");
        try {

            GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);

            String index = getResponse.getIndex();
            String type = getResponse.getType();
            String id = getResponse.getId();
            System.out.println("index: " + index + " type:" + type + " id: " + id);
            if (getResponse.isExists()) {
                long version = getResponse.getVersion();
                String sourceAsString = getResponse.getSourceAsString();
                Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
                byte[] sourceAsBytes = getResponse.getSourceAsBytes();
                System.out.println("version: " + version);
                System.out.println("sourceAsString: " + sourceAsString);
                System.out.println("sourceAsMap: "+sourceAsMap);
                System.out.println("sourceAsBytes: "+sourceAsBytes);
            } else {

            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

/**
 * D:\bigdata\tools\jdk\bin\java.exe -javaagent:D:\bigdata\tools\IntelliJIDEA2018.2.2\lib\idea_rt.jar=50485:D:\bigdata\tools\IntelliJIDEA2018.2.2\bin -Dfile.encoding=UTF-8 -classpath D:\bigdata\tools\jdk\jre\lib\charsets.jar;D:\bigdata\tools\jdk\jre\lib\deploy.jar;D:\bigdata\tools\jdk\jre\lib\ext\access-bridge-64.jar;D:\bigdata\tools\jdk\jre\lib\ext\cldrdata.jar;D:\bigdata\tools\jdk\jre\lib\ext\dnsns.jar;D:\bigdata\tools\jdk\jre\lib\ext\jaccess.jar;D:\bigdata\tools\jdk\jre\lib\ext\jfxrt.jar;D:\bigdata\tools\jdk\jre\lib\ext\localedata.jar;D:\bigdata\tools\jdk\jre\lib\ext\nashorn.jar;D:\bigdata\tools\jdk\jre\lib\ext\sunec.jar;D:\bigdata\tools\jdk\jre\lib\ext\sunjce_provider.jar;D:\bigdata\tools\jdk\jre\lib\ext\sunmscapi.jar;D:\bigdata\tools\jdk\jre\lib\ext\sunpkcs11.jar;D:\bigdata\tools\jdk\jre\lib\ext\zipfs.jar;D:\bigdata\tools\jdk\jre\lib\javaws.jar;D:\bigdata\tools\jdk\jre\lib\jce.jar;D:\bigdata\tools\jdk\jre\lib\jfr.jar;D:\bigdata\tools\jdk\jre\lib\jfxswt.jar;D:\bigdata\tools\jdk\jre\lib\jsse.jar;D:\bigdata\tools\jdk\jre\lib\management-agent.jar;D:\bigdata\tools\jdk\jre\lib\plugin.jar;D:\bigdata\tools\jdk\jre\lib\resources.jar;D:\bigdata\tools\jdk\jre\lib\rt.jar;D:\eclipse-workspace\esjavatest\target\classes;D:\bigdata\tools\repo_ali\org\elasticsearch\client\elasticsearch-rest-high-level-client\6.4.2\elasticsearch-rest-high-level-client-6.4.2.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\elasticsearch\6.4.2\elasticsearch-6.4.2.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\elasticsearch-core\6.4.2\elasticsearch-core-6.4.2.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\elasticsearch-secure-sm\6.4.2\elasticsearch-secure-sm-6.4.2.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\elasticsearch-x-content\6.4.2\elasticsearch-x-content-6.4.2.jar;D:\bigdata\tools\repo_ali\org\yaml\snakeyaml\1.17\snakeyaml-1.17.jar;D:\bigdata\tools\repo_ali\com\fasterxml\jackson\core\jackson-core\2.8.10\jackson-core-2.8.10.jar;D:\bigdata\tools\repo_ali\com\fasterxml\jackson\dataformat\jackson-dataformat-smile\2.8.10\jackson-dataformat-smile-2.8.10.jar;D:\bigdata\tools\repo_ali\com\fasterxml\jackson\dataformat\jackson-dataformat-yaml\2.8.10\jackson-dataformat-yaml-2.8.10.jar;D:\bigdata\tools\repo_ali\com\fasterxml\jackson\dataformat\jackson-dataformat-cbor\2.8.10\jackson-dataformat-cbor-2.8.10.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-core\7.4.0\lucene-core-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-analyzers-common\7.4.0\lucene-analyzers-common-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-backward-codecs\7.4.0\lucene-backward-codecs-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-grouping\7.4.0\lucene-grouping-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-highlighter\7.4.0\lucene-highlighter-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-join\7.4.0\lucene-join-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-memory\7.4.0\lucene-memory-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-misc\7.4.0\lucene-misc-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-queries\7.4.0\lucene-queries-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-queryparser\7.4.0\lucene-queryparser-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-sandbox\7.4.0\lucene-sandbox-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-spatial\7.4.0\lucene-spatial-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-spatial-extras\7.4.0\lucene-spatial-extras-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-spatial3d\7.4.0\lucene-spatial3d-7.4.0.jar;D:\bigdata\tools\repo_ali\org\apache\lucene\lucene-suggest\7.4.0\lucene-suggest-7.4.0.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\elasticsearch-cli\6.4.2\elasticsearch-cli-6.4.2.jar;D:\bigdata\tools\repo_ali\net\sf\jopt-simple\jopt-simple\5.0.2\jopt-simple-5.0.2.jar;D:\bigdata\tools\repo_ali\com\carrotsearch\hppc\0.7.1\hppc-0.7.1.jar;D:\bigdata\tools\repo_ali\joda-time\joda-time\2.10\joda-time-2.10.jar;D:\bigdata\tools\repo_ali\com\tdunning\t-digest\3.2\t-digest-3.2.jar;D:\bigdata\tools\repo_ali\org\hdrhistogram\HdrHistogram\2.1.9\HdrHistogram-2.1.9.jar;D:\bigdata\tools\repo_ali\org\apache\logging\log4j\log4j-api\2.11.1\log4j-api-2.11.1.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\jna\4.5.1\jna-4.5.1.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\client\elasticsearch-rest-client\6.4.2\elasticsearch-rest-client-6.4.2.jar;D:\bigdata\tools\repo_ali\org\apache\httpcomponents\httpclient\4.5.2\httpclient-4.5.2.jar;D:\bigdata\tools\repo_ali\org\apache\httpcomponents\httpcore\4.4.5\httpcore-4.4.5.jar;D:\bigdata\tools\repo_ali\org\apache\httpcomponents\httpasyncclient\4.1.2\httpasyncclient-4.1.2.jar;D:\bigdata\tools\repo_ali\org\apache\httpcomponents\httpcore-nio\4.4.5\httpcore-nio-4.4.5.jar;D:\bigdata\tools\repo_ali\commons-codec\commons-codec\1.10\commons-codec-1.10.jar;D:\bigdata\tools\repo_ali\commons-logging\commons-logging\1.1.3\commons-logging-1.1.3.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\plugin\parent-join-client\6.4.2\parent-join-client-6.4.2.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\plugin\aggs-matrix-stats-client\6.4.2\aggs-matrix-stats-client-6.4.2.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\plugin\rank-eval-client\6.4.2\rank-eval-client-6.4.2.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\plugin\lang-mustache-client\6.4.2\lang-mustache-client-6.4.2.jar;D:\bigdata\tools\repo_ali\com\github\spullara\mustache\java\compiler\0.9.3\compiler-0.9.3.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\client\transport\6.4.2\transport-6.4.2.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\plugin\transport-netty4-client\6.4.2\transport-netty4-client-6.4.2.jar;D:\bigdata\tools\repo_ali\io\netty\netty-buffer\4.1.16.Final\netty-buffer-4.1.16.Final.jar;D:\bigdata\tools\repo_ali\io\netty\netty-codec\4.1.16.Final\netty-codec-4.1.16.Final.jar;D:\bigdata\tools\repo_ali\io\netty\netty-codec-http\4.1.16.Final\netty-codec-http-4.1.16.Final.jar;D:\bigdata\tools\repo_ali\io\netty\netty-common\4.1.16.Final\netty-common-4.1.16.Final.jar;D:\bigdata\tools\repo_ali\io\netty\netty-handler\4.1.16.Final\netty-handler-4.1.16.Final.jar;D:\bigdata\tools\repo_ali\io\netty\netty-resolver\4.1.16.Final\netty-resolver-4.1.16.Final.jar;D:\bigdata\tools\repo_ali\io\netty\netty-transport\4.1.16.Final\netty-transport-4.1.16.Final.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\plugin\reindex-client\6.4.2\reindex-client-6.4.2.jar;D:\bigdata\tools\repo_ali\org\elasticsearch\plugin\percolator-client\6.4.2\percolator-client-6.4.2.jar;D:\bigdata\tools\repo_ali\com\alibaba\fastjson\1.2.47\fastjson-1.2.47.jar Get
 * index: test_1 type:doc id: 1
 * version: 1
 * sourceAsString: {"user":"kimchy","postDate":"2013-01-30","message":"trying out Elasticsearch"}
 * sourceAsMap: {postDate=2013-01-30, message=trying out Elasticsearch, user=kimchy}
 * sourceAsBytes: [B@5bd03f44
 *
 * Process finished with exit code 0
 */
