package ch.panter.pan.cake;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class AuthenticationActivity extends FragmentActivity
									implements AccountNameDialogFragment.AccountNameDialogListener{
		
	ArrayList<Account> googleAccounts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authentication);

		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	public void clickAuthenticate(View view) {
		AccountManager am = AccountManager.get(this);
		googleAccounts = new ArrayList<Account>(Arrays.asList(am.getAccountsByType("com.google")));
		
		if (googleAccounts.isEmpty()) {
			Context context = getApplicationContext();
			CharSequence text = "No Google accounts found";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			return;
		}
		
		ArrayList<String> googleAccountNames = new ArrayList<String>();
		for (Account googleAccount : googleAccounts) {
			googleAccountNames.add(googleAccount.name);
		}
		
		showAccountsDialog(googleAccountNames);
	}

	private void showAccountsDialog(ArrayList<String> googleAccountNames) {
        Bundle arguments = new Bundle();
        arguments.putStringArrayList("googleAccountNames", googleAccountNames);

		FragmentManager fm = getSupportFragmentManager();
        AccountNameDialogFragment accountNameDialog = new AccountNameDialogFragment();
        accountNameDialog.setArguments(arguments);
        accountNameDialog.show(fm, "fragment_account_name");
	}
	
	private void authenticateAccount(Account account) {
		AccountManager am = AccountManager.get(this);
		Bundle options = new Bundle();
		
		am.getAuthToken(
				account,                // Account retrieved using getAccountsByType()
				"Manage your entries, view project and task names",            // Auth scope
				options,                        // Authenticator-specific options
				this,                           // Your activity
				new OnTokenAcquired(),          // Callback called when a token is successfully acquired
				new Handler(new OnError()));    // Callback called if an error occurs	
	}
	
	private class OnError implements Callback {

		@Override
		public boolean handleMessage(Message arg0) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {

		}
	}

	private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
		@Override
		public void run(AccountManagerFuture<Bundle> result) {

			// Get the result of the operation from the AccountManagerFuture.
			Bundle bundle = null;
			Intent launch = null;
			try {
				bundle = result.getResult();
				launch = (Intent) result.getResult().get(AccountManager.KEY_INTENT);
			} catch (OperationCanceledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AuthenticatorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (launch != null) {
				startActivityForResult(launch, 0);
				return;
			}

			if (bundle == null) {
				return;
			}
			// The token is a named value in the bundle. The name of the value
			// is stored in the constant AccountManager.KEY_AUTHTOKEN.
			String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
	        SharedPreferences settings = getSharedPreferences(
	        		getString(R.string.preference_file_key), Context.MODE_PRIVATE);
	        SharedPreferences.Editor editor = settings.edit();
	        editor.putString("authToken", token);
	        editor.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDialogChoice(int which) {
		authenticateAccount(googleAccounts.get(which));
	}
}
