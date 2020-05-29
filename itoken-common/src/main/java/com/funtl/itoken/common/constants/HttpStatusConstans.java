package com.funtl.itoken.common.constants;

public enum HttpStatusConstans {
    Bad_GATEWAY(502, "从上游服务器无法接受到响应");
    private int status;
    private String content;

    private HttpStatusConstans(int status, String content){
        this.status = status;
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }
}
