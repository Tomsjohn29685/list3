package com.example.list3;

//public class webitemMyTask implements Runnable{
//    Handler handler;
//    public void setHandler(Handler handler){
//        this.handler = handler;
//    }
//    public void run() {
//        ArrayList<HashMap<String,String>> list = new ArrayList<>();
//        try {
//            Log.d(TAG, "尝试连接网站");
//            Document doc = Jsoup.connect("https://www.huilvbiao.com/bank/spdb").get();
//            Log.d(TAG, "获取HTML文档");
//
//            Element table = doc.select("table").first();
//            Elements rows = table.select("tr");
//
//            for (Element row : rows) {
//                Element coinSpan = row.select("span").first();
//                String currencyName = coinSpan != null ? coinSpan.text() : "未知币种";
//
//                Elements tds = row.select("td");
//                if (tds.size() < 4) continue;
//
//                String data1 = tds.get(0).text();
//                String data2 = tds.get( 1).text();
//                String data3 = tds.get(2).text();
//                String data4 = tds.get(3).text();
//                Log.i(TAG, String.format(
//                        "%s  现汇买入:%s 现汇卖出:%s 现钞买入:%s 现钞卖出:%s",
//                        currencyName, data1, data2, data3, data4
//                ));
//                HashMap<String, String> map = new HashMap<String,String>();
//                map.put("ItemTitle","title"+coinSpan); //title
//                map.put("Price",data1); // description
//                list.add(map);
//                float f = Float.parseFloat(data1);
//            }
//
//            Message msg = handler.obtainMessage(9, list);
//            handler.sendMessage(msg);
//
//        } catch (IOException e) {
//            Log.e(TAG, "网络请求异常", e);
//            handler.sendEmptyMessage(0);
//        };
//    }
//}
