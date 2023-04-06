package com.example.myapplication;

import java.util.List;

public interface TodoCallback<T> {

    void onSuccess(T result);

    void onFail();
}
