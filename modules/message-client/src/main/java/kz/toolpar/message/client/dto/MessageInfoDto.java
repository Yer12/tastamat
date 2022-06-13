package kz.toolpar.message.client.dto;

import java.util.ArrayList;
import java.util.List;

public class MessageInfoDto {

    private String url;
    private String request;
    private List<MessageInfoHeader> headers = new ArrayList<>();

    public MessageInfoDto() {
    }

    public String getUrl() {
        return url;
    }

    public MessageInfoDto setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getRequest() {
        return request;
    }

    public MessageInfoDto setRequest(String request) {
        this.request = request;
        return this;
    }

    public List<MessageInfoHeader> getHeaders() {
        return headers;
    }

    public MessageInfoDto setHeaders(List<MessageInfoHeader> headers) {
        this.headers = headers;
        return this;
    }
}
