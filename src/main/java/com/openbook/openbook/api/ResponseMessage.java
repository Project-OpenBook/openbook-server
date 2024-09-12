package com.openbook.openbook.api;

public record ResponseMessage(
        String message
) {
    public String toWrite() {
        return "{" +
                "\"message\":" + "\"" + message +"\"" +
                "}";
    }
}
