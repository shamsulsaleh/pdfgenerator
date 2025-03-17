package com.shamsulsaleh.pdfgenerator.dto;

public class BasicRespDto {
    private int code;
    private String status;
    private String msg;

    public BasicRespDto(int code, String status, String msg) {
        this.code = code;
        this.status = status;
        this.msg = msg;
    }

    // Getters and Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}