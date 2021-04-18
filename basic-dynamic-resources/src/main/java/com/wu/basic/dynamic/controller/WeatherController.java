package com.wu.basic.dynamic.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wu.basic.dynamic.conf.DynamicConfig;
import com.wu.framework.inner.layer.web.EasyController;
import com.wu.framework.inner.lazy.database.expand.database.persistence.LazyOperation;
import com.wu.framework.inner.lazy.database.expand.database.persistence.map.EasyHashMap;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : 吴佳伟
 * @version 1.0
 * @describe : 高的地图天气
 * @date : 2020/12/30 10:27 下午
 */
@EasyController
public class WeatherController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final DynamicConfig dynamicConfig;
    private final LazyOperation lazyOperation;
    private final String A_MAP_AREA_CODE_TABLE = "a_map_area_code";
    private final String A_MAP_WEATHER_TABLE = "a_map_weather";


    public WeatherController(DynamicConfig dynamicConfig, LazyOperation lazyOperation) {
        this.dynamicConfig = dynamicConfig;
        this.lazyOperation = lazyOperation;
    }


    //    @PostConstruct
    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    @PostMapping("/init")
    public void init() {
        String sql = "select adcode from %s where adcode is not null ";
        final List<String> stringList = lazyOperation.executeSQL(String.format(sql, A_MAP_AREA_CODE_TABLE), String.class);
        stringList.forEach(s -> {
            final EasyHashMap easyHashMap = city(s);
            if (null != easyHashMap) {
                easyHashMap.put("hour", LocalDateTime.now().getHour());
                easyHashMap.put("date", LocalDate.now());
                easyHashMap.setUniqueLabel(A_MAP_WEATHER_TABLE);
                System.out.println(easyHashMap);
                lazyOperation.insert(easyHashMap);
            }
        });

    }


    /**
     * @param
     * @return
     * @describe 根据city编码获取 天气数据
     * @author Jia wei Wu
     * @date 2021/4/18 4:53 下午
     **/
    @GetMapping("/city")
    public EasyHashMap city(String cityCode) {
        final DynamicConfig.AMapWeatherProperties aMapWeatherProperties = dynamicConfig.getAMapWeatherProperties();
        final JSONObject json = restTemplate.getForObject("https://restapi.amap.com/v3/weather/weatherInfo?key={1}&city={2}&extensions={3}&output={4}",
                JSONObject.class,
                aMapWeatherProperties.getKey(),
                cityCode,
                aMapWeatherProperties.getExtensions(),
                aMapWeatherProperties.getOutput());
        assert json != null;
        try {
            final List<EasyHashMap> easyHashMapList = JSONArray.parseArray(json.getJSONArray("lives").toJSONString(), EasyHashMap.class);
            return easyHashMapList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
