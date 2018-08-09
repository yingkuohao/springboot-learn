package com.kuohao.learn.spring.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: chengjing
 * Date: 16/5/31
 * Time: 下午4:01
 * CopyRight: taobao
 * Descrption:
 */

public class ServiceSettings {
    private static final String SETTINGS_FILE_NAME = System.getProperty("user.home") + System.getProperty("file.separator") + ".lottery-retailer.properties";
    private static final Log log = LogFactory.getLog(ServiceSettings.class);
    private static Properties properties = new Properties();



    public static String getProducerId() {
        return properties.getProperty("lottery.retailer.producerId");
    }


    public static void load() {
        FileInputStream is = null;

        try {
            is = new FileInputStream(SETTINGS_FILE_NAME);
            properties.load(is);
        } catch (FileNotFoundException var12) {
            log.warn("The settings file \'" + SETTINGS_FILE_NAME + "\' does not exist.");
        } catch (IOException var13) {
            log.warn("Failed to load the settings from the file: " + SETTINGS_FILE_NAME);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException var11) {
                    ;
                }
            }

        }

    }

    public static void load(String configFile) {
        FileInputStream is = null;

        try {
            is = new FileInputStream(configFile);
            properties.load(is);
        } catch (FileNotFoundException var13) {
            log.warn("The settings file \'" + configFile + "\' does not exist.");
        } catch (IOException var14) {
            log.warn("Failed to load the settings from the file: " + configFile);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException var12) {
                    ;
                }
            }

        }

    }

    static {
        load();
    }
}
