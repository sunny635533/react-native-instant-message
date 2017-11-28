package com.bbc.instant;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Nullable;

/**
 * Created by sunny on 2017/11/20.
 */

public class RNInstantMessageModule extends ReactContextBaseJavaModule {


    private static final String REACT_MODULE_NAME = "RNInstantMessage";
    private static final String TAG = RNInstantMessageModule.class.getName();

    private static RNInstantMessageModule INSTANCE;

    protected WebSocketClient webSocketClient;
    protected WebSocketClient.Listener websocketListener;


    public static final int WS_MESSAGE                  = 1;
    public static final int WS_HEART                    = 2;
    public static final int WS_ONLINE_STATUS            = 3;
    public static final int WS_GIFT                     = 4;
    public static final int WS_LIVE_END_BY_PERFORMER    = 5;
    public static final int WS_LIVE_END                 = 6;
    public static final int WS_PERFORMER_END_LIVE       = 7;
    public static final int WS_BAN_USER                 = 8;
    public static final int WS_BAN_MESSAGE              = 9;
    public static final int WS_INTERACTIVE_ICON_LIST    = 11;
    public static final int WS_VIEWER_ENTER             = 14;
    public static final int WS_START_COUNTDOWN          = 16;
    public static final int WS_GIFT_SENDER              = 12;

//    private static final String EVENT_WS_ONLINE_STATUS = REACT_MODULE_NAME + ":WS_ONLINE_STATUS";
//    private static final String EVENT_WS_MESSAGE = REACT_MODULE_NAME + ":WS_MESSAGE";
//    private static final String EVENT_WS_HEART = REACT_MODULE_NAME + ":WS_HEART";
//    private static final String EVENT_WS_GIFT = REACT_MODULE_NAME + ":WS_GIFT";
//    private static final String EVENT_WS_LIVE_END = REACT_MODULE_NAME+":WS_LIVE_END";
//    private static final String EVENT_WS_BAN_USER = REACT_MODULE_NAME + ":WS_BAN_USER";
//    private static final String EVENT_WS_BAN_MESSAGE = REACT_MODULE_NAME + ":WS_BAN_MESSAGE";
//    private static final String EVENT_WS_VIEWER_ENTER = REACT_MODULE_NAME + ":WS_VIEWER_ENTER";
//    private static final String EVENT_WS_GIFT_SENDER = REACT_MODULE_NAME + ":WS_GIFT_SENDER";

    private static final String EVENT_INSTANT_MESSAGE = REACT_MODULE_NAME+":getInstantMessage";


    @Override
    public String getName() {
        return REACT_MODULE_NAME;
    }

    public static @Nullable
    RNInstantMessageModule getInstance() {
        return INSTANCE;
    }

    public RNInstantMessageModule(ReactApplicationContext reactContext) {
        super(reactContext);
        INSTANCE = this;
    }

    @ReactMethod
    public void initWebClient(String socketUrl,String chatSecret,final Promise p){
        URI uri;
        try {
            uri = new URI(socketUrl + "/" + chatSecret);
            Log.d(TAG,"uri = "+uri);

            initListener();
            webSocketClient = new WebSocketClient(uri, websocketListener,null);
            webSocketClient.connect();
            p.resolve("true");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            p.reject("RNInstantMessage.initWebClient()","webSocketClient.connect",e);
        }
    }


    @ReactMethod
    public void disConnectWebClient(){
        if(webSocketClient != null){
            webSocketClient.disconnect();
            websocketListener = null;
        }
    }

    @ReactMethod
    public void connectWebClient(){
        if (webSocketClient != null) {
            if (!webSocketClient.isConnected()) {
                webSocketClient.connect();
            }
        }
    }

    @ReactMethod
    public void sendMessage(String message,Promise p){
        try{
            webSocketClient.send(message);
            p.resolve("true");
        }catch (Exception e){
            p.reject("WebClient.sendMessage","sendMessage error!",e);
        }
    }

