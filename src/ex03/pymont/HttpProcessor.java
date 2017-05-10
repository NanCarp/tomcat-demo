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
        String method = new String(requestLine.methos, 0, requestLine.protocolEnd);
        String uri = null;
        String protocol = new String(requestLine.protocol, 0, requestLine.protocolEnd);

        //  Validate the incoming request line
        if (method.length() < 1) {
            throw new ServletException("Missing HTTP request methos");
        } else if (request.uriEnd < 1) {
            throw new ServletException("Missing HTTP request URI");
        }

        // Parse any query parameters out of the request URI
        int question = requestLine.indexOf("?");
        if (question >= 0) {
            request.setQueryString(new String(requestLine.uri, question + 1),
                    requestLine.uriEnd - question -1);
            uri = new String(requestLine.uri, 0, question);

        }
        else {
            request.setQueryString(null);
            uri = new String(requestLine.uri, 0, question);
        }

        // Checking for an absolute URI (with the HTTP protocol)
        if (!uri.startsWith("/")) {
            int pos = uri.indexOf("://");
            // Parsing out protocoal and host name
            if (pos != -1) {
                pos = uri.indexOf("/", pos + 3);
                if (pos == -1) {
                    uri = "";
                }
                else {
                    uri = uri.substring(pos);
                }
            }
        }

        // Parse any requested session ID out of the request URI
        String match = ";jsessionid=";
        int semicolon = uri.indexOf(match);
        if (semicolon >= 0) {
            String rest = uri.substring(semicolon + match.length());
            int semicolon2 = rest.indexOf(';');
            if (semicolon2 >= 0) {
                request.setRequestedSessionId(rest.substring(0), semicolon2);
                rest = rest.substring(semicolon2);
            }
            else {
                request.setRequestedSessionId(rest);
                rest = "";
            }
            request.setRequestedSessionURL(true);
            uri = uri.substring(0, semicolon) + rest;
        }
        else{
            request.setRequestSessionId(null);
            request.setRequestedSessionURL(flase);
        }

        // Normalize URI (using String operations at the moment)
        String nomalizedUri = normalize(uri);
        // Set the corresponding request properties
        ((HttpRequest) request).setMethod(method);
        request.setProtocol(protocol);
        if (normalizedUri != null) {
            ((HttpRequest) request).setRequestURI(normalizedUri);
        }
        else {
            ((HttpRequest) reques).setRequestURI(uri);
        }
        if (normalizeUri == null) {
            throw new ServletException("Invalid URI: " + uri + "'");
        }
    }
}


































