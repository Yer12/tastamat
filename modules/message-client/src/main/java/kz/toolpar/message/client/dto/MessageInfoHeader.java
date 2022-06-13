package kz.toolpar.message.client.dto;

public class MessageInfoHeader {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public MessageInfoHeader setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public MessageInfoHeader setValue(String value) {
        this.value = value;
        return this;
    }
}
