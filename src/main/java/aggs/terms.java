package aggs;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.global.GlobalAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import utils.EsClient;

public class terms {
    private static TransportClient client = null;

    static {
        client = EsClient.getClient();
    }

    public static void main(String[] args) {
        TermsAggregationBuilder builder = AggregationBuilders.terms("bucket_test").field("wordType");


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
}
