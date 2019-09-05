package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;
import com.webserver.http.HttpServlet;

import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * @author LeafDust
 * @create 2019-08-23 15:49
 */
public class RegServlet extends HttpServlet {
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        String[] regInfo = {httpRequest.getParameters("username"), httpRequest.getParameters("nickname"),
                httpRequest.getParameters("password"), httpRequest.getParameters("age")};

        if (!(regInfo[0] == null || regInfo[1] == null || regInfo[2] == null || regInfo[3] == null)) {
            try (RandomAccessFile raf = new RandomAccessFile("./user.dat", "rw")) {
                byte[] data = new byte[32];
                for (int i = 0; i < raf.length() / 128; i++) {
                    raf.seek(i * 128);
                    raf.read(data);
                    if (new String(data, "UTF-8").trim().equals(regInfo[0])) {
                        skipWeb("have_user.html", httpResponse);
                        return;
                    }
                }
                raf.seek(raf.length());
                for (String s : regInfo) {
                    raf.write(Arrays.copyOf(s.getBytes("UTF-8"), 32));
                }
                skipWeb("reg_success.html", httpResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            skipWeb("reg_fail.html", httpResponse);
        }

    }
}
