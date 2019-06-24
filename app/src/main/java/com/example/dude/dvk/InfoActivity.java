package com.example.dude.dvk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.dude.dvk.databinding.ActivityInfoBinding;

import java.io.FileOutputStream;

public class InfoActivity extends AppCompatActivity {

    State state;


    public InfoActivity() {
    }

    public InfoActivity(State state){
        this.state = state;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ActivityInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_info);
        binding.setState(MainActivity.state);


    }


    public void getLastJsClick(View v) {

        //НАДО ДОДЕБАЖИТЬ

        /*
        if (!MainActivity.cache.isEmpty()){
            byte[] bytes = MainActivity.cache.values().iterator().next();


            FileOutputStream fos;

            try{
                fos = openFileOutput("commons.js", Context.MODE_APPEND);
                fos.write(bytes);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        */
    }

}
