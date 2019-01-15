package com.example.kson.ksonshoppingcart.contract;

import com.example.kson.ksonshoppingcart.entity.CartBean;
import com.example.kson.ksonshoppingcart.model.ICartmodelCallback;

import java.util.HashMap;
import java.util.List;

public interface CartContract {

    public abstract class CartPresenter {

        public abstract void getCarts(HashMap<String, String> params);

    }

    interface ICartModel {

        void getCarts(HashMap<String, String> params, ICartmodelCallback callback);


    }

    interface ICartView {

        void success(List<CartBean.Cart> list);

        void failure(String msg);

    }
}
