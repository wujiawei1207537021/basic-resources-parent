package com.wu.basic.dynamic.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : 吴佳伟
 * @version 1.0
 * @describe :
 * @date : 2020/12/30 10:40 下午
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "dynamic")
public class DynamicConfig {

    private AMapWeatherProperties aMapWeatherProperties;


    /**
     * @describe: 高德地图天气配置
     * @author : 吴佳伟
     * @date : 2021/1/2 5:05 下午
     * @version : 1.0
     */
    @Data
    public static class AMapWeatherProperties {

        private String url="https://restapi.amap.com/v3/weather/weatherInfo?key={1}&city={2}&extensions={3}&output={4}";
        /**
         * 请求服务权限标识
         */
        private String key;
        /**
         * 城市编码
         */
        private String city="110101";
        /**
         * 气象类型
         *
         * 可选值：base/all
         *
         * base:返回实况天气
         *
         * all:返回预报天气
         */
        private String extensions="base";

        /**
         * 可选值：JSON,XML
         */
        private String output="JSON";
    }


}
