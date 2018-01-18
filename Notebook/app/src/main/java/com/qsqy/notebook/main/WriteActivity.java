package com.qsqy.notebook.main;

import android.annotation.TargetApi;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.google.gson.Gson;
import com.qsqy.notebook.BaseActivity;
import com.qsqy.notebook.R;
import com.qsqy.notebook.main.entity.NoteBook;
import com.qsqy.notebook.sdk.db.NoteDB;
import com.qsqy.notebook.sdk.textview.FontTextView;
import com.qsqy.notebook.sdk.toast.Toast;

import java.util.Date;

public class WriteActivity extends BaseActivity implements View.OnClickListener,TextWatcher{
    private FontTextView leftTv,rightTv;
    private EditText themeEdit,contentEdit;
    private Switch openSwitch;
    private NoteBook datas;
    private boolean isUpdate = false;
    private boolean isOpen = true;
    private String theme,content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        init();
    }

    private void init(){
        leftTv = (FontTextView)findViewById(R.id.left_tv);
        rightTv = (FontTextView)findViewById(R.id.right_tv);
        themeEdit = (EditText)findViewById(R.id.theme_et);
        contentEdit = (EditText)findViewById(R.id.content_et);
        openSwitch = (Switch) findViewById(R.id.open_switch);

        leftTv.setOnClickListener(this);
        rightTv.setOnClickListener(this);

        if(getIntent().getStringExtra("datas")!=null){
            isUpdate = true;
            datas = new Gson().fromJson(getIntent().getStringExtra("datas"), NoteBook.class);
            themeEdit.setText(datas.getTitle());
            contentEdit.setText(datas.getMessage());
            openSwitch.setChecked(datas.getPassword().equals("0") ? true : false);
            isOpen = datas.getPassword().equals("0") ? true : false;
        }

        themeEdit.addTextChangedListener(this);
        contentEdit.addTextChangedListener(this);
        openSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isOpen != b){
                    theme = themeEdit.getText().toString().trim();
                    content = contentEdit.getText().toString().trim();
                    if(theme.equals("") && content.equals("")){
                        rightTv.setVisibility(View.GONE);
                    }else{
                        rightTv.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_tv:
                finish();
                break;
            case R.id.right_tv:
                NoteDB noteDB = new NoteDB(WriteActivity.this);
                String theme = themeEdit.getText().toString().trim();
                String content = contentEdit.getText().toString().trim();
                String time = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date());

                if(isUpdate){
                    noteDB.update(datas.getId(), theme, content, (openSwitch.isChecked() ? "0" : "1"), time);
                    Toast.show(this,"修改成功",1500);
                    setResult(2);
                }else{
                    noteDB.saveNote(theme, content, (openSwitch.isChecked() ? "0" : "1"), time);
                    Toast.show(this,"保存成功",1500);
                    setResult(1);
                }

                noteDB.close();
                finish();
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        theme = themeEdit.getText().toString().trim();
        content = contentEdit.getText().toString().trim();
        if(theme.equals("") && content.equals("")){
            rightTv.setVisibility(View.GONE);
        }else{
            rightTv.setVisibility(View.VISIBLE);
        }
    }
}
