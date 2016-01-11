package com.example.mypegasus.learnvolley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


/**
 * Volley 提供的功能：
 * 1.JSON、图片（异步）
 * 2.网络请求的排序
 * 3.网络请求的优先级处理
 * 4.缓存
 * 5.多级别的取消请求
 * 6.与Activity生命周期联动
 * */
public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private NetworkImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (NetworkImageView) findViewById(R.id.imageView2);
        requestQueue = Volley.newRequestQueue(this);
        getJSONVolley();
        loadImageVolley(imageUrl);
        //loadImageVolley();
        loadNetWorkImageViewVolley();
    }

    public void getJSONVolley() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

//        String url = "http://localhost:63343/test20151231/jsondata.html";
        String url = "http://10.0.3.2:63343/test20151231/jsondata.html";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("请求出错，msg：" + error.getMessage());
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    RequestQueue requestQueue = null;
    String imageUrl = "http://10.0.3.2:63343/test20151231/img-1.png";

    public void loadImageVolley(String imageUrl) {
        ImageRequest imageRequest = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 250, 200, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("请求出错，msg：" + error.getMessage());
            }
        });
        requestQueue.add(imageRequest);
    }

    public void loadImageVolley() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
//        android.util.LruCache<> lruCache = new android.util.LruCache<String, Bitmap>(20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return lruCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                lruCache.put(url, bitmap);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        ImageLoader.ImageListener imageListener = imageLoader.getImageListener(imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(imageUrl, imageListener);
    }

    public void loadNetWorkImageViewVolley() {
        String imageUrl = "http://10.0.3.2:63343/test20151231/img-1.png";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return lruCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                lruCache.put(url, bitmap);
            }

        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        imageView2.setTag("url");
        imageView2.setImageUrl(imageUrl, imageLoader);
    }
}
