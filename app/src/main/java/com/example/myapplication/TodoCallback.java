package com.example.myapplication;

public interface TodoCallback<T> {

    void onSuccess(T result);

    void onFail();
}