package agg;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import utils.EsClient;


public class termsAggs {
    private static TransportClient client = null;

    static {
        client = EsClient.getClient();
    }

    public static void main(String[] args) {
        //TermsAggregationBuilder builder = AggregationBuilders.terms("bucket_test").field("wordType");
        System.out.println("------");
        termsBucket();
        termsSum();
    }

    /**
     GET /word_bank/_search
     {
     "size": 0,
     "aggs": {
     "terms_test": {
     "cardinality": {
     "field": "wordType"
     }
     }
     }
     }
     */
    public static void termsBucket(){

        SearchResponse sr = client.prepareSearch("word_bank").setTypes("doc")
                .setQuery(QueryBuilders.matchAllQuery())
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .addAggregation(AggregationBuilders.terms("bucket_test").field("wordType")
                        /*.subAggregation(AggregationBuilders.sum("sum").field("number"))*/).get();
        // sr is here your SearchResponse object
        Terms genders = sr.getAggregations().get("bucket_test");
        // For each entry
        for (Terms.Bucket entry : genders.getBuckets()) {
            System.out.println(entry.getKey());      // Term
            System.out.println(entry.getDocCount()); // Doc count
        }
    }


    /**
     GET /word_bank/_search
     {
     "size": 0,
     "aggs": {
     "terms_test": {
     "terms": {
     "field": "wordType",
     "size": 50
     },
     "aggs": {
     "stats": {
     "stats": {
     "field": "number"
     }
     }
     }
     }
     }
     }

     */
    public static void termsSum(){
        SearchResponse sr = client.prepareSearch("word_bank").setTypes("doc")
                .setQuery(QueryBuilders.matchAllQuery())
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .addAggregation(AggregationBuilders.terms("bucket_test").field("wordType")
                        .subAggregation(AggregationBuilders.sum("sum").field("number"))).get();
        // sr is here your SearchResponse object
        Terms genders = sr.getAggregations().get("bucket_test");
        // For each entry
        for (Terms.Bucket entry : genders.getBuckets()) {
            System.out.println("-----metric-------");
            System.out.println(entry.getKey());      // Term
            System.out.println(entry.getDocCount()); // Doc count
            Sum sum = entry.getAggregations().get("sum");
            System.out.println("----- sum -------");
            System.out.println(sum.getName());
            System.out.println(sum.getValue());
        }
    }
}
