package com.wu.basic.dynamic.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wu.basic.dynamic.conf.DynamicConfig;
import com.wu.framework.easy.excel.util.FastExcelImp;
import com.wu.framework.easy.stereotype.upsert.component.IUpsert;
import com.wu.framework.easy.stereotype.upsert.dynamic.QuickEasyUpsert;
import com.wu.framework.easy.stereotype.upsert.entity.EasyHashMap;
import com.wu.framework.easy.stereotype.upsert.enums.EasyUpsertType;
import com.wu.framework.easy.stereotype.web.EasyController;
import com.wu.framework.inner.lazy.database.expand.database.persistence.LazyOperation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
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
    private final IUpsert iUpsert;

    public WeatherController(DynamicConfig dynamicConfig, LazyOperation lazyOperation, IUpsert iUpsert) {
        this.dynamicConfig = dynamicConfig;
        this.lazyOperation = lazyOperation;
        this.iUpsert = iUpsert;
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
                iUpsert.upsert(easyHashMap);
            }
        });

    }

    @QuickEasyUpsert(type = EasyUpsertType.MySQL)
    @GetMapping("/city")
    public EasyHashMap city(String cityCode) {
        final DynamicConfig.AMapWeatherProperties aMapWeatherProperties = dynamicConfig.getAMapWeatherProperties();
        final JSONObject json = restTemplate.getForObject("https://restapi.amap.com/v3/weather/weatherInfo?key={1}&city={2}&extensions={3}&output={4}",
                JSONObject.class,
                aMapWeatherProperties.getKey(),
                cityCode,
                aMapWeatherProperties.getExtensions(),
                aMapWeatherProperties.getOutput());
        EasyHashMap easyHashMap = new EasyHashMap(A_MAP_WEATHER_TABLE);
        System.out.println(json);
        assert json != null;
        try {
            final List<EasyHashMap> easyHashMapList = JSONArray.parseArray(json.getJSONArray("lives").toJSONString(), EasyHashMap.class);
            easyHashMap.putAll(easyHashMapList.get(0));
            return easyHashMap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return
     * @params 保存区域code
     * @author 吴佳伟
     * @date 2021/1/3 11:57 上午
     **/
    @QuickEasyUpsert(type = EasyUpsertType.MySQL)
    @PostMapping()
    public List saveAreaCode(MultipartFile file) {
        List<EasyHashMap> easyHashMapList = FastExcelImp.parseExcel(file, EasyHashMap.class);
        if (ObjectUtils.isEmpty(easyHashMapList)) {
            return easyHashMapList;
        }
        EasyHashMap firstEasyHashMap = new EasyHashMap(A_MAP_AREA_CODE_TABLE);
        firstEasyHashMap.putAll(easyHashMapList.get(0));
        easyHashMapList.remove(0);
        easyHashMapList.add(0, firstEasyHashMap);
        return easyHashMapList;
    }

}
