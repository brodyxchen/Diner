package com.jason.diner;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class AboutFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_fragment, container, false);

        
//        DisplayMetrics dm = new DisplayMetrics();     
//        Document.MainDoc().mainActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);     
//        int screenWidth = dm.widthPixels;     
//        int screenHeight = dm.heightPixels; 
//        Test.info("ActionBar screenWH", "(" +screenWidth + ":" + screenHeight + ")");
//        Rect frame = new Rect();
//        Document.MainDoc().mainActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//        int statusBarHeight = frame.top;
//        Test.info("ActionBar statusBarH", "" +statusBarHeight);
//        int actionBarHeight = Document.MainDoc().mainActivity.getActionBar().getHeight();
//        int contentTop = Document.MainDoc().mainActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
//        int titleBarHeight = contentTop - statusBarHeight;
//        Test.info("ActionBar titleBarHeight", "" +titleBarHeight);
        
        return rootView;
    }
    
}
