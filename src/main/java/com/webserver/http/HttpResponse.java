package com.webserver.http;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author LeafDust
 * @create 2019-08-23 14:27
 */
public class HttpResponse {
    private Socket socket;
    private OutputStream out;
    //状态行相关信息
    private int statusCode = 200;
    private String statusReason = "ok";
    //响应头相关信息
    private Map<String, String> responseHeader = new HashMap<>();
    private File entity;

    public HttpResponse(Socket socket) {
        try {
            this.socket = socket;
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //设置响应状态
    public void setResponseStatus(int statusCode, String statusReason) {
        this.statusCode = statusCode;
        this.statusReason = statusReason;
    }

    //设置文件
    public void setFile(File file) {
        //获取文件后缀
        entity=file;
        String ext = entity.getName().substring(entity.getName().lastIndexOf(".") + 1).toLowerCase();
        putResponseHeader("Content-Type", HttpContext.getMimeType(ext));
        putResponseHeader("Content-Length", entity.length() + "");
    }

    //添加响应头数据
    private void putResponseHeader(String key, String value) {
        responseHeader.put(key, value);
    }

    //写操作
    private void writeOut(String string) {
        try {
            out.write(string.getBytes("ISO-8859-1"));
            out.write(13);
            out.write(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //响应
    public void response() {
        respondStatus();
        respondHeader();
        respondContent();
    }

    //响应状态行
    private void respondStatus() {
        writeOut("HTTP/1.1" + " " + statusCode + " " + statusReason);
    }

    //响应响应头
    private void respondHeader() {
        Set<Map.Entry<String, String>> entries = responseHeader.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            writeOut(entry.getKey() + ": " + entry.getValue());
        }
        try {
            out.write(13);
            out.write(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //响应响应正文
    private void respondContent() {
        byte[] data = new byte[10 * 1024];
        try (FileInputStream fis = new FileInputStream(entity);) {
            int len = -1;
            while ((len = fis.read(data)) != -1) {
                out.write(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
