package com.wu.basic.dynamic.controller;

import com.wu.basic.dynamic.conf.DynamicConfig;

import com.wu.framework.inner.layer.web.EasyController;
import com.wu.framework.inner.lazy.database.expand.database.persistence.LazyOperation;
import com.wu.framework.inner.lazy.database.expand.database.persistence.map.EasyHashMap;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final LazyOperation lazyOperation;

    public ICiBaController(RestTemplate restTemplate, DynamicConfig dynamicConfig, LazyOperation lazyOperation) {
        this.restTemplate = restTemplate;
        this.dynamicConfig = dynamicConfig;
        this.lazyOperation = lazyOperation;
    }


    /**
     * 爱词霸 每日一句
     *
     * @param
     * @return
     * @author 吴佳伟
     * @date 2021/1/7 10:10 下午
     **/
    @Scheduled(cron = "0 0 0 * * ?")
    @GetMapping()
    public EasyHashMap daySentence() {
        final DynamicConfig.ICiBa iCiBa = dynamicConfig.getICiBa();
        EasyHashMap easyHashMap = restTemplate.getForObject(iCiBa.getUrl(), EasyHashMap.class);
        final EasyHashMap<Object, Object> objectObjectEasyHashMap = new EasyHashMap<>("i_ci_ba_day_sentence");
        objectObjectEasyHashMap.putAll(easyHashMap);
        lazyOperation.insert(objectObjectEasyHashMap);
        return objectObjectEasyHashMap;
    }
}
