package com.example.copythat.copythat;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.media.MediaRecorder;

import java.io.IOException;
 
public class MainActivity extends Activity {

    private int i;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "<3", Toast.LENGTH_SHORT).show();
        MediaRecorder VoiceRecord = new MediaRecorder();
        VoiceRecord.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        VoiceRecord.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        VoiceRecord.setOutputFile("/sdcard/test/yolo.mp3");
        VoiceRecord.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        try {
                VoiceRecord.prepare();
                VoiceRecord.start();
                i = 0;
                while(i++ < 1000000)
                    Log.d("yolo", Integer.toString(i));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Toast de la mort", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
