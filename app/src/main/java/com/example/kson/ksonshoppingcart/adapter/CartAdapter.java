package com.example.kson.ksonshoppingcart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kson.ksonshoppingcart.R;
import com.example.kson.ksonshoppingcart.callback.CartCallback;
import com.example.kson.ksonshoppingcart.callback.CartUICallback;
import com.example.kson.ksonshoppingcart.entity.CartBean;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

public class CartAdapter extends XRecyclerView.Adapter<CartAdapter.MyVh> implements CartCallback {
    private Context context;
    private List<CartBean.Cart> carts;

    /**
     * 通知首页的接口
     */
    private CartUICallback cartCallback;

    /**
     * 初始化首页接口，对外暴露
     * @param cartCallback
     */
    public void setCartCallback(CartUICallback cartCallback) {
        this.cartCallback = cartCallback;
    }

    public CartAdapter(Context context, List<CartBean.Cart> carts) {
        this.context = context;
        this.carts = carts;
    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_cart_item, viewGroup, false);
        return new MyVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyVh myVh, int i) {

        final CartBean.Cart cart = carts.get(i);
        myVh.nameTv.setText(cart.sellerName);



        myVh.checkBox.setChecked(cart.isChecked);

        //对每件商品的pos赋值，记录一级列表的位置pos
        for (CartBean.Cart.Product product : cart.list) {
            product.pos = i;
        }

        //二级适配器
        ProductAdapter productAdapter = new ProductAdapter(context, cart.list);
        productAdapter.setCartCallback(this);//设置二级适配器回调接口

        myVh.xRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        myVh.xRecyclerView.setAdapter(productAdapter);

        myVh.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.isChecked = myVh.checkBox.isChecked();//设置一级对象的选中状态

                //设置二级对象选中状态
                for (CartBean.Cart.Product product : cart.list) {
                    product.isProductChecked = cart.isChecked;
                }

                //通知刷新
                notifyDataSetChanged();
                //选中状态改变后，通知首页价格联动
                if (cartCallback!=null){
                    cartCallback.notifyCart();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return carts == null ? 0 : carts.size();
    }

    /**
     * 二级列表选中状态的监听，通知一级列表刷新数据
     * @param isChecked
     * @param postion
     */
    @Override
    public void notifyCartItem(boolean isChecked, int postion) {

//        if (isChecked){
//            carts.get(postion).isChecked = true;
//        }else{
//            carts.get(postion).isChecked = false;
//        }
        //设置一级列表的选中状态
        carts.get(postion).isChecked = isChecked;
        notifyDataSetChanged();

        //选中状态改变后，通知主页联动价格
        if (cartCallback!=null){
            cartCallback.notifyCart();
        }


    }

    /**
     * 数量改变后，通知价格联动
     */
    @Override
    public void notifyNum() {
        if (cartCallback!=null){
            cartCallback.notifyCart();
        }
    }

    public class MyVh extends RecyclerView.ViewHolder {
        private XRecyclerView xRecyclerView;

        private CheckBox checkBox;
        private TextView nameTv;

        public MyVh(@NonNull View itemView) {
            super(itemView);
            xRecyclerView = itemView.findViewById(R.id.rv);
            checkBox = itemView.findViewById(R.id.checkbox);
            nameTv = itemView.findViewById(R.id.name);
        }
    }
}
