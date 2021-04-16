package com.wu.basic.dynamic.controller;

import com.wu.framework.inner.layer.web.EasyController;
import com.wu.framework.inner.lazy.database.expand.database.persistence.LazyOperation;
import com.wu.framework.inner.lazy.database.expand.database.persistence.map.EasyHashMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @author : 吴佳伟
 * @version 1.0
 * @describe : 新华字典Controller
 * @date : 2021/2/28 1:27 下午
 */
@EasyController("/xinhua")
public class XinHuaDictionaryController implements CommandLineRunner {

    private final LazyOperation lazyOperation;
    private final RestTemplate restTemplate = new RestTemplateBuilder().build();

    public XinHuaDictionaryController(LazyOperation lazyOperation) {
        this.lazyOperation = lazyOperation;
    }

//        @PostConstruct
//    @PostMapping()
//    public void saveWord() {
//        String jsonStr = "";
//        try {
//            File file = ResourceUtils.getFile("classpath:static/data/word.json");
//            FileReader fileReader = new FileReader(file);
//            Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
//            int ch = 0;
//            StringBuffer sb = new StringBuffer();
//            while ((ch = reader.read()) != -1) {
//                sb.append((char) ch);
//            }
//            fileReader.close();
//            reader.close();
//            jsonStr = sb.toString();
//            jsonStr = jsonStr.replace("'", "’");
//            final List<EasyHashMap> easyHashMaps = JSON.parseArray(jsonStr, EasyHashMap.class);
//            easyHashMaps.get(0).setUniqueLabel("word");
//            lazyOperation.insert(easyHashMaps);
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//    }

    /**
     * @param
     * @return
     * @describe 百度文字转换成语音
     * @author Jia wei Wu
     * @date 2021/3/2 9:36 下午
     **/
    public File baiduTextToSpeech(String text) throws IOException {
        String url = "https://fanyi.baidu.com/gettts?lan=zh&text=%s&spd=5&source=wise";
        URL u = new URL(String.format(url, text));
        HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
        int statusCode = urlConnection.getResponseCode();
        if (statusCode != HttpURLConnection.HTTP_OK) {
            System.out.println("Http错误码：" + statusCode);
        }
        InputStream is = urlConnection.getInputStream();
        File tempFile = new File("temp" + File.separator + text + ".mp3");
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdir();
        }
        OutputStream out = new FileOutputStream(tempFile.getPath());
        int len = 0;
        byte[] b = new byte[2048];
        while ((len = is.read(b)) != -1) {
            out.write(b, 0, len);
        }
        return tempFile;
    }


    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        List<EasyHashMap> easyHashMapList = lazyOperation.executeSQL("select voice,strokes,update_time,pin_yin,radicals,create_time,more,old_word,id,explanation,word from word where voice is null limit 100 ", EasyHashMap.class);
        String url = "https://fanyi.baidu.com/gettts?lan=zh&text={1}&spd=5&source=wise";
        for (EasyHashMap easyHashMap : easyHashMapList) {
            String word = easyHashMap.get("word").toString();
            easyHashMap.put("voice", baiduTextToSpeech(word));
            easyHashMap.setUniqueLabel("word");
        }
//        lazyOperation.insert(easyHashMapList);
        if (easyHashMapList.size() == 100) run(args);
    }
}
