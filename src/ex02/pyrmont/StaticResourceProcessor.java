package ex02.pyrmont;

import java.io.IOException;

/**
 * Created by nanca on 5/8/2017.
 */
public class StaticResourceProcessor {

    public void process(Request request, Response response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
