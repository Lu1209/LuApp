package com.qsqy.notebook;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qsqy.notebook.main.WriteActivity;
import com.qsqy.notebook.main.adapter.NoteAdapter;
import com.qsqy.notebook.sdk.db.NoteDB;
import com.qsqy.notebook.sdk.db.NoteOpenHelper;
import com.qsqy.notebook.main.entity.NoteBook;
import com.qsqy.notebook.sdk.dialog.CustomDialog;
import com.qsqy.notebook.sdk.toast.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.qsqy.notebook.sdk.dialog.CustomDialog.INPUT;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        NoteAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener, NoteAdapter.OnItemLongClickListener {
    private NoteOpenHelper dbHelper;
    private TextView titleTv;
    private ListView listView;
    private NoteAdapter adapter;
    private List<NoteBook> datas;
    private List<NoteBook> list_delete = new ArrayList<NoteBook>();// 需要删除的数据
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private int type = 0;//0:公开；1：私人
    private boolean isJion = false;//false:没有进去过私人空间，true:有进去过私人空间
    private NoteDB noteDB;

    private RelativeLayout multipleChoiceRl;
    private TextView delAllTv;
    boolean selectAll = false;//是否全选


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new NoteOpenHelper(this, "Note.db", null, 1);
        dbHelper.getWritableDatabase();
        init();
        initData();
    }

    private void init(){
        titleTv = (TextView) findViewById(R.id.title_tv);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView)findViewById(R.id.listview);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        multipleChoiceRl = (RelativeLayout) findViewById(R.id.multiple_choice_rl);
        delAllTv = (TextView) findViewById(R.id.del_all_tv);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        delAllTv.setOnClickListener(this);

        findViewById(R.id.logo_iv).setOnClickListener(this);
        findViewById(R.id.add_tv).setOnClickListener(this);
        findViewById(R.id.left_tv).setOnClickListener(this);
        findViewById(R.id.right_tv).setOnClickListener(this);

        multipleChoiceRl.setVisibility(View.GONE);
    }

    private void initData(){
        noteDB = new NoteDB(this);
        if(noteDB.findAllData().size()>0){
            datas = noteDB.findData(type);
            Log.d("api","datas:"+new Gson().toJson(datas,new TypeToken<ArrayList<NoteBook>>(){}.getType()));
            if(datas != null){
                adapter = new NoteAdapter(this,false,false,datas,-1,this,this);
                listView.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1 || resultCode == 2){
            recover();
            initData();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logo_iv:
                drawer.openDrawer(navigationView);
                break;
            case R.id.add_tv:
                Intent intent = new Intent(this, WriteActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.left_tv:
                recover();
                adapter = new NoteAdapter(this,false,false,datas,-1,this,this);
                listView.setAdapter(adapter);
                break;
            case R.id.right_tv:
                recover();
                if(list_delete!=null){
                    for(int i=0 ;i<list_delete.size(); i++){
                        noteDB.deleteNote(list_delete.get(i).getId());
                    }
                }
                datas = noteDB.findData(type);
                adapter = new NoteAdapter(this,false,false,datas,-1,this,this);
                listView.setAdapter(adapter);
                break;
            case R.id.del_all_tv:
                if(selectAll){
                    selectAll = false;
                    titleTv.setText("共选择了0项");
                    delAllTv.setText("全选");
                    list_delete=null;
                }else{
                    selectAll = true;
                    titleTv.setText("共选择了"+datas.size()+"项");
                    delAllTv.setText("全不选");
                    list_delete = datas;
                }
                adapter = new NoteAdapter(this,true,selectAll,datas,-1,this,this);
                listView.setAdapter(adapter);
                break;
        }
    }

    @Override
    public void onItemClick(View view, int index, boolean isChecked, List<NoteBook> list_delete) {
        if(isChecked){
            this.list_delete = list_delete;
            titleTv.setText("共选择了"+list_delete.size()+"项");
            if(list_delete.size() == datas.size()){
                selectAll = true;
                delAllTv.setText("全不选");
            }else if(list_delete.size() == 0){
                selectAll = false;
                delAllTv.setText("全选");
            }
        }else{
            Intent intent = new Intent(this, WriteActivity.class);
            intent.putExtra("datas",new Gson().toJson(datas.get(index), NoteBook.class));
            startActivityForResult(intent,2);
        }
    }

    @Override
    public void onItemLongClick(View view, int position, List<NoteBook> list_delete) {
        this.list_delete = list_delete;
        multipleChoiceRl.setVisibility(View.VISIBLE);
        titleTv.setText("共选择了1项");
        if(list_delete.size() == datas.size()){
            selectAll = true;
            delAllTv.setText("全不选");
        }else if(list_delete.size() == 0){
            selectAll = false;
            delAllTv.setText("全选");
        }
        adapter = new NoteAdapter(this, true, false, datas, position, this, this);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.note_public) {
            type = 0;
            initData();
        } else if (id == R.id.note_private) {
            if(noteDB.findPasswordData().size()>0){
                if(isJion){
                    type = 1;
                    initData();
                }else{
                    joinPrivate();
                }
            }else{
                setPassword();
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && !titleTv.getText().toString().equals("记事本")){
            recover();
            adapter = new NoteAdapter(this,false,false,datas,-1,this,this);
            listView.setAdapter(adapter);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void recover(){
        multipleChoiceRl.setVisibility(View.GONE);
        titleTv.setText("记事本");
        selectAll = false;
        delAllTv.setText("全选");
    }

    private void joinPrivate(){
        new CustomDialog(this).setTitle("提示").setMessage(INPUT,"请设置输入私人密码。").setButton("确定","取消").setOnDialogClickListener(new CustomDialog.OnDialogClickListener() {
            @Override
            public void click(int index, Dialog view, String password1, String password2) {
                switch(index){
                    case R.id.sure_btn:
                        if(noteDB.findPasswordData().get(0).getText().equals(password1)){
                            isJion = true;
                            type = 1;
                            initData();
                        }else{
                            Toast.show(MainActivity.this,"密码错误！", 1500);
                        }
                        break;
                    case R.id.cancel_btn:
                        Toast.show(MainActivity.this,"操作已取消！", 1500);
                        break;
                }
            }
        }).show();
    }

    private void setPassword(){
        new CustomDialog(this).setTitle("提示").setMessage(INPUT,"请设置私人密码。").setButton("确定","取消").setOnDialogClickListener(new CustomDialog.OnDialogClickListener() {
            @Override
            public void click(int index, Dialog view, String password1, String password2) {
                switch(index){
                    case R.id.sure_btn:
                        noteDB.savePassword("私人空间",password1);
                        type = 1;
                        initData();
                        break;
                    case R.id.cancel_btn:
                        Toast.show(MainActivity.this,"操作已取消！", 1500);
                        break;
                }
            }
        }).show();
    }

//    private void setTip1(){
//        new CustomDialog(this).setTitle("密码提示").setMessage(PASSWORDTIP,"为了防止忘记密码，请为秘密空间1设置密码提示。").setButton("确定","取消").setOnDialogClickListener(new CustomDialog.OnDialogClickListener() {
//            @Override
//            public void click(int index, Dialog view, String password1, String password2) {
//                switch(index){
//                    case R.id.sure_btn:
//                        noteDB.addPassword(password1, "第一个私人空间");
//                        if(noteDB.findPasswordData().size()>1){
//
//                        }else{
//                            initData();
//                        }
//                        break;
//                    case R.id.cancel_btn:
//                        Toast.show(MainActivity.this,"操作已取消！", 1500);
//                        break;
//                }
//            }
//        }).show();
//    }

//    private void setTip2(){
//        new CustomDialog(this).setTitle("密码提示").setMessage(PASSWORDTIP,"为了防止忘记密码，请为秘密空间2设置密码提示。").setButton("确定","取消").setOnDialogClickListener(new CustomDialog.OnDialogClickListener() {
//            @Override
//            public void click(int index, Dialog view, String password1, String password2) {
//                switch(index){
//                    case R.id.sure_btn:
//                        noteDB.addPassword(password1, "第二个私人空间");
//                        initData();
//                        break;
//                    case R.id.cancel_btn:
//                        Toast.show(MainActivity.this,"操作已取消！", 1500);
//                        break;
//                }
//            }
//        }).show();
//    }
}
