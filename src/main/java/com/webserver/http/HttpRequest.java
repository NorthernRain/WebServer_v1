package com.webserver.http;

import com.webserver.exception.NullRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LeafDust
 * @create 2019-08-23 10:01
 */
public class HttpRequest {
    private Socket socket;
    private InputStream in;
    //请求行相关信息
    private String method;
    private String uri;
    private String protocol;
    private String stringURI;
    private String queryString;
    private Map<String, String> parameter = new HashMap<>();
    //消息头相关信息
    private Map<String, String> headers = new HashMap<>();


    public HttpRequest(Socket socket) throws NullRequestException {
        this.socket = socket;
        try {
            in = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseRequest();
        parseHeader();
        parseContent();
    }

    //解析请求行
    private void parseRequest() throws NullRequestException {
        String line = readLine();
        if ("".equals(line)) {
            throw new NullRequestException("空请求！");
        }
        String[] request = line.split("\\s");
        method = request[0].trim();
        uri = request[1].trim();
        protocol = request[2].trim();
        parseURI();
        System.out.println("method:" + method);
        System.out.println("uri:" + uri);
        System.out.println("protocol:" + protocol);
    }

    //二次解析
    private void parseURI() {
        int d = uri.indexOf("?");
        if (d != -1) {
            stringURI = uri.substring(0, d);
            try {
                queryString = URLDecoder.decode(uri.substring(d + 1), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            putParameter();
            System.out.println(parameter);
        } else {
            stringURI = uri;
        }
    }

    //解析消息头
    private void parseHeader() {
        String line;
        while (!"".equals(line = readLine())) {
            String[] herdersLine = line.split(":\\s");
            if (herdersLine.length > 1)
                headers.put(herdersLine[0], herdersLine[1]);
            else headers.put(herdersLine[0], null);
        }
        System.out.println(headers);
    }

    //解析消息正文
    private void parseContent() {
        if (headers.containsKey("Content-Length")) {
            int length = Integer.parseInt(headers.get("Content-Length"));
            if ("application/x-www-form-urlencoded".equals(headers.get("Content-Type"))) {
                try {
                    byte[] data = new byte[length];
                    in.read(data);
                    queryString = URLDecoder.decode(new String(data, "UTF-8"), "UTF-8");
                    putParameter();
                    System.out.println("parameter"+parameter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //读取客户端的请求
    private String readLine() {
        StringBuilder builder = new StringBuilder();
        try {
            char c = 'a';
            int d = -1;
            while ((d = in.read()) != -1) {
                if (c == 13 && (char) d == 10) {
                    break;
                }
                c = (char) d;
                builder.append(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString().trim();
    }

    //parameter添加元素的方法
    private void putParameter() {
        String[] paras = queryString.split("&");
        for (String s : paras) {
            String[] para = s.split("=");
            if (para.length > 1) {
                parameter.put(para[0], para[1]);
            } else {
                parameter.put(para[0], null);
            }
        }
    }

    //get方法
    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getStringURI() {
        return stringURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getHeaders(String key) {
        return headers.get(key);
    }
    public String getParameters(String key) {
        return parameter.get(key);
    }
}
