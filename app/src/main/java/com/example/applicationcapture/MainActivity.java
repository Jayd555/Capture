package com.example.applicationcapture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spash s = new spash();
        s.start();
    }

    private class spash extends Thread{
        @Override
        public void run() {
            try{sleep(3000);}
            catch (Exception e)
            {e.printStackTrace();}
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            MainActivity.this.finish();

        }
    }
}
