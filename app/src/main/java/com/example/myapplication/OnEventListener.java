package com.example.myapplication;

public interface OnEventListener<String> {
    void onSuccess(String[] object);
    void onFailure(Exception e);
}
