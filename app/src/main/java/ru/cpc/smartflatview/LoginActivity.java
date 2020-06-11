package ru.cpc.smartflatview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
{
    public static abstract class SFUnlocker
    {
        public abstract void Unlock(boolean bUnlock);
    }

    public static SFUnlocker s_pUnlocker;

    public static String s_sTrueCode;

    // UI references.
    private EditText mCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        mCodeView = (EditText) findViewById(R.id.code);
        mCodeView.setText("");
//        mCodeView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });

        Button mEmailSignInButton = (Button) findViewById(R.id.enter_button);
        if (mEmailSignInButton != null)
        {
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
        }

        Button mCancelButton = (Button) findViewById(R.id.cancel_button);
        if (mCancelButton != null)
        {
            mCancelButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    s_pUnlocker.Unlock(false);
                    finish();
                }
            });
        }

        View mLoginFormView = findViewById(R.id.login_form);

        mCodeView.requestFocus();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        //Toast.makeText(getApplicationContext(), "Back Pressed", Toast.LENGTH_SHORT).show();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mCodeView.setError(null);

        // Store values at the time of the login attempt.
        String code = mCodeView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(code)) {
            mCodeView.setError(getString(R.string.error_incorrect_password));
            focusView = mCodeView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            s_pUnlocker.Unlock(true);
            finish();
        }
    }

    private boolean isPasswordValid(String password) {

        return password.equals(s_sTrueCode);
    }
}

