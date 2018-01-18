package com.qsqy.notebook.sdk.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qsqy.notebook.R;
import com.qsqy.notebook.sdk.toast.Toast;

/**
 * Created by CK on 2018/1/11.
 */

public class CustomDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private TextView titleTv;
    private TextView messageTv;
//    private RadioGroup radioGroup;
    private EditText password1;
    private EditText password2;
    private Button sureBtn;
    private Button cancelBtn;
    public static final int TIP = 0, INPUT = 1;
    private int model;
    private OnDialogClickListener listener;

    public CustomDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CustomDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    public CustomDialog setOnDialogClickListener(OnDialogClickListener listener){
        this.listener = listener;
        return this;
    }

    private void init(Context context){
        this.context = context;
        setContentView(R.layout.app_dialog);

        titleTv = (TextView) findViewById(R.id.title_tv);
        messageTv = (TextView) findViewById(R.id.message_tv);
//        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        password1 = (EditText) findViewById(R.id.password_1);
        password2 = (EditText) findViewById(R.id.password_2);
        sureBtn = (Button) findViewById(R.id.sure_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_btn);

        sureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
//                //获取变更后的选中项的ID
//                int radioButtonId = radioGroup.getCheckedRadioButtonId();
//                RadioButton rb = (RadioButton)findViewById(radioButtonId);
//                Log.d("api","rb.getText():"+rb.getText());
//                if(rb.getText().equals("1")){
//                    passwordNum = 1;
//                    password2.setText("");
//                    password2.setVisibility(View.GONE);
//                }else{
//                    passwordNum = 2;
//                    password2.setVisibility(View.VISIBLE);
//                }
//            }
//        });
    }

    /**
     * 设置标题
     * @param title
     * @return
     */
    public CustomDialog setTitle(String title){
        titleTv.setText(title);
        return this;
    }

    /**
     * 设置对话框内容
     * @param model TIP：没有输入框；INPUT：有输入框
     * @param message
     * @return
     */
    public CustomDialog setMessage(int model, String message){
        this.model = model;
        switch(model){
            case TIP:
                messageTv.setVisibility(View.VISIBLE);
//                radioGroup.setVisibility(View.GONE);
                password1.setVisibility(View.GONE);
//                password2.setVisibility(View.GONE);
                break;
            case INPUT:
                messageTv.setVisibility(View.VISIBLE);
//                radioGroup.setVisibility(View.VISIBLE);
                password1.setVisibility(View.VISIBLE);
//                password2.setVisibility(View.GONE);
                break;
        }
        messageTv.setText(message);
        return this;
    }

    /**
     * 设置按钮名称
     * @param sureStr
     * @param cancelStr
     * @return
     */
    public CustomDialog setButton(String sureStr, String cancelStr){
        sureBtn.setText(sureStr);
        cancelBtn.setText(cancelStr);
        return this;
    }

    public String getPassword1(){
        return password1.getText().toString().trim();
    }

    public String getPassword2(){
        return password2.getText().toString().trim();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sure_btn:
                if(model == TIP){
                    listener.click(view.getId(), this, null, null);
                    dismiss();
                }else{
                    if(getPassword1().length()>0){
                        listener.click(view.getId(), this, getPassword1(), null);
                        dismiss();
                    }else{
                        Toast.show((Activity) context,"密码不能为空",1500);
                    }
                }
                break;
            case R.id.cancel_btn:
                listener.click(view.getId(), this, null, null);
                dismiss();
                break;
        }


    }

    public interface OnDialogClickListener {

        public void click(int index, Dialog view, String password1, String password2);
    }


    @Override
    public void show() {
        super.show();
    }
}
