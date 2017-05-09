package ex03.pymont;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import ex03.pyrmont.connector.http.SocketInputStream

import javax.servlet.ServletException;

/**
 * Created by nanca on 5/9/2017.
 */
public class HttpProcessor {

    public void process(Socket socket) {
        SocketInputStream input = null;
        OutputStream output = null;
        try {
            input = new SocketInputStream(socket.getInputStream(), 2048);
            output = socket.getOutputStream();

            // create HttpRequest object and parse
            request = new httpRequest(input);

            // create HttpResponse objec
            response = new HttpResponse(output);
            response.setRequest(request);

            response.setHeader("Server", "Pyrmont Servlet Container");

            parseRequest(input, output);
            parseHeaders(input);

            // check if this is a request for a servlet or a static resource
            // a request for a servlet begins wit "/servlet/"
            if (request.getRequestURI().startWith("/servlet/")) {
                ServletProcessor processor = new ServletProcessor();
                processor.process(request, response);
            }
            else {
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(request, response);
            }

            //close the socket
            socket.close();
            // no shutdown for this application

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseRequest(SocketInputStream input, OutputStream output)
        throws IOException, ServletException {

        // Parse the incoming request line
        input.readRequestLine(requestLine);
        String method = new String(requestLine.methos, 0, requstLine.protocolEnd);

        //  Validate the incoming request line
        if (method.length() < 1) {
            throw new ServletException("Missing HTTP request methos");
        } else if (request.uriEnd < 1) {
            throw new ServletException("Missing HTTP request URI");
        }
    }
}


































