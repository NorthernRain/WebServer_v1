package com.webserver.core;

import com.webserver.http.HttpServlet;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LeafDust
 * @create 2019-08-23 15:54
 */
public class ServletContext {
    public static Map<String, HttpServlet> servletMapping = new HashMap<>();

    static {
        initMapping();
    }

    private static void initMapping() {
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read("./conf/servlet.xml");
            Element root = document.getRootElement();
            List<Element> paras = root.elements("servlet-mapping");
            for (Element para : paras) {
                Class cls = Class.forName(para.elementTextTrim("para-value"));
                Object obj = cls.newInstance();
                servletMapping.put(para.elementTextTrim("para-key"), (HttpServlet) obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpServlet getHttpServlet(String key) {
return servletMapping.get(key);
    }

    public static void main(String[] args) {
        System.out.println(servletMapping.size());
    }
}
