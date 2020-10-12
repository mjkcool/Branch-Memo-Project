package com.example.branchmemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Date;

public class NewnoteActivity extends AppCompatActivity {
    Toolbar toolbar2;
    public static ActionBar actionBar;
    EditText titleTxt, contentTxt;
    Button btn_toSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newnote);

        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);//기본 제목
        actionBar.setDisplayHomeAsUpEnabled(true); //툴바의 뒤로가기 버튼

        titleTxt = findViewById(R.id.TitleView);
        contentTxt = findViewById(R.id.ContentView);
        btn_toSave = findViewById(R.id.saveBtn);
        btn_toSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        String code, title, content;
                        Calendar cal = Calendar.getInstance();
                        Date date = new Date(System.currentTimeMillis());

                        //content
                        content = contentTxt.getText().toString().replace("\n", " ");
                        if(content==null || content.length()==0){ //검열
                            Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //title
                        title = titleTxt.getText().toString();
                        if(title==null || title.length() == 0){ //대체 여부 결정
                            String temp_title = content.substring(0, 25);
                            title = temp_title;
                        }
                        content = contentTxt.getText().toString();
                        code = (new CodeCreater()).getNewCode();

                        MemoVo memo = new MemoVo(code, title, content, date);
                        MainActivity.memoDatabase.memeDao().insert(memo);

                        MemoListVo memolist = new MemoListVo(memo.getCode(), title, memo.getDateval());
                        MainActivity.memoListDatabase.memoListDao().insert(memolist);
                    }
                });

                Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_LONG).show();

                titleTxt.setText("");
                contentTxt.setText("");

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {//toolbar의 back키 눌렀을 때 동작
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}