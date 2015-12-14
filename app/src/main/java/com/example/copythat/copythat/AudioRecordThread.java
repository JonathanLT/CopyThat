package com.example.copythat.myapplication;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.nio.Buffer;
import java.nio.ShortBuffer;

/**
 * Created by Jonathan on 11/12/2015.
 */
public class AudioRecordThread extends Thread {

    @Override
    public void run() {
        while (true) {
            MainActivity.recorder.read(MainActivity.buffer, 0, MainActivity.buffer.length);
        }
    }
}
