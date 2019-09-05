package com.webserver.core;

import com.webserver.exception.NullRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.http.HttpServlet;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * @author LeafDust
 * @create 2019-08-23 9:59
 */
public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            HttpRequest httpRequest = new HttpRequest(socket);
            HttpResponse httpResponse = new HttpResponse(socket);
            String path = httpRequest.getStringURI();
            File file = new File("./webapps" + path);
            if (ServletContext.servletMapping.containsKey(path)) {
                HttpServlet httpServlet = ServletContext.getHttpServlet(path);
                httpServlet.service(httpRequest, httpResponse);
            } else {
                if (file.exists()) {
                    httpResponse.setFile(file);
                } else {
                    file = new File("./webapps/myweb/404.html");
                    httpResponse.setResponseStatus(404, "NOT FOUND!");
                    httpResponse.setFile(file);
                }
            }
            httpResponse.response();
        } catch (NullRequestException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
