package com.wu.basic.dynamic.controller;

import com.alibaba.fastjson.JSON;
import com.wu.framework.easy.stereotype.upsert.component.IUpsert;
import com.wu.framework.easy.stereotype.upsert.entity.EasyHashMap;
import com.wu.framework.easy.stereotype.web.EasyController;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author : 吴佳伟
 * @version 1.0
 * @describe : 新华字典Controller
 * @date : 2021/2/28 1:27 下午
 */
@EasyController("/xinhua")
public class XinHuaDictionaryController {
    private final IUpsert iUpsert;

    public XinHuaDictionaryController(IUpsert iUpsert) {
        this.iUpsert = iUpsert;
    }

    @PostConstruct
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


}
