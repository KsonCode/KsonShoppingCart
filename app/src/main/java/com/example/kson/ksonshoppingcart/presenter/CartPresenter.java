package com.example.kson.ksonshoppingcart.presenter;

import com.example.kson.ksonshoppingcart.contract.CartContract;
import com.example.kson.ksonshoppingcart.entity.CartBean;
import com.example.kson.ksonshoppingcart.model.CartModel;
import com.example.kson.ksonshoppingcart.model.ICartmodelCallback;
import com.google.gson.Gson;

import java.util.HashMap;

public class CartPresenter extends CartContract.CartPresenter {
    private CartModel cartModel;

    public CartPresenter(CartContract.ICartView iCartView) {
        cartModel = new CartModel();
        this.iCartView = iCartView;
    }

    private CartContract.ICartView iCartView;



    @Override
    public void getCarts(HashMap<String, String> params) {

        cartModel.getCarts(params, new ICartmodelCallback() {
            @Override
            public void success(String result) {

                CartBean cartBean = new Gson().fromJson(result,CartBean.class);

                iCartView.success(cartBean.data);
            }

            @Override
            public void failure(String msg) {

                iCartView.failure(msg);

            }
        });

    }
}
