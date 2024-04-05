package com.yh.ssc.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: ssc
 * @description:
 * @author: yehang
 * @create: 2024-04-05 13:46
 **/
@Component
@ConfigurationProperties(prefix = "platform")
public class PlatformConfig {
    private String auth;
    
    public String getAuth() {
        return auth;
    }
    
    public void setAuth(String auth) {
        this.auth = auth;
    }
}
