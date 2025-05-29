package com.example.list3;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class cus_list2 extends ListActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "cus_list2";
    private ArrayList<HashMap<String,String>> listItems;
    private SimpleAdapter listItemAdapter;
    Handler handler;
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtomicReference<SharedPreferences> sp = new AtomicReference<>(getSharedPreferences("myrate", Context.MODE_PRIVATE));
        //logDate:保存从SP获得的数据集
        logDate = sp.get().getString(DATE_SP_KEY,"");
        Log.i("List","lastRatedateStr=" + logDate);

        initListView();
        setListAdapter(listItemAdapter);

        RateManager rateManager = new RateManager(this);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                //处理接收的消息
                if (msg.what == 9) {
                    listItems = (ArrayList<HashMap<String,String>>)msg.obj;
                    MyAdapter adapter = new MyAdapter(cus_list2.this,R.layout.list_item,listItems);
                    setListAdapter(adapter);
//                    rateManager.deleteAll(listItems);
//                    rateManager.addAll(listItems);
//                    Log.i(TAG,"handleMessage:写入所有数据");
                }
                else if (msg.what == 0) {
                    Log.e(TAG, "网络请求失败");
                    Toast.makeText(cus_list2.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
//thread
        Thread t = new Thread(()->{
        //原来的代码：不加入数据库
            ArrayList<HashMap<String,String>> list = new ArrayList<>();
            try {
                Log.d(TAG, "尝试连接网站");
                Document doc = Jsoup.connect("https://www.huilvbiao.com/bank/spdb").get();
                Log.d(TAG, "获取HTML文档");

                Element table = doc.select("table").first();
                Elements rows = table.select("tr");

                for (Element row : rows) {
                    Element coinSpan = row.select("span").first();
                    String currencyName = coinSpan != null ? coinSpan.text() : "未知币种";

                    Elements tds = row.select("td");
                    if (tds.size() < 4) continue;

                    String data1 = tds.get(0).text();
                    String data2 = tds.get(1).text();
                    String data3 = tds.get(2).text();
                    String data4 = tds.get(3).text();
                    Log.i(TAG, String.format(
                            "%s  现汇买入:%s 现汇卖出:%s 现钞买入:%s 现钞卖出:%s",
                            currencyName, data1, data2, data3, data4
                    ));
                    HashMap<String, String> map = new HashMap<String,String>();
                    map.put("ItemTitle","title"+coinSpan); //title
                    map.put("Price",data1); // description
                    list.add(map);
                    float f = Float.parseFloat(data1);
                }

                Message msg = handler.obtainMessage(9, list);
                handler.sendMessage(msg);

            } catch (IOException e) {
                Log.e(TAG, "网络请求异常", e);
                handler.sendEmptyMessage(0);
            }
//修改数据库代码
//            String curDateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//            Log.i("run", "curDateStr:" + curDateStr + " logDate:" + logDate);
//            if (curDateStr.equals(logDate)) {
//                Log.i("run", "日期相等，从数据库中获取数据");
//                List<HashMap<String, String>> hashMapList =RateManager.listAll();
//                for (HashMap<String, String> item : hashMapList) {
//                    List.add(item.get("ItemTitle") + "=>" + item.get("Price"));
//                }
//            } else {
//                Log.i("run", "日期不相等，从网络中获取在线数据");
//                ArrayList<HashMap<String,String>> list = new ArrayList<>();
//                // 数据库操作改为处理HashMap
//                RateManager.deleteAll(listItems);
//                Log.i("db", "删除所有记录");
//                RateManager.addAll(listItems);
//                Log.i("db", "添加新记录集");
//
//                // 更新存储日期
//                sp.set(getSharedPreferences("myrate", Context.MODE_PRIVATE));
//                sp.get().edit().putString(DATE_SP_KEY, curDateStr).apply();
//                Log.i("run", "更新日期结束：" + curDateStr);
//
//            }
//
//            Message msg;
//            msg.obj = listItems;
//            msg.what = 0;
//            handler.sendMessage(msg);



        });
        t.start();
}

    private void initListView(){
        listItems = new ArrayList<HashMap<String,String>>();
        for (int i = 0;i < 10;i++) {
            HashMap<String, String> map = new HashMap<String,String>();
            map.put("ItemTitle","title"+i); //title
            map.put("Price", "detail" +i); // description
            listItems.add(map);
        }
// 生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this, listItems, // ListItems数据源
                R.layout.list_item, //ListItemXML
                new String[] { "ItemTitle", "Price" },
                new int[] { R.id.itemTitle, R.id.price }
);
    }

    @Override
    public void onItemClick(AdapterView <? > adapterView, View view, int position, long id) {
        HashMap<String, String> map = (HashMap<String, String>) getListView().getItemAtPosition(position);
        String titleStr = map.get("ItemTitle");
        String priceStr = map.get("Price");
        Log.i(TAG, "onItemClick: titleStr=" + titleStr);
        Log.i(TAG, "onItemClick: detailStr=" + priceStr);
        Log.i(TAG, "onItemclick: position=" + position);
        // 启动 CalculateActivity，并传入币种名和汇率
        Intent intent = new Intent(cus_list2.this, CalculateActivity.class);
        intent.putExtra("currency_name", titleStr);
        intent.putExtra("exchange_rate", priceStr);
        startActivity(intent);
    }
}