package com.qsqy.notebook.sdk.textview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by CK on 2017/11/23.
 */

public class FontTextView extends TextView {


    public FontTextView(Context context) {
        super(context);
        init(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        this.setTypeface(FontManager.getTypeface(context,FontManager.FONTAWESOME));
    }
}
