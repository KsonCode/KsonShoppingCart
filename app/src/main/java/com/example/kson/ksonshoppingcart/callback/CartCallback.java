package com.example.kson.ksonshoppingcart.callback;

public interface CartCallback {

    void notifyCartItem(boolean isChecked ,int postion);
    void notifyNum();
}