    public void initListener(){
        websocketListener = new WebSocketClient.Listener() {
            @Override
            public void onConnect() {
            }

            @Override
            public void onMessage(final String message) {
                Log.d(TAG, "WebClient -> Message received: " + message);

                WritableMap event = Arguments.createMap();
                event.putString("message",message);
                sendEvent(getReactApplicationContext(),EVENT_INSTANT_MESSAGE,event);

//                getReactApplicationContext()
//                        .getJSModule(RCTNativeAppEventEmitter.class)
//                        .emit(EVENT_INSTANT_MESSAGE, event);

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        try {
////                            JSONObject jsonObject = new JSONObject(message);
////                            int msgType = jsonObject.getInt("e");
////                            JSONArray jsonArray = jsonObject.getJSONArray("a");
////                            String[] resultList = new String[jsonArray.length()];
////                            for (int i = 0; i < jsonArray.length(); i++) {
////                                resultList[i] = jsonArray.getString(i);
////                            }
////
////                            WritableMap event = Arguments.createMap();
////                            event.putString("messages",message);
////                            event.putInt("type", msgType);
////                            Bundle bundle = new Bundle();
////                            bundle.putStringArray("bbcMsg", resultList);
////                            event.putMap("bundle",Arguments.fromBundle(bundle));
////
////                            switch (msgType) {
////                                case WS_ONLINE_STATUS: // 在线观众状态信息
////                                    System.out.println("===sunny === WS_ONLINE_STATUS ::msgType="+msgType+",  "+jsonObject.toString());
////                                    getReactApplicationContext()
////                                            .getJSModule(RCTNativeAppEventEmitter.class)
////                                            .emit(EVENT_WS_ONLINE_STATUS, event);
////                                    break;
////                                case WS_LIVE_END: // 直播结束
////                                    System.out.println("===sunny === WS_LIVE_END::msgType="+msgType+",  "+jsonObject.toString());
////                                    getReactApplicationContext()
////                                            .getJSModule(RCTNativeAppEventEmitter.class)
////                                            .emit(EVENT_WS_LIVE_END, event);
////                                    break;
////                                case WS_HEART:// 直播时候发送的动态表情,例如心形、太阳等图标
////                                    System.out.println("===sunny === WS_HEART::msgType="+msgType+",  "+jsonObject.toString());
////                                    getReactApplicationContext()
////                                            .getJSModule(RCTNativeAppEventEmitter.class)
////                                            .emit(EVENT_WS_HEART, event);
////                                    break;
////                                case WS_MESSAGE: // 聊天信息
////                                    System.out.println("===sunny === WS_MESSAGE::msgType="+msgType+",  "+jsonObject.toString());
////                                    getReactApplicationContext()
////                                            .getJSModule(RCTNativeAppEventEmitter.class)
////                                            .emit(EVENT_WS_MESSAGE, event);
////                                    break;
////                                case WS_BAN_USER:
////                                    System.out.println("===sunny === WS_BAN_USER::msgType="+msgType+",  "+jsonObject.toString());
////                                    getReactApplicationContext()
////                                            .getJSModule(RCTNativeAppEventEmitter.class)
////                                            .emit(EVENT_WS_BAN_USER, event);
////                                    break;
////                                case WS_BAN_MESSAGE:
////                                    System.out.println("===sunny === WS_BAN_MESSAGE::msgType="+msgType+",  "+jsonObject.toString());
////                                    getReactApplicationContext()
////                                            .getJSModule(RCTNativeAppEventEmitter.class)
////                                            .emit(EVENT_WS_BAN_MESSAGE, event);
////                                    break;
////                                case WS_GIFT: // 聊天发送的礼物列表
////                                    System.out.println("===sunny === WS_GIFT::msgType="+msgType+",  "+jsonObject.toString());
////                                    getReactApplicationContext()
////                                            .getJSModule(RCTNativeAppEventEmitter.class)
////                                            .emit(EVENT_WS_GIFT, event);
////                                    break;
////                                case WS_VIEWER_ENTER: // 观众进入直播的提示
////                                    System.out.println("===sunny === WS_VIEWER_ENTER::msgType="+msgType+",  "+jsonObject.toString());
////                                    getReactApplicationContext()
////                                            .getJSModule(RCTNativeAppEventEmitter.class)
////                                            .emit(EVENT_WS_VIEWER_ENTER, event);
////                                    break;
////                                case WS_GIFT_SENDER:// 礼物发送的观众列表
////                                    System.out.println("<WS_GIFT_SENDER>::msgType="+msgType+",  "+jsonObject.toString());
////                                    getReactApplicationContext()
////                                            .getJSModule(RCTNativeAppEventEmitter.class)
////                                            .emit(EVENT_WS_GIFT_SENDER, event);
////                                    break;
////                            }
////                        } catch (Exception e) {
////                            Log.e(TAG,"e.printStackTrace = "+e.getMessage());
////                            e.printStackTrace();
////                        }
//                    }
//                });
            }

            @Override
            public void onMessage(byte[] data) {

            }

            @Override
            public void onDisconnect(int code, String reason) {
                Log.d(TAG,"WebClient -> onDisconnect!");
            }

            @Override
            public void onError(Exception error) {
                Log.d(TAG,"WebClient -> onError:::"+error.toString());
            }
        };
    }


    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

}
