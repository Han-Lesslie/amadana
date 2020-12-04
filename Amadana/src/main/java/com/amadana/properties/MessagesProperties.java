package com.amadana.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="message")
@PropertySource("classpath:files-message.properties")
@Data
public class MessagesProperties {
    //压缩大小
    private long fileSize;
    // 压缩比例
    private double scaleRatio;
    //上传路径
    private String uploadPath;
    //图片类型
    private String imgType;

}
