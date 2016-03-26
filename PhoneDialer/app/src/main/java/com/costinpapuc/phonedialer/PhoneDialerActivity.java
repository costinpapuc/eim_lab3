package com.costinpapuc.phonedialer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class PhoneDialerActivity extends AppCompatActivity {
    final public static int CONTACTS_MANAGER_REQUEST_CODE = 1;
    final public static int[] buttonIds = {
            R.id.number_0_button,
            R.id.number_1_button,
            R.id.number_2_button,
            R.id.number_3_button,
            R.id.number_3_button,
            R.id.number_4_button,
            R.id.number_5_button,
            R.id.number_6_button,
            R.id.number_7_button,
            R.id.number_8_button,
            R.id.number_9_button,
            R.id.star_button,
            R.id.pound_button
    };
    private EditText phoneNumberEditText = null;
    private NumberButtonClickListener numberButtonClickListener = new NumberButtonClickListener();
    private BackspaceButtonClickListener backspaceButtonClickListener = new BackspaceButtonClickListener();
    private CallButtonClickListener callButtonClickListener = new CallButtonClickListener();
    private HangupButtonClickListener hangupButtonClickListener = new HangupButtonClickListener();
    private ContactsButtonClickListener contactsButtonClickListener = new ContactsButtonClickListener();


    private class ContactsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String phoneNumber = phoneNumberEditText.getText().toString();
            if (phoneNumber.length() > 0) {
                Intent intent = new Intent("com.costinpapuc.contactsmanager.intent.action.ContactsManagerActivity");
                intent.putExtra("com.costinpapuc.contactsmanager.PHONE_NUMBER_KEY", phoneNumber);
                startActivityForResult(intent, CONTACTS_MANAGER_REQUEST_CODE);
            } else {
                Toast.makeText(getApplication(), getResources().getString(R.string.phone_number_error), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class NumberButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            phoneNumberEditText.setText(phoneNumberEditText.getText().toString()
                    + ((Button)view).getText().toString());
        }
    }

    private class BackspaceButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String phoneNumber = phoneNumberEditText.getText().toString();
            if (phoneNumber.length() > 0) {
                phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 1);
                phoneNumberEditText.setText(phoneNumber);
            }
        }
    }

    private class CallButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String phoneNumber = phoneNumberEditText.getText().toString();
            Intent intent = new Intent(Intent.ACTION_CALL);
            phoneNumber = phoneNumber.replace("#", "%23");
            Log.d("log", phoneNumber);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            } else {
                ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        }
    }

    private class HangupButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            phoneNumberEditText.setText("");
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dialer);
        phoneNumberEditText = (EditText)findViewById(R.id.phone_number_edit_text);
        Button button;
        for (int buttonId : buttonIds) {
            button = (Button)findViewById(buttonId);
            button.setOnClickListener(numberButtonClickListener);
        }
        ImageButton backspaceImageButton = (ImageButton)findViewById(R.id.backspace_button);
        backspaceImageButton.setOnClickListener(backspaceButtonClickListener);
        ImageButton callImageButton = (ImageButton)findViewById(R.id.call_button);
        callImageButton.setOnClickListener(callButtonClickListener);
        ImageButton hangupImageButton = (ImageButton)findViewById(R.id.hangup_button);
        hangupImageButton.setOnClickListener(hangupButtonClickListener);
        ImageButton contactsImageButton = (ImageButton)findViewById(R.id.contacts_button);
        contactsImageButton.setOnClickListener(contactsButtonClickListener);

    }
}
