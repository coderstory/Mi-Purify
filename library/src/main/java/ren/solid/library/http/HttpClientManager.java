package ren.solid.library.http;


import java.util.Map;

import ren.solid.library.http.callback.HttpCallBack;
import ren.solid.library.http.request.HttpRequest;


/**
 * Created by _SOLID
 * Date:2016/5/14
 * Time:11:03
 */
public class HttpClientManager {


    public static void getData(String url, HttpCallBack callBack) {
        HttpRequest request = new HttpRequest.Builder().method(HttpRequest.Method.GET).url(url).create();
        HttpHelper.getProvider().loadString(request, callBack);
    }

    public static void getData(String url, Map<String, String> params, HttpCallBack callBack) {
        HttpRequest request = new HttpRequest.Builder().method(HttpRequest.Method.GET).url(url).params(params).create();
        HttpHelper.getProvider().loadString(request, callBack);
    }
}
