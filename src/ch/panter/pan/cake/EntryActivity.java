package ch.panter.pan.cake;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;

public class EntryActivity extends FragmentActivity {

	private Chronometer chronometer;
	Boolean timerStarted = false;
	Button timerStartButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entries);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		chronometer = (Chronometer) findViewById(R.id.entries_timer);
		timerStartButton = (Button) findViewById(R.id.entries_start);
		
		View.OnClickListener goButtonListener = new OnClickListener() {
			public void onClick(View v) {
				if (timerStarted) {
					chronometer.stop();
					timerStarted = false;
					timerStartButton.setText(R.string.entries_start);
				} else {
					chronometer.start();
					timerStarted = true;
					timerStartButton.setText(R.string.entries_stop);
				}
			}
		};
		
		timerStartButton.setOnClickListener(goButtonListener);
	}
}
