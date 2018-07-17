package util;

import java.io.BufferedReader;
import java.io.StringReader;

import domain.RequestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);

    @Test
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
    }

    @Test
    public void dataSplit() throws Exception {
        RequestUtils requestUtils = new RequestUtils("GET /index.html HTTP/1.1");
        requestUtils.findFile();
        System.out.println(requestUtils.getUrl());
    }
}
