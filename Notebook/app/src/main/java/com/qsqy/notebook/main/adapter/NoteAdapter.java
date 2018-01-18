package com.qsqy.notebook.main.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qsqy.notebook.MainActivity;
import com.qsqy.notebook.R;
import com.qsqy.notebook.main.entity.NoteBook;
import com.qsqy.notebook.sdk.db.NoteDB;
import com.qsqy.notebook.sdk.dialog.CustomDialog;
import com.qsqy.notebook.sdk.textview.FontTextView;
import com.qsqy.notebook.sdk.toast.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.qsqy.notebook.sdk.dialog.CustomDialog.INPUT;
import static com.qsqy.notebook.sdk.dialog.CustomDialog.TIP;

/**
 * Created by CK on 2018/1/8.
 */

public class NoteAdapter extends BaseAdapter{
    private Context context;
    private int position;//长按的item
    private List<NoteBook> datas = new ArrayList<NoteBook>();
    private List<NoteBook> list_delete = new ArrayList<NoteBook>();// 需要删除的数据
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    private boolean isMultiSelect = false;// 是否处于多选状态
    private boolean isAllSelect = false;// 是否全选状态
    private HashMap<Integer, Boolean> isChecked;// 用来记录是否被选中

    public NoteAdapter(Context context, boolean isMultiSelect, boolean isAllSelect,List<NoteBook> datas, int position, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener){
        this.context = context;
        this.isMultiSelect = isMultiSelect;
        this.isAllSelect = isAllSelect;
        this.datas = datas;
        this.position = position;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;

        isChecked = new HashMap<Integer, Boolean>();
        list_delete.clear();
        if (isAllSelect) {
            for (int i = 0; i < datas.size(); i++) {
                isChecked.put(i, true);
                list_delete.add(datas.get(i));
            }
        } else {
            for (int i = 0; i < datas.size(); i++) {
                isChecked.put(i, false);
            }
        }
        // 如果长按Item，则设置长按的Item中的CheckBox为选中状态
        if (isMultiSelect && position >= 0) {
            isChecked.put(position, true);
            list_delete.add(datas.get(position));
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        HolderView holderView = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_view_note,null);
            holderView = new HolderView();
            holderView.layoutRl = (RelativeLayout)view.findViewById(R.id.layout_rl);
            holderView.titleTv = (TextView)view.findViewById(R.id.title_tv);
            holderView.delCb = (CheckBox) view.findViewById(R.id.del_cb);
            holderView.rightTv = (FontTextView) view.findViewById(R.id.right_tv);
            view.setTag(holderView);
        }else{
            holderView = (HolderView) view.getTag();
        }

        // 根据position设置CheckBox是否可见，是否选中
        holderView.delCb.setChecked(isChecked.get(i));
        holderView.rightTv.setVisibility(!isMultiSelect ? View.VISIBLE : View.GONE);
        holderView.delCb.setVisibility(isMultiSelect ? View.VISIBLE : View.GONE);
        holderView.titleTv.setText(datas.get(i).getTitle()!=null && !datas.get(i).getTitle().equals("") ? datas.get(i).getTitle() : datas.get(i).getTime());

        final int position = i;
        final HolderView finalHolderView = holderView;
        holderView.delCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finalHolderView.delCb.isChecked()) {
                    list_delete.add(datas.get(position));
                } else {
                    list_delete.remove(datas.get(position));
                }
                onItemClickListener.onItemClick(view,position,true,list_delete);
            }
        });
        holderView.layoutRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMultiSelect) {
                    if (finalHolderView.delCb.isChecked()) {
                        finalHolderView.delCb.setChecked(false);
                        list_delete.remove(datas.get(position));
                    } else {
                        finalHolderView.delCb.setChecked(true);
                        list_delete.add(datas.get(position));
                    }
                    onItemClickListener.onItemClick(view,position,true,list_delete);
                }else{
                    onItemClickListener.onItemClick(view,position,false,null);
                }
            }
        });
        holderView.layoutRl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isMultiSelect = true;
                list_delete.clear();
                list_delete.add(datas.get(position));
                finalHolderView.delCb.setChecked(true);
                onItemLongClickListener.onItemLongClick(view, position, list_delete);
//                new CustomDialog(context).setTitle("提示").setMessage(TIP,"确定删除："+(datas.get(postion).getTitle()!=null ? datas.get(postion).getTitle() : datas.get(postion).getTime())).setButton("确定","取消").setOnDialogClickListener(new CustomDialog.OnDialogClickListener() {
//                    @Override
//                    public void click(int index, Dialog view, String password1, String password2) {
//                        switch(index){
//                            case R.id.sure_btn:
//                                NoteDB noteDB = new NoteDB(context);
//                                Log.d("api-","datas.get(postion).getId():"+datas.get(postion).getId());
//                                noteDB.deleteNote(datas.get(postion).getId());
//                                datas = noteDB.findData(type);
//                                notifyDataSetChanged();
//                                break;
//                            case R.id.cancel_btn:
//                                Toast.show((Activity) context,"操作已取消！", 1500);
//                                break;
//                        }
//                    }
//                }).show();
                return true;
            }
        });
        return view;
    }

    class HolderView{
        private RelativeLayout layoutRl;
        private TextView titleTv;
        private CheckBox delCb;
        private FontTextView rightTv;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int index, boolean isChecked, List<NoteBook> list_delete);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position, List<NoteBook> list_delete);
    }

}
