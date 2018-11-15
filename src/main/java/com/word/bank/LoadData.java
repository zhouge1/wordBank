package com.word.bank;

import com.alibaba.fastjson.JSONArray;
import utils.Sogou;

public class LoadData {
    public static void main(String[] args) {
        try {
            WordBankService.createWordBank();
            JSONArray datas = Sogou.getData();
            WordBankService.addDataToES(datas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
