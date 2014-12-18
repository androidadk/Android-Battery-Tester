package com.mobilemerit.batterychecker;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

public class DialogActivity extends Activity {

	TextView status;
	TextView remark;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setContentView(R.layout.dialog_theme_layout);
		status=(TextView)this.findViewById(R.id.status);
		remark=(TextView)this.findViewById(R.id.remark);
		status.setText(""+getIntent().getStringExtra("status"));
		remark.setText(""+getIntent().getStringExtra("remark"));
		
	}
}
