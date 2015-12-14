package com.example.copythat.myapplication;

import android.app.Activity;
import android.media.AudioFormat;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.media.MediaRecorder;
import android.media.AudioRecord;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    AudioRecordThread myAudioCapture;
    FlushBuffer myFulshBuff;
    MediaRecorder VoiceRecord;
    public static AudioRecord recorder;
    public static short[] FullBuffer;
    public static short[] buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "kill me", Toast.LENGTH_SHORT).show();
        VoiceRecord = new MediaRecorder();
        VoiceRecord.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        //VoiceRecord.setAudioSource(MediaRecorder.AudioSource.VOICE_DOWNLINK); // Kernel
        //VoiceRecord.setAudioSource(MediaRecorder.AudioSource.VOICE_UPLINK); // Kernel
        VoiceRecord.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        VoiceRecord.setOutputFile("/sdcard/test/yolo.mp3");
        VoiceRecord.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        recorder = new AudioRecord(MediaRecorder.AudioSource.VOICE_CALL, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
        buffer = new short[AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)];
        FullBuffer = new short[1000000];
        myAudioCapture = new AudioRecordThread();
        myFulshBuff = new FlushBuffer();
        Log.d("Buff Value", buffer.toString());

        TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private PhoneStateListener mPhoneListener = new PhoneStateListener()
    {
        boolean status = false;
        public void onCallStateChanged(int state, String incomingNumber)
        {
            switch (state)
            {
                case TelephonyManager.CALL_STATE_RINGING:
                    Toast.makeText(MainActivity.this, "Appel entrant", Toast.LENGTH_SHORT).show();
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Toast.makeText(MainActivity.this, "Appel en cours", Toast.LENGTH_SHORT).show();
                    //VoiceRecord.prepare();
                    //VoiceRecord.start();
                    recorder.startRecording();
                    myAudioCapture.start();
                    myFulshBuff.start();
                    status = true;
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    Toast.makeText(MainActivity.this, "Apple end", Toast.LENGTH_SHORT).show();
                    if (status)
                    {
                        status = false;
                        //VoiceRecord.stop();
                        //VoiceRecord.release();
                        recorder.stop();
                        recorder.release();
                        myAudioCapture.interrupt();
                        myFulshBuff.interrupt();
                        Log.d("Debug", "App finished");
                        ByteBuffer bytebuf = ByteBuffer.allocate(2*MainActivity.FullBuffer.length);
                        int i = 0;
                        while (MainActivity.FullBuffer.length > i)
                            bytebuf.putShort(MainActivity.FullBuffer[i++]);
                        File someFile = new File("/sdcard/test/test.wav");
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(someFile);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.write('R');
                            fos.write('I');
                            fos.write('F');
                            fos.write('F');
                            fos.write((byte)((MainActivity.FullBuffer.length + 36) & 0xff));
                            fos.write((byte)(((MainActivity.FullBuffer.length + 36) >> 8) & 0xff));
                            fos.write((byte)(((MainActivity.FullBuffer.length + 36) >> 16) & 0xff));
                            fos.write((byte)(((MainActivity.FullBuffer.length + 36) >> 24) & 0xff));
                            fos.write('W');
                            fos.write('A');
                            fos.write('V');
                            fos.write('E');
                            fos.write('f');
                            fos.write('m');
                            fos.write('t');
                            fos.write(' ');
                            fos.write(16);
                            fos.write(0);
                            fos.write(0);
                            fos.write(0);
                            fos.write(1);
                            fos.write(0);
                            fos.write(2);
                            fos.write(0);
                            fos.write((byte)(44100 & 0xff));
                            fos.write((byte)((44100 >> 8) & 0xff));
                            fos.write((byte)((44100 >> 16) & 0xff));
                            fos.write((byte)((44100 >> 24) & 0xff));
                            fos.write((byte)(88200 & 0xff));
                            fos.write((byte)((88200 >> 8) & 0xff));
                            fos.write((byte)((88200 >> 16) & 0xff));
                            fos.write((byte)((88200 >> 24) & 0xff));
                            fos.write((byte)(4));
                            fos.write(0);
                            fos.write((byte)16);
                            fos.write(0);
                            fos.write('d');
                            fos.write('a');                 // 00 - RIFF
                            fos.write('t');                 // 00 - RIFF
                            fos.write('a');                 // 00 - RIFF
                            fos.write((byte)(MainActivity.FullBuffer.length & 0xff)); // 0 - size of this chunk
                            fos.write((byte)((MainActivity.FullBuffer.length >> 8) & 0xff)); // 0 - size of this chunk
                            fos.write((byte)((MainActivity.FullBuffer.length >> 16) & 0xff)); // 0 - size of this chunk
                            fos.write((byte)((MainActivity.FullBuffer.length >> 24) & 0xff)); // 0 - size of this chunk
                            fos.write(bytebuf.array());
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    Log.d("Appel", "Unknown phone state=" + state);
            }
        }
    };
}
