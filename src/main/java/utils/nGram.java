package utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.client.transport.TransportClient;
import query.terms;

import java.util.Set;

public class NGram {
    private static TransportClient client = null;

    private static int MIN_SIZE = 1;
    private static int MAX_SIZE = 10;
    static {
	client = EsClient.getClient();
    }
    public static void main(String[] args) {
	System.out.println("-----------------");
	String text = "大漠孤烟直，长河落日圆秦淮河，中国(长江)下游右岸{支流。古称龙}藏浦，汉代<起>称淮水，唐以后改/称《秦淮》。 [1]  秦淮河有南北两源，北源句容河发源于句容市宝华山南麓，南源溧水河发源于南京市溧水区东庐山，两河在南京市江宁区方山埭西北村汇合成秦淮河干流，绕过方山向西北至外城城门上坊门从东水关流入南京城，由东向西横贯市区，南部从西水关流出，注入长江。";

	//System.out.println(getWords(text).toJavaList(String.class));
	Set<String> words = terms.termsApi(client,"word_bank","word",getWords(text).toJavaList(String.class));
	getIndex(text,words);
    }

    public static void getIndex(String document,Set<String> words){
	JSONObject result = new JSONObject();
	for (String word:words) {
	    result.put(word,document.indexOf(word));
	}
    }

    public static JSONArray getWords(String text){
	JSONArray lines = tokenizer(text);
	JSONArray words = new JSONArray();
	for(int i=0;i<lines.size();i++){
	    words = ngram(lines.getString(i),words,MAX_SIZE);
	}
	return words;
    }
    public static JSONArray tokenizer(String text){
	String[] texts = text.split("、|，|。|；|？|！|,|\\.|;|\\?|!|]|}|\\{|\\[|}|\\{|<|>|《|》|-|_|=|\\+|\\)|\\(|\\.|/");
	JSONArray tokenizers = new JSONArray();
	for (String line : texts) {
	    tokenizers.add(line);
	}
	return tokenizers;
    }

    public static JSONArray ngram(String line ,JSONArray words,int n){
	for(int i = MIN_SIZE;i<=n;i++) {
	    words = gram(line, words, i);
	}
	return words;
    }
    public static JSONArray gram(String line, JSONArray words, int n) {
	line = line.trim();
	if (line.length() < n) {
	    return words;
	} else {
	    int length = line.length();
	    if (length == n) {
		words.add(line);
	    }else {
		for(int i=0;i<=length-n;i++){
		    words.add(line.substring(i,i+n));
		}
	    }
	    return words;
	}
    }
}
