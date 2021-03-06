package com.example.kson.ksonshoppingcart.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kson.ksonshoppingcart.R;
import com.example.kson.ksonshoppingcart.callback.CartCallback;
import com.example.kson.ksonshoppingcart.entity.CartBean;
import com.example.kson.ksonshoppingcart.widget.AddMinusView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

public class ProductAdapter extends XRecyclerView.Adapter<ProductAdapter.MyVh>  {
    private Context context;
    private List<CartBean.Cart.Product> carts;

    /**
     * 一级列表回调接口
     */
    private CartCallback cartCallback;

    public void setCartCallback(CartCallback cartCallback) {
        this.cartCallback = cartCallback;
    }

    public ProductAdapter(Context context, List<CartBean.Cart.Product> carts) {
        this.context = context;
        this.carts = carts;
    }

    @NonNull
    @Override
    public MyVh onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_layout, viewGroup, false);
        return new MyVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyVh myVh, int i) {

        final CartBean.Cart.Product product = carts.get(i);

        myVh.checkBox.setChecked(product.isProductChecked);


        String[] imgs = product.images.split("\\|");
        if (imgs != null && imgs.length > 0) {

            Glide.with(context).load(imgs[0]).into(myVh.iv);
        }

        myVh.addMinusView.setNumTv(product.productNum);//设置数量到edittext
        myVh.priceTv.setText("¥：" + product.price);
        myVh.titleTv.setText(product.title);
        //加减器监听
        myVh.addMinusView.setAddMinusCallback(new AddMinusView.AddMinusCallback() {
            @Override
            public void numCallback(int num) {

                product.productNum = num;//对当前商品数量动态改变
                //通知一级列表数量改变，刷新数据
                if (cartCallback != null) {
                    cartCallback.notifyNum();
                }
            }
        });



        //设置二级按钮选中状态
        myVh.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("myVh.checkBox.isChecked():"+myVh.checkBox.isChecked());

                if (!myVh.checkBox.isChecked()) {//二级未选中
                    product.isProductChecked = false;

                    //一级未选中的回调

                    if (cartCallback != null) {
                        cartCallback.notifyCartItem(false, product.pos);
                    }


                } else {//二级已选中
                    product.isProductChecked = true;

                    //遍历所有数据
                    for (CartBean.Cart.Product cart : carts) {

                        //判断集合内商品的选中状态，如果未选中
                        if (!cart.isProductChecked){
                            if (cartCallback != null) {
                                cartCallback.notifyCartItem(false, product.pos);
                            }
                            break;//跳出循环

                        }
                        //如果都选中，设置一级为true
                        if (cartCallback != null) {
                            cartCallback.notifyCartItem(true, product.pos);
                        }

                        //以下代码有问题，注掉
//                        //如果有一个未选中，一级未选中
//                        if (!myVh.checkBox.isChecked()) {
//                            //一级未选中
//                            cart.isProductChecked = false;
//                            if (cartCallback != null) {
//                                cartCallback.notifyCartItem(false, product.pos);
//                            }
//                            continue;
//                        }
//
//                        //如果都选中，则一级选中
//                        if (myVh.checkBox.isChecked()) {
////                            cart.isProductChecked = true;//这行代码去掉
//                            //一级选中
//                            if (cartCallback != null) {
//                                cartCallback.notifyCartItem(true, product.pos);
//                            }
//
//                        }
                    }


                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return carts == null ? 0 : carts.size();
    }



    public class MyVh extends RecyclerView.ViewHolder {
        private ImageView iv;

        private CheckBox checkBox;
        private TextView titleTv, priceTv;
        private AddMinusView addMinusView;


        public MyVh(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_product);
            checkBox = itemView.findViewById(R.id.checkbox);
            titleTv = itemView.findViewById(R.id.title);
            addMinusView = itemView.findViewById(R.id.addminusView);
            priceTv = itemView.findViewById(R.id.price);
        }
    }
}
