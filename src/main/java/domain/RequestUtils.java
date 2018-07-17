package domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestUtils {
    private static final Logger logger =  LoggerFactory.getLogger(RequestUtils.class);

    private String url;
    private String [] requestArr;

    public RequestUtils(String readLine) {
        this.requestArr = readLine.split(" ");
    }

    public void findFile() {
       for (String line : getRequestArr())
           System.out.println("line info : " + line);
    }

    public String getParameter() {
        if (getUrl().contains("?")) {
            String parameter = getUrl().substring(getUrl().indexOf("?")+1);
            logger.info("parameter info : {}", parameter);
            return parameter;
        }
        return getUrl();
    }

    public String getUrl() {
        return requestArr[1];
    }

    public String[] getRequestArr() {
        return requestArr;
    }
}
