package com.example.myapplication.interfaces;

import com.example.myapplication.appmessages.ErrorMessage;

public interface TodoCallback<T> {

    void onSuccess(T result);

    void onFail(ErrorMessage errorMessage);
}