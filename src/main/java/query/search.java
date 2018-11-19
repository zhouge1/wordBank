package query;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import utils.EsClient;

import java.util.Iterator;


public class search {
    private static TransportClient client = null;

    static {
	client = EsClient.getClient();
    }

    public static void main(String[] args) {
	System.out.println("----------------");
	//searchAPIQuery();
	MultiSearchAPIQuery();
    }

    /**
     * eg. of es
     * <p>
     * <p>
     * SearchResponse response = client.prepareSearch("index1", "index2")
     * .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
     * .setQuery(QueryBuilders.termQuery("multi", "test"))                 // Query
     * .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
     * .setFrom(0).setSize(60).setExplain(true)
     * .get();
     * <p>
     * // MatchAll on the whole cluster with all default options
     * SearchResponse response = client.prepareSearch().get();
     */

//    GET word_bank/_search
//    {
//	"query": {
//	"term": {
//	    "wordType": {
//		"value": "行政区划地名"
//	    }
//	}
//    },
//	"post_filter": {
//	"range": {
//	    "number": {
//		"gte": 12,
//			       "lte": 18
//	    }
//	}
//    },
//	"size": 20,
//    "explain": true
//    }
    public static void searchAPIQuery() {
	SearchResponse response = client.prepareSearch("word_bank")
					  .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
					  .setQuery(QueryBuilders.termQuery("wordType", "行政区划地名"))                 // Query
					  .setPostFilter(QueryBuilders.rangeQuery("number").from(12).to(18))     // Filter
					  .setFrom(0).setSize(60).setExplain(true)
					  .get();
	System.out.println(response.getHits().getTotalHits());
    }

    /**
     * SearchRequestBuilder srb1 = client
     * .prepareSearch().setQuery(QueryBuilders.queryStringQuery("elasticsearch")).setSize(1);
     * SearchRequestBuilder srb2 = client
     * .prepareSearch().setQuery(QueryBuilders.matchQuery("name", "kimchy")).setSize(1);
     * <p>
     * MultiSearchResponse sr = client.prepareMultiSearch()
     * .add(srb1)
     * .add(srb2)
     * .get();
     * <p>
     * // You will get all individual responses from MultiSearchResponse#getResponses()
     * long nbHits = 0;
     * for (MultiSearchResponse.Item item : sr.getResponses()) {
     * SearchResponse response = item.getResponse();
     * nbHits += response.getHits().getTotalHits();
     * }
     */
    public static void MultiSearchAPIQuery() {
	SearchRequestBuilder srb1 = client.prepareSearch().setQuery(QueryBuilders.queryStringQuery("安宁镇")).setSize(1);
	SearchRequestBuilder srb2 = client.prepareSearch().setQuery(QueryBuilders.matchQuery("wordType", "行政区划地名")).setSize(1);

	MultiSearchResponse sr = client.prepareMultiSearch()
					 .add(srb1)
					 .add(srb2)
					 .get();

// You will get all individual responses from MultiSearchResponse#getResponses()
	long nbHits = 0;
	for (MultiSearchResponse.Item item : sr.getResponses()) {
	    SearchResponse response = item.getResponse();
	    System.out.println(response.getHits().getTotalHits());
	    SearchHit[] hits = response.getHits().getHits();
	    for(SearchHit searchHit:hits)
		System.out.println(searchHit.getSourceAsMap());
	    }

    }
}
