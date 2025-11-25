package com.spendshare.spendshare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean sucess;
    private String message;
    private T data;

    public ApiResponse(boolean success, String message){
        this.sucess = success;
        this.message = message;
        this.data = null;
    }
}
