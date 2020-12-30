package com.wu.basic.dynamic.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : 吴佳伟
 * @version 1.0
 * @describe :
 * @date : 2020/12/30 10:40 下午
 */
@Data
@ConfigurationProperties(prefix = "dynamic")
public class DynamicConfig {

    private String weatherUrl;

}
