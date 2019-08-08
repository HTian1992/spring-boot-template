package com.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by lum on 2018/9/26
 */
public class JacksonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JacksonUtils.class);


    private final static XmlMapper XML_MAPPER = xmlMapperInit();

    private final static ObjectMapper OBJECT_MAPPER = jsonMapperInit();

    private static XmlMapper xmlMapperInit() {
        XmlMapper XML_MAPPER = new XmlMapper();
        XML_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return XML_MAPPER;
    }

    private static ObjectMapper jsonMapperInit(){
        ObjectMapper OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return OBJECT_MAPPER;
    }

    public static <T>T readXMLValue(String xml, Class<T> clz) {
        try {
            return XML_MAPPER.readValue(xml, clz);
        } catch (IOException e) {
            logger.error("jackson解析xml异常：{}" , e.getMessage());
            return null;
        }
    }

    public static <T>T readJsonValue(String msg, Class<T> clz) {
        try {
            return OBJECT_MAPPER.readValue(msg, clz);
        } catch (IOException e) {
            logger.error("jackson解析xml异常：{}" ,e.getMessage());
            return null;
        }
    }

    public static String writeAsJsonString(Object obj) {
        try {
            if (null != obj) {
                return OBJECT_MAPPER.writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            logger.error("jackson解析xml异常：{}", e);
        }
        return null;
    }

    public static <T>T readJsonValue(String msg, TypeReference valueTypeRef) {
        try {
            return OBJECT_MAPPER.readValue(msg, valueTypeRef);
        } catch (IOException e) {
            logger.error("jackson解析json异常：" + e.getMessage());
            return null;
        }
    }

    public static String logString(Object obj) {
        try {
            if (null == obj) {
                return "null";
            }
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("jackson解析json异常：" + e.getMessage());
        }
        return "";
    }

    public static ObjectMapper getObjectMapper(){
        return OBJECT_MAPPER;
    }
}
