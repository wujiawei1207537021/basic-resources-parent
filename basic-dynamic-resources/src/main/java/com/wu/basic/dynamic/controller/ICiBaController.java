package com.wu.basic.dynamic.controller;

import com.wu.basic.dynamic.conf.DynamicConfig;
import com.wu.framework.easy.stereotype.upsert.dynamic.QuickEasyUpsert;
import com.wu.framework.easy.stereotype.upsert.entity.EasyHashMap;
import com.wu.framework.easy.stereotype.upsert.enums.EasyUpsertType;
import com.wu.framework.easy.stereotype.web.EasyController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

/**
 * @author : 吴佳伟
 * @version 1.0
 * @describe : 爱词霸
 * @date : 2021/1/7 10:09 下午
 */
@EasyController("/ICiBa")
public class ICiBaController {
    private final RestTemplate restTemplate;
    private final DynamicConfig dynamicConfig;

    public ICiBaController(RestTemplate restTemplate, DynamicConfig dynamicConfig) {
        this.restTemplate = restTemplate;
        this.dynamicConfig = dynamicConfig;
    }


    /**
     * 爱词霸 每日一句
     *
     * @param
     * @return
     * @author 吴佳伟
     * @date 2021/1/7 10:10 下午
     **/
    @QuickEasyUpsert(type = EasyUpsertType.MySQL)
    @GetMapping()
    public EasyHashMap daySentence() {
        final DynamicConfig.ICiBa iCiBa = dynamicConfig.getICiBa();
        EasyHashMap easyHashMap = restTemplate.getForObject(iCiBa.getUrl(), EasyHashMap.class);
        final EasyHashMap<Object, Object> objectObjectEasyHashMap = new EasyHashMap<>("i_ci_ba_day_sentence");
        objectObjectEasyHashMap.putAll(easyHashMap);
        return objectObjectEasyHashMap;
    }
}
