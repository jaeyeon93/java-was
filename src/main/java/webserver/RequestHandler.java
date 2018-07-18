package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Date;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import javax.rmi.CORBA.Util;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            log.info("first request info : {}", line);
            String [] arr = line.split(" ");
            int contentLength = 0;
            while (!line.equals("")) {
                line = br.readLine();
                log.info("request info : {}", line);
                if (line.contains("Content-Length"))
                    contentLength = getContentLength(line);
            }
            String url = arr[1];
            log.info("url : {}", url);

            if (("/user/create").equals(arr[1])) {
                Map<String, String> params = HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength));
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                DataBase.addUser(user);
                log.info("user info on RequestHeader : {}", user.toString());
                url = "/index.html";
                log.info("url : {}", url);
                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos, url);
            }

            if ("/user/login".equals(url)) {
                log.info("login Controller called");
                // 인자전달
                Map<String, String> params = HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength));
                // 유저조회
                User target = DataBase.findUserById(params.get("userId"));

                if (target == null || !target.getPassword().equals(params.get("password"))) {
                    //loginFail
                    url = "/user/login_failed.html";
                    log.info("loginFail called, url : {}", url);
                    DataOutputStream dos = new DataOutputStream(out);
                    response302Header(dos, url);
                }

                log.info("target User : {}", target.toString());
                if (target.getPassword().equals(params.get("password"))) {
                    log.info("login Success");
                    // Header설정 쿠키설정하기

                }

                // 로그인실패
                log.info("if문 통과못함");
            }



            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp/" + arr[1]).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public Integer getContentLength(String line) {
        return Integer.parseInt(line.split(":")[1].trim());
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            log.info("302 redirect success");
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
