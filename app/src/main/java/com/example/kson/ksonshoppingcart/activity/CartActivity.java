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

public class CartActivity extends AppCompatActivity implements CartContract.ICartView,CartUICallback,XRecyclerView.LoadingListener {
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

    /**
     * 初始化数据
     */
    private void initData() {
        cartPresenter = new CartPresenter(this);
        carts = new ArrayList<>();

        cartPresenter.getCarts(new HashMap<String, String>());

    }


    /**
     * 初始化view
     */
    private void initView() {
        xRecyclerView = findViewById(R.id.rv);
        xRecyclerView.setLoadingListener(this);//设置加载监听器
        xRecyclerView.setLoadingMoreEnabled(true);//设置上拉加载
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        checkBox = findViewById(R.id.checkbox);
        //设置全选反选按钮
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {//全选按钮选中
                    // TODO: 2019/1/15

                    for (CartBean.Cart cart : carts) {
                        cart.isChecked = true;//设置一级商家选中
                        for (CartBean.Cart.Product product : cart.list) {
                            product.isProductChecked = true;//设置二级商品选中
                        }
                    }

                } else {//未选中

                    for (CartBean.Cart cart : carts) {
                        cart.isChecked = false;//设置一级商家未选中
                        for (CartBean.Cart.Product product : cart.list) {
                            product.isProductChecked = false;//设置二级商品未选中
                        }
                    }
                }

                //通知刷新适配器
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
        //遍历所有商品计算总价
        for (CartBean.Cart cart : carts) {

            for (CartBean.Cart.Product product : cart.list) {

                if (product.isProductChecked) {
                    System.out.println("num:"+product.productNum);
                    totalprice += product.price*product.productNum;
                }

            }
        }
        //设置总价
        checkBox.setText("¥：" + totalprice);


    }

    /**
     * 通知底部导航栏总价联动
     */
    @Override
    public void notifyCart() {
        getTotalPrice();
    }

    /**
     * 下拉
     */
    @Override
    public void onRefresh() {

        xRecyclerView.refreshComplete();

    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMore() {
        xRecyclerView.loadMoreComplete();
    }
}
