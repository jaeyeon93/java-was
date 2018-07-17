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
            log.info("url : {}", arr[1]);
            if (("/user/create").equals(arr[1])) {
                Map<String, String> paramas = HttpRequestUtils.parseQueryString(IOUtils.readData(br, contentLength));
                User user = new User(paramas.get("userId"), paramas.get("password"), paramas.get("name"), paramas.get("email"));
                log.info("user info on RequestHeader : {}", user.toString());
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp/" + arr[1]).toPath());
            log.info("body : {}", body);
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
