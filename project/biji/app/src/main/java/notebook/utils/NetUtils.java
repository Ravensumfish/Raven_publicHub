/**
 * description: 网络请求工具类(没用到
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/2/20
 */

package notebook.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtils {
    public static String doGet(String mUrl){
        String result = "";

        try {
            //建立连接
            URL url = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(6000);
            connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");
            connection.setRequestProperty("Accept-Encoding","gzip,deflate");
            connection.connect();

            //获取流
            InputStream inputStream = connection.getInputStream();
            //转化流为字符串
            result = SteamToString(inputStream);
            Log.d("TAG","(urlGet:)-->>" + result);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private static String SteamToString(InputStream inputStream) throws IOException {
        BufferedReader reader;
        StringBuilder stringBuilder = new StringBuilder();
        String oneLine;

        //reader包装流,先读取流，再读取流入缓冲区
        reader = new BufferedReader((new InputStreamReader(inputStream)));

        //读取字符串（分行读取）
        try {
            while ((oneLine = reader.readLine()) != null) {
                stringBuilder.append(oneLine).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try{
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(stringBuilder.length() == 0) {
            return null;
        }

        return stringBuilder.toString();
    }
}



