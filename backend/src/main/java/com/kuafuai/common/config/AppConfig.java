package com.kuafuai.common.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private static String profile;
    private static String version;
    private static String channel;

    public void setProfile(String profile) {
        AppConfig.profile = profile;
    }

    public void setVersion(String version) {
        AppConfig.version = version;
    }

    public void setChannel(String channel) {
        AppConfig.channel = channel;
    }

    public static String getProfile() {
        return profile;
    }

    public static String getVersion() {
        return version;
    }

    public static String getChannel() {
        return channel;
    }

    /**
     * 获取导入上传路径
     */
    public static String getImportPath() {
        return getProfile() + "/import";
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getProfile() + "/upload";
    }
}
