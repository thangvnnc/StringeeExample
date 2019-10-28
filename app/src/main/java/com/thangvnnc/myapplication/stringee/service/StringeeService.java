package com.thangvnnc.myapplication.stringee.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.stringee.StringeeClient;
import com.stringee.call.StringeeCall;
import com.stringee.exception.StringeeError;
import com.stringee.listener.StringeeConnectionListener;
import com.thangvnnc.myapplication.stringee.common.StringeeToken;

import org.json.JSONObject;

public class StringeeService extends Service implements StringeeConnectionListener {
    
    private final static String TAG = "StringeeService";
    private StringeeClient stringeeClient = null;
    private MediaPlayer mediaPlayer = null;
    private String userTokenId = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initRingStone();
        initStringee();
        createToken("1234");
        refreshToken();
        return START_STICKY;
    }

    @Override
    public void onConnectionConnected(StringeeClient stringeeClient, boolean b) {
        Log.e(TAG, "onConnectionConnected");
    }

    @Override
    public void onConnectionDisconnected(StringeeClient stringeeClient, boolean b) {
        Log.e(TAG, "onConnectionDisconnected");
    }

    @Override
    public void onIncomingCall(StringeeCall stringeeCall) {
        Log.e(TAG, "onIncomingCall");
    }

    @Override
    public void onConnectionError(StringeeClient stringeeClient, StringeeError stringeeError) {
        Log.e(TAG, "stringeeError : " + stringeeError.getMessage());
    }

    @Override
    public void onRequestNewToken(StringeeClient stringeeClient) {
        Log.e(TAG, "onRequestNewToken");
    }

    @Override
    public void onCustomMessage(String s, JSONObject jsonObject) {
        Log.e(TAG, "onCustomMessage");
    }

    private void initRingStone() {
        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.setLooping(true);
    }

    private void startRing() {
        mediaPlayer.start();
    }

    private void stopRing() {
        mediaPlayer.stop();
    }

    private void initStringee() {
        stringeeClient = new StringeeClient(this);
        stringeeClient.setConnectionListener(this);
    }

    private void createToken(String userId) {
        userTokenId = userId;
        refreshToken();
    }

    private void refreshToken() {
        String token = StringeeToken.create(userTokenId);
        stringeeClient.connect(token);
    }
}
