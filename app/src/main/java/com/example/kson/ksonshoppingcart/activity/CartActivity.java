package com.example.kson.ksonshoppingcart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.kson.ksonshoppingcart.R;
import com.example.kson.ksonshoppingcart.adapter.CartAdapter;
import com.example.kson.ksonshoppingcart.callback.CartUICallback;
import com.example.kson.ksonshoppingcart.contract.CartContract;
import com.example.kson.ksonshoppingcart.entity.CartBean;
import com.example.kson.ksonshoppingcart.presenter.CartPresenter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartContract.ICartView,CartUICallback {
    private XRecyclerView xRecyclerView;

    private CheckBox checkBox;

    private CartPresenter cartPresenter;
    private CartAdapter cartAdapter;
    private List<CartBean.Cart> carts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();
        initData();
    }

    private void initData() {
        cartPresenter = new CartPresenter(this);
        carts = new ArrayList<>();

        cartPresenter.getCarts(new HashMap<String, String>());

    }


    private void initView() {
        xRecyclerView = findViewById(R.id.rv);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // TODO: 2019/1/15

                    for (CartBean.Cart cart : carts) {
                        cart.isChecked = true;
                        for (CartBean.Cart.Product product : cart.list) {
                            product.isProductChecked = true;
                        }
                    }

                } else {

                    for (CartBean.Cart cart : carts) {
                        cart.isChecked = false;
                        for (CartBean.Cart.Product product : cart.list) {
                            product.isProductChecked = false;
                        }
                    }
                }

                cartAdapter.notifyDataSetChanged();
                getTotalPrice();

            }
        });


    }

    @Override
    public void success(List<CartBean.Cart> list) {

        if (list != null) {

            carts = list;

            for (CartBean.Cart cart : carts) {
                for (CartBean.Cart.Product product : cart.list) {
                    product.productNum = 1;
                }
            }

            cartAdapter = new CartAdapter(this, carts);
            cartAdapter.setCartCallback(this);
            xRecyclerView.setAdapter(cartAdapter);
        }

    }

    @Override
    public void failure(String msg) {

    }

    /**
     * 获取总价
     */
    private void getTotalPrice() {
        double totalprice = 0;
        for (CartBean.Cart cart : carts) {

            for (CartBean.Cart.Product product : cart.list) {

                if (product.isProductChecked) {
                    System.out.println("num:"+product.productNum);
                    totalprice += product.price*product.productNum;
                }

            }
        }
        checkBox.setText("¥：" + totalprice);


    }

    @Override
    public void notifyCart() {
        getTotalPrice();
    }
}
