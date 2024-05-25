package com.openbook.openbook.global.dto;

public record ResponseMessage(
        String message
) {
    public String toWrite() {
        return "{" +
                "\"message\":" + "\"" + message +"\"" +
                "}";
    }
}
