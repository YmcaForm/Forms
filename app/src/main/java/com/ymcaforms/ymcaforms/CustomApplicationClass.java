package com.ymcaforms.ymcaforms;

import android.app.Application;

import io.branch.referral.Branch;

/**
 * Created by Kachucool on 25-11-2017.
 */

public class CustomApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Branch logging for debugging
        Branch.enableLogging();

        // Branch object initialization
        Branch.getAutoInstance(this);
    }
}
