package com.wu.basic.dynamic.controller;

import com.alibaba.fastjson.JSON;
import com.wu.framework.easy.stereotype.upsert.component.IUpsert;
import com.wu.framework.easy.stereotype.upsert.entity.EasyHashMap;
import com.wu.framework.easy.stereotype.web.EasyController;
import com.wu.framework.inner.lazy.database.expand.database.persistence.LazyOperation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author : 吴佳伟
 * @version 1.0
 * @describe : 新华字典Controller
 * @date : 2021/2/28 1:27 下午
 */
@EasyController("/xinhua")
public class XinHuaDictionaryController implements CommandLineRunner {
    private final IUpsert iUpsert;
    private final LazyOperation lazyOperation;

    public XinHuaDictionaryController(IUpsert iUpsert, LazyOperation lazyOperation) {
        this.iUpsert = iUpsert;
        this.lazyOperation = lazyOperation;
    }

//    @PostConstruct
    @PostMapping()
    public void saveWord() {
        String jsonStr = "";
        try {
            File file = new File("/Users/wujiawei/IdeaProjects/chinese-xinhua/data/idiom.json");
            FileReader fileReader = new FileReader(file);
            Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            jsonStr = jsonStr.replace("'", "’");
            final List<EasyHashMap> easyHashMaps = JSON.parseArray(jsonStr, EasyHashMap.class);
            easyHashMaps.get(0).setUniqueLabel("idiom");
            iUpsert.upsert(easyHashMaps);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * @param
     * @return
     * @describe 百度文字转换成语音
     * @author Jia wei Wu
     * @date 2021/3/2 9:36 下午
     **/
    public InputStream baiduTextToSpeech(String text) throws IOException {
        String url = "https://fanyi.baidu.com/gettts?lan=zh&text=%s&spd=5&source=wise";
        URL u = new URL(String.format(url, text));
        HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
        int statusCode = urlConnection.getResponseCode();
        if (statusCode != HttpURLConnection.HTTP_OK) {
            System.out.println("Http错误码：" + statusCode);
        }
        InputStream is = urlConnection.getInputStream();
        return is;

    }


    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        List<EasyHashMap> easyHashMapList = lazyOperation.executeSQL("select word from word where voice is null limit 1000 ", EasyHashMap.class);
        for (EasyHashMap easyHashMap : easyHashMapList) {
            InputStream word = baiduTextToSpeech(easyHashMap.get("word").toString());
            easyHashMap.put("voice", word);
            easyHashMap.setUniqueLabel("word");
            iUpsert.upsert(easyHashMap);
        }
    }
}
