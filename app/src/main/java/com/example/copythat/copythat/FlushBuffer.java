package com.example.copythat.myapplication;

import android.util.Log;

import java.util.Arrays;

/**
 * Created by Jonathan on 14/12/2015.
 */
public class FlushBuffer extends Thread {
    @Override
    public void run() {
        while (true) {
            if (MainActivity.buffer[4095] != 0)
            {
                System.arraycopy(MainActivity.buffer, 0, MainActivity.FullBuffer, MainActivity.FullBuffer.length, MainActivity.buffer.length);
                Arrays.fill(MainActivity.buffer, (short) 0);
            }
        }
    }
}
