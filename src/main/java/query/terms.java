package query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import utils.EsClient;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 GET /_search
 {
 "query": {
 "terms" : { "user" : ["kimchy", "elasticsearch"]}
 }
 }

 GET /word_bank/_search
 {
 "query": {
 "terms": {
 "word": [
 "单核吞噬细",
 "单核细条件培养液",
 "丹粒"
 ]
 }
 }
 }
 */
public class terms {
    private static TransportClient client = null;

    static {
        client = EsClient.getClient();
    }
    /**
     * termsQuery("tags",
     *         "blue", "pill");
     * @param args
     */
    public static void main(String[] args){
        Set set = new HashSet<String>();
        set.add("单核吞噬细");
        set.add("单核细条件培养液");
        set.add("丹粒");
        System.out.println(termsApi(client,"word_bank","word",set));
    }

    /**
     *         Set set = new HashSet<String>();
     *         set.add("单核吞噬细");
     *         set.add("单核细条件培养液");
     *         set.add("丹粒");
     *         termsApi(client,"word_bank","word",set);
     * @param client
     * @param index
     * @param field
     * @param values
     */
    public static Set<String> termsApi(TransportClient client,String index,String field,Collection<String> values){
        TermsQueryBuilder termsQueryBuilder =  QueryBuilders.termsQuery(field,values);
        SearchResponse response = client.prepareSearch(index)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(termsQueryBuilder).setSize(10000)                 // Query
                .execute().actionGet();
        //TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("word","");
        Set<String> result = new HashSet<String>();
        for (SearchHit hit :response.getHits()) {
            result.add(hit.getSourceAsMap().get("word").toString());
        }
        return result;
    }
}
