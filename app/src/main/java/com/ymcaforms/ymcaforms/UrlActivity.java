package com.ymcaforms.ymcaforms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class UrlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Branch init
        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // option 1: log data
                    Log.i("BRANCH SDK", referringParams.toString());
                    Intent i=new Intent(UrlActivity.this,User.class);
                    try {
                        i.putExtra("user_id",referringParams.get("key1").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        i.putExtra("form_id",referringParams.get("key2").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    startActivity(i);
                    finish();

                } else {
                    Log.i("BRANCH SDK", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);

        JSONObject sessionParams = Branch.getInstance().getLatestReferringParams();

// first
        JSONObject installParams = Branch.getInstance().getFirstReferringParams();
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }
}
