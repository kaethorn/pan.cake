package ch.panter.pan.cake;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AccountNameDialogFragment extends DialogFragment {

    public AccountNameDialogFragment() { }
    
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface AccountNameDialogListener {
        public void onDialogChoice(int which);
    }
    
    // Use this instance of the interface to deliver action events
    AccountNameDialogListener mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AccountNameDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	ArrayList<String> googleAccountNames = (ArrayList<String>) getArguments().getStringArrayList("googleAccountNames");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.choose_account)
			.setItems(googleAccountNames.toArray(new String[googleAccountNames.size()]), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mListener.onDialogChoice(which);
				}
			});
		return builder.create();
	}
}
