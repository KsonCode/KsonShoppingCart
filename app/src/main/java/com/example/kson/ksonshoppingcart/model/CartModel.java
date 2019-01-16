package com.example.kson.ksonshoppingcart.model;

import android.os.Handler;

import com.example.kson.ksonshoppingcart.api.ProductApi;
import com.example.kson.ksonshoppingcart.contract.CartContract;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class CartModel implements CartContract.ICartModel {
   Handler handler = new Handler();
    @Override
    public void getCarts(HashMap<String, String> params, final ICartmodelCallback callback) {

        //日志拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> map : params.entrySet()) {


            builder.add(map.getKey(),map.getValue());

        }

        Request request = new Request.Builder().post(builder.build()).url(ProductApi.CART_URL)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (callback!=null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.failure("网络有问题");
                        }
                    });

                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String result = response.body().string();
                if (callback!=null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.success(result);
                        }
                    });

                }
            }
        });
    }
}
