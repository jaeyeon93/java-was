package domain;

public class RequestUtils {
    private String url;
    private String [] requestArr;

    public RequestUtils(String readLine) {
        this.requestArr = readLine.split(" ");
    }

    public void findFile() {
       for (String line : getRequestArr())
           System.out.println("line info : " + line);
    }

    public String getUrl() {
        return requestArr[1];
    }

    public String[] getRequestArr() {
        return requestArr;
    }
}
