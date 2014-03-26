/*
 * GuideFragment
 *
 * Version 1.0
 *
 * 2014-03-25
 *
 * Copyright notice
 */
package com.jason.diner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 向导界面
 * @author Jason
 *
 */
public class GuideFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.guide_fragment,
        		container, false);
        
        ImageView guideImage = 
        		(ImageView) rootView.findViewById(R.id.guideImage);
        guideImage.setBackgroundResource(R.drawable.drawer_shadow);
        
        
        return rootView;
    }
    
}
