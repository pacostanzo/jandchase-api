package com.jandprocu.jandchase.api.productsms.model;

import java.util.Date;
import java.util.List;

public class ErrorMessage {
    private Date timestamp;
    private List<String> message;
    private String details;

    public ErrorMessage(){}

    public ErrorMessage(Date timestamp, List<String> message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
