package com.sise.subappstore;

import com.sise.subappstore.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class FragmentSubastar extends Fragment implements OnClickListener{

public FragmentSubastar(){}
	View rootView;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.activity_fragment_subastar, container, false);
        ImageButton btn = (ImageButton) rootView.findViewById(R.id.btnIniciaraSubastar);
        btn.setOnClickListener(this);
        return rootView;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btnIniciaraSubastar) {
			Intent intent = new Intent(rootView.getContext(),RegistrarSubasta.class);
			startActivity(intent);
		}
	}
}
