package com.pwojcik;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Response {
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final Map<Integer, String> TEXT_ERROR_MAP = Map.of(200, "OK", 404, "Not Found", 500, "Server error");
    private static final String RESPONSE_STRING_TEMPLATE = """      
            HTTP/1.1 %d %s
            Date: %s
            Server: httpserv
            Content-Type: text/html
            Content-Length: %d
                       
            %s""";
    private final String data;
    private final int statusCode;

    public Response(int statusCode, String data) {
        this.data = data;
        this.statusCode = statusCode;
    }


    public String asString() {
        String response = String.format(RESPONSE_STRING_TEMPLATE, statusCode, TEXT_ERROR_MAP.get(statusCode), getCurrentDateFormatted(), data.length(), data);
        System.out.printf("Response:\n %s", response);
        return response;
    }

    private String getCurrentDateFormatted() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.format(new Date());
    }

}
