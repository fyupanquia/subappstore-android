package com.sise.subappstore;

import com.sise.subappstore.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class FragmentMaps extends Fragment implements OnClickListener{
	View rootView;
	public FragmentMaps(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
         rootView = inflater.inflate(R.layout.activity_fragment_maps, container, false);
        ImageButton btn = (ImageButton) rootView.findViewById(R.id.btnEncuentranos);
        btn.setOnClickListener(this);
        return rootView;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnEncuentranos) {
			Intent intent = new Intent(rootView.getContext(),GPSActivity.class);
			startActivity(intent);
	}
	}
}
