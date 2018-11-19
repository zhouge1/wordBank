package utils;

import com.alibaba.fastjson.JSONArray;

public class nGram {
    public static void main(String[] args){
        System.out.println("-----------------");
        String text = "秦淮河，中国(长江)下游右岸{支流。古称龙}藏浦，汉代<起>称淮水，唐以后改/称《秦淮》。 [1]  秦淮河有南北两源，北源句容河发源于句容市宝华山南麓，南源溧水河发源于南京市溧水区东庐山，两河在南京市江宁区方山埭西北村汇合成秦淮河干流，绕过方山向西北至外城城门上坊门从东水关流入南京城，由东向西横贯市区，南部从西水关流出，注入长江。";
        String[] texts = text.split("、|，|。|；|？|！|,|\\.|;|\\?|!|]|}|\\{|\\[|}|\\{|<|>|《|》|-|_|=|\\+|\\)|\\(|\\.|/");
        JSONArray tokenizers = new JSONArray();
        for (String line:texts ) {
            tokenizers.add(line);
        }
        System.out.println(tokenizers);

    }
}
