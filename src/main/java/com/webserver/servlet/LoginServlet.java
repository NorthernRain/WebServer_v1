package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.http.HttpServlet;

import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * @author LeafDust
 * @create 2019-08-23 16:28
 */
public class LoginServlet extends HttpServlet {
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        try (RandomAccessFile raf = new RandomAccessFile("./user.dat", "rw")) {
            byte[] data = new byte[32];
            for (int i = 0; i < raf.length() / 128; i++) {
                raf.seek(i * 128);
                raf.read(data);
                if (new String(data, "UTF-8").trim().equals(httpRequest.getParameters("username"))) {
                    raf.seek(raf.getFilePointer() + 32);
                    raf.read(data);
                    if (new String(data, "UTF-8").trim().equals(httpRequest.getParameters("password"))) {
                        skipWeb("login_success.html", httpResponse);
                        return;
                    } else {
                        skipWeb("password_error.html", httpResponse);
                        return;
                    }
                }
            }
            skipWeb("username_error.html", httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
