package com.sy.shopping.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {
    private PropertiesUtils(){

    }

    public static Properties loadProperties(String fileName){
        Properties properties=new Properties();
        try {
            properties.load(PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
