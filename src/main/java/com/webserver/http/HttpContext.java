package com.webserver.http;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LeafDust
 * @create 2019-08-23 14:28
 */
public class HttpContext {
    private static Map<String, String> mimeMapping = new HashMap<>();

    static {
        initMapping();
    }

    //初始化
    private static void initMapping() {
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read("./conf/web.xml");
            Element root = document.getRootElement();
            List<Element> mimes = root.elements("mime-mapping");
            for (Element e : mimes) {
                mimeMapping.put(e.elementTextTrim("extension"), e.elementTextTrim("mime-type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getMimeType(String key) {
        return mimeMapping.get(key);
    }
}
