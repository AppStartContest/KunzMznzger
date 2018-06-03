package com.ltei.kunzmznzger.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ListLinearLayout extends LinearLayout {


    public interface ViewCreator {
        View createView(Object item, int itemPosition);
    }



    private ArrayList<?> array = null;
    private ViewCreator viewCreator = null;

    public ListLinearLayout(Context context) {
        super(context);
    }
    public ListLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public void init(ArrayList<?> array, ViewCreator creator) {
        setViewCreator(creator);
        setArray(array);
    }
    public void setViewCreator(ViewCreator creator) {
        this.viewCreator = creator;
    }
    public void setArray(ArrayList<?> array) {
        this.array = array;
        notifyArrayChange();
    }

    public void notifyArrayChange() {
        removeAllViews();
        if (viewCreator != null) {
            int itemPosition = 0;
            for (Object item : array) {
                addView(viewCreator.createView(item, itemPosition));
                itemPosition += 1;
            }
        }
    }

}
