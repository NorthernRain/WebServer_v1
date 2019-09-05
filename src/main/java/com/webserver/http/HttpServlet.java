package com.webserver.http;

import java.io.File;

/**
 * @author LeafDust
 * @create 2019-08-23 15:50
 */
public abstract class HttpServlet {
    public abstract void service(HttpRequest httpRequest, HttpResponse httpResponse);

    public void skipWeb(String fileName, HttpResponse httpResponse) {
        File file = new File("./webapps/myweb/" + fileName);
        httpResponse.setFile(file);
    }
}
