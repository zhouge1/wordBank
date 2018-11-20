package utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.collect.HppcMaps;
import query.terms;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
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
	//String text = "大漠孤烟直，长河落日圆秦淮河，中国(长江)下游右岸{支流。古称龙}藏浦，汉代<起>称淮水，唐以后改/称《秦淮》。 [1]  秦淮河有南北两源，北源句容河发源于句容市宝华山南麓，南源溧水河发源于南京市溧水区东庐山，两河在南京市江宁区方山埭西北村汇合成秦淮河干流，绕过方山向西北至外城城门上坊门从东水关流入南京城，由东向西横贯市区，南部从西水关流出，注入长江。";
	String text = "床前明月光，疑是地上霜。\n" +
			      "举头望明月，低头思故乡。";

	//System.out.println(getWords(text).toJavaList(String.class));
	Set<String> words = terms.termsApi(client,"word_bank","word",getWords(text).toJavaList(String.class));
	JSONObject positions = positionOfWord(text,words);
	positions = show(words,positions);
	//System.out.println(distinct(words,positions));
	positions = showResult(words,positions);
	show(positions);
    }

    public static void show(JSONObject positions){
	Set<String> set = positions.keySet();
	Iterator<String> iterator = set.iterator();
	JSONArray ss = new JSONArray();
	while (iterator.hasNext()){
	    int postion = Integer.parseInt(iterator.next());
	    ss.add(postion);
	}
	ss.sort(new Comparator<Object>() {
	    @Override
	    public int compare(Object num1, Object num2) {
		return Integer.parseInt(num1.toString())-Integer.parseInt(num2.toString());
	    }

	});
	Object[] s = ss.toArray();
	for(int i=0;i<s.length;i++){
	    System.out.println(positions.getString(s[i].toString()));
	}
    }
    public static JSONObject showResult(Set<String> words,JSONObject positions){
	Iterator<String> iterator = words.iterator();
	JSONObject result = new JSONObject();
	while (iterator.hasNext()) {
	    String word = iterator.next();
	    JSONArray ps = positions.getJSONArray(word);
	    if(!(ps==null || ps.size()==0)) {
		for (int i = 0; i < ps.size(); i++) {
		    String p = ps.getString(i);
		    if (!p.contains("-")) {
			result.put(p, word);
		    } else {
			result.put(p.split("-")[0], word);
		    }
		}
	    }
	}
	return result;
    }
    public static JSONObject distinct(Set<String> words,JSONObject positions){
	Iterator<String> iterator = words.iterator();
	while (iterator.hasNext()){
	    String word = iterator.next();
	    if(positions.getJSONArray(word).isEmpty()){
		positions.remove(word);
	    }
	}
	return positions;
    }
    public static JSONObject show(Set<String> words,JSONObject positions){
	Object[] ws = words.toArray();
	Iterator<String> iterator = words.iterator();
	while (iterator.hasNext()){
	    String word = iterator.next();

	    for(int i=0;i< ws.length;i++){
		String w = ws[i].toString();
		if(word.contains(w) && !word.equals(w)){
		    JSONArray wpostion = positions.getJSONArray(w);
		    JSONArray ps = positions.getJSONArray(word);
		    for(int j=0;j<ps.size();j++){
			for(int k=0;k<wpostion.size();k++){
			    if(ps.getString(j).contains(wpostion.getString(k))){
				wpostion.remove(k);
			    }
			}
		    }
		}
	    }
	}
	return positions;
    }
    public static JSONObject positionOfWord(String text,Set<String> words){
	JSONObject postions = new JSONObject();
	int n = text.length();
	for(int i=0;i<MAX_SIZE;i++){
	    for(int j=0;j<n-i;j++) {
		String word = text.substring(j,j+i);
		String indexString = null;
		if(words.contains(word)){
		    switch (i) {
			case 1:
			    indexString = j + "";
			    break;
			case 2:
			    indexString = j + "-" + (j + 1);
			    break;
			case 3:
			    indexString = j + "-" + (j + 1) + "-" + (j + 2);
			    break;
			case 4:
			    indexString = j + "-" + (j + 1) + "-" + (j + 2)+ "-" + (j + 3);
			    break;
			case 5:
			    indexString = j + "-" + (j + 1) + "-" + (j + 2)+ "-" + (j + 3)+ "-" + (j + 4);
			    break;
			case 6:
			    indexString = j + "-" + (j + 1) + "-" + (j + 2)+ "-" + (j + 3)+ "-" + (j + 4)+ "-" + (j + 5);
			    break;
			case 7:
			    indexString = j + "-" + (j + 1) + "-" + (j + 2)+ "-" + (j + 3)+ "-" + (j + 4)+ "-" + (j + 5)+ "-" + (j + 6);
			    break;
			case 8:
			    indexString = j + "-" + (j + 1) + "-" + (j + 2)+ "-" + (j + 3)+ "-" + (j + 4)+ "-" + (j + 5)+ "-" + (j + 6)+ "-" + (j + 7);
			    break;
			case 9:
			    indexString = j + "-" + (j + 1) + "-" + (j + 2)+ "-" + (j + 3)+ "-" + (j + 4)+ "-" + (j + 5)+ "-" + (j + 6)+ "-" + (j + 7)+ "-" + (j + 8);
			    break;
			case 10:
			    indexString = j + "-" + (j + 1) + "-" + (j + 2)+ "-" + (j + 3)+ "-" + (j + 4)+ "-" + (j + 5)+ "-" + (j + 6)+ "-" + (j + 7)+ "-" + (j + 8)+ "-" + (j + 9);
			    break;
		    }

		    if (!postions.containsKey(word)) {
			JSONArray set = new JSONArray();
			set.add(indexString);
			postions.put(word, set);
		    } else {
			JSONArray set = postions.getJSONArray(word);
			set.add(indexString);
			postions.put(word,set );
		    }
		}

	    }

	}
	//System.out.println(postions);
	return postions;
    }
    public static void getIndex(String document,Set<String> words){
	JSONObject result = new JSONObject();
	for (String word:words) {
	    result.put(word,document.indexOf(word));
	}
	System.out.println(result);
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
