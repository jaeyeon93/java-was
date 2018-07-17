package util;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Map;

import domain.RequestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);
    private RequestUtils requestUtils;

    @Test
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
    }

    @Test
    public void dataSplit() throws Exception {
        requestUtils = new RequestUtils("GET /index.html HTTP/1.1");
        requestUtils.findFile();
        System.out.println(requestUtils.getUrl());
    }

    @Test
    public void parameterTest() throws Exception {
        requestUtils = new RequestUtils("GET /user/create?userId=jimmy&password=12345&name=KIM+JaeYeon&email=foodsksms%40gmail.com HTTP/1.1");
        String parameter = requestUtils.getParameter();
        logger.info("parameter test : {}", parameter);
    }

    @Test
    public void parseValueTest() throws Exception {
        requestUtils = new RequestUtils("GET /user/create?userId=jimmy&password=12345&name=JimmyKim&email=foodsksms@gmail.com HTTP/1.1");
        logger.info("url : {}", requestUtils.getUrl());
        logger.info("parse value : {}", HttpRequestUtils.parseQueryString(requestUtils.getParameter()));
        Map<String, String> parseResult = HttpRequestUtils.parseQueryString(requestUtils.getParameter());
        logger.info("parseResult : {}", parseResult);
        logger.info("getName : {}", parseResult.get("name"));
    }

    @Test
    public void NoparameterTest() throws Exception {
        requestUtils = new RequestUtils("GET /user/create HTTP/1.1");
        String parameter = requestUtils.getParameter();
        logger.info("parameter test : {}", parameter);
    }
}
