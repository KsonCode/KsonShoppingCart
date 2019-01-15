package com.example.kson.ksonshoppingcart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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


        myVh.priceTv.setText("¥：" + product.price);
        myVh.titleTv.setText(product.title);
        myVh.addMinusView.setAddMinusCallback(new AddMinusView.AddMinusCallback() {
            @Override
            public void numCallback(int num) {
                product.productNum = num;
                if (cartCallback != null) {
                    cartCallback.notifyNum();
                }
            }
        });



        myVh.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("myVh.checkBox.isChecked():"+myVh.checkBox.isChecked());

                if (!myVh.checkBox.isChecked()) {//未选中
                    product.isProductChecked = false;

                    //一级未选中

                    if (cartCallback != null) {
                        cartCallback.notifyCartItem(false, product.pos);
                    }


                } else {//已选中
                    product.isProductChecked = true;

                    for (CartBean.Cart.Product cart : carts) {

                        if (!myVh.checkBox.isChecked()) {
                            //一级未选中
                            cart.isProductChecked = false;
                            if (cartCallback != null) {
                                cartCallback.notifyCartItem(false, product.pos);
                            }
                            return;
                        }

                        if (myVh.checkBox.isChecked()) {
//                            cart.isProductChecked = true;//这行代码去掉
                            //一级选中
                            if (cartCallback != null) {
                                cartCallback.notifyCartItem(true, product.pos);
                            }

                        }
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
