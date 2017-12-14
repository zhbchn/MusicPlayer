package cn.itcast.musicplayer;
import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {
    private EditText path;
    private Intent intent;
    private myConn conn;
    MusicService.MyBinder binder;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        path = (EditText) findViewById(R.id.et_inputpath);
        findViewById(R.id.tv_play).setOnClickListener(this);
        findViewById(R.id.tv_pause).setOnClickListener(this);
        conn = new myConn();
        intent = new Intent(this, MusicService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);

    }
    private class myConn implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MusicService.MyBinder) service;
        }
        public void onServiceDisconnected(ComponentName name) {
        }
    }
    public void onClick(View v) {
        String pathway = path.getText().toString().trim();
        File SDpath = Environment.getExternalStorageDirectory();
        Log.i("SDpath",SDpath.getName()+":"+SDpath.getAbsolutePath());
        //Toast.makeText(this,SDpath.getPath()+" 路径",Toast.LENGTH_LONG);
        File file = new File(SDpath,pathway);
        String path = file.getAbsolutePath();
        Toast.makeText(this,path,Toast.LENGTH_LONG);
        Log.i("musicPath",path);
        switch (v.getId()) {
            case R.id.tv_play:
                if (file.exists() && file.length() > 0) {
                    binder.play(path);
                } else {
                    Toast.makeText(this, "找不到音乐文件", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_pause:
                binder.pause();
                break;
            default:
                break;
        }
    }
    protected void onDestroy() {
        unbindService(conn);
        super.onDestroy();
    }



}
