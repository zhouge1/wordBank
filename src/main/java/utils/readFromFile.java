package utils;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import wordBank.addDocument;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class readFromFile {
    public static void main(String[] args) {
        System.out.println("------------------");
        //System.out.println(readJson("/words.json"));
        //System.out.println(readFile("/wordBankFile/start"));
        //System.out.println(getFiles("/wordBankFile"));
        String directory = "/conf/wordBankFile";
        List<String> list = getFiles("/conf/wordBankFile");
        System.out.println(getDataFromDirectory(directory, list));
    }

    /**
     * Get data from file
     *
     * @param fileName
     * @return
     */
    public static JSONArray readJson(String fileName) {
        //读取json配置文件
        try {
            InputStream wordsStream = addDocument.class.getResourceAsStream(fileName);
            String wordsString = IOUtils.toString(wordsStream, "utf-8");
            JSONArray words = JSONArray.parseArray(wordsString);
            return words;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get data from file
     *
     * @param fileName
     * @return
     */
    public static JSONArray readFile(JSONArray lines, String fileName) {
        //读取json配置文件
        try {
            InputStream wordsStream = addDocument.class.getResourceAsStream(fileName);
            LineIterator lineIterator = IOUtils.lineIterator(wordsStream, "utf-8");
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine().trim();
                if (!lines.contains(line)) {
                    lines.add(line);
                }
            }
            return lines;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get fileNames from a directory
     *
     * @param path
     * @return
     */
    public static ArrayList<String> getFiles(String path) {
        URL url = addDocument.class.getResource(path);
        System.out.println(url.toString());
        System.out.println(url.toExternalForm());
        ArrayList<String> files = new ArrayList<String>();
        File file = new File(url.toString().split("file:/")[1]);
        File[] sunFileList = file.listFiles();
        for (int i = 0; i < sunFileList.length; i++) {
            if (sunFileList[i].isFile()) {
                files.add(sunFileList[i].getName());
            }
        }
        return files;
    }

    /**
     * get data from a directory
     *
     * @param directory
     * @param list
     * @return
     */
    public static JSONArray getDataFromDirectory(String directory, List<String> list) {
        Iterator iterator = list.iterator();
        JSONArray result = new JSONArray();
        while (iterator.hasNext()) {
            String fileName = iterator.next().toString();
            result = readFile(result, directory + "/" + fileName);
        }
        return result;
    }
}
