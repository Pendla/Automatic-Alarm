package com.pendla.googlecalendaralarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.AccountPicker;
import com.pendla.googlecalendaralarm.clock.ClockPresenter;
import com.pendla.googlecalendaralarm.clock.ClockView;

/**
 * Created by Simon on 2014-11-20.
 *
 * This class is used when first starting the app, making sure that the user setups the application
 * correctly. Since we need to know what account to use for example. This could be expanded to
 * a installation process of sort.
 */
public class SetupActivity extends Activity {

    private boolean setupAccount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Make the user pick an account.
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"}, false, null, null, null, null);
        startActivityForResult(intent, ClockPresenter.REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //User has picked an account, save it in shared preferences
        if(requestCode == ClockPresenter.REQUEST_CODE_PICK_ACCOUNT){
            if(resultCode == RESULT_OK){
                //TODO Save accountname is shared preferences.
                setupAccount = true;
            }
        }

        if(doneSetup()){
            Intent startApplication = new Intent(this, ClockView.class);
            startActivity(startApplication);
        }
    }

    public boolean doneSetup(){
        return setupAccount;
    }
}
