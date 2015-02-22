package edu.neu.madcourse.cognitosample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.Record;
import com.amazonaws.mobileconnectors.cognito.SyncConflict;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataStorageException;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.cognito.core.CognitoConfigContract;
import edu.neu.madcourse.cognito.core.CognitoDataSetProvider;
import edu.neu.madcourse.cognito.core.CognitoKeyValue;

public class CognitoDevAuthSample extends Activity implements CognitoDataSetProvider.OnProviderInitResultCallback, View.OnClickListener {

    @Override
    protected void onResume() {
        super.onResume();
        CognitoConfigContract contract = new CognitoConfigContract(
                AwsConstants.AWS_ACCOUNT_ID,
                AwsConstants.AWS_IDENTITY_POOL_ID,
                AwsConstants.AWS_UNAUTH_ROLE_ARN,
                AwsConstants.AWS_AUTH_ROLE_ARN,
                AwsConstants.AWS_REGION,
                AwsConstants.AWS_DEVELOPER_CREDENTIAL_KEY,
                AwsConstants.AWS_DEVELOPER_CREDENTIAL_SECRETE,
                AwsConstants.AWS_DEVELOPER_PROVIDER_NAME);
        CognitoDataSetProvider.initialize(this, contract, this);
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, "Successfully init CognitoDatasetProvider", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(String s) {
        Toast.makeText(this, "Failed to init CognitoDatasetProvider", Toast.LENGTH_LONG).show();
    }

    private ListView lvKeyValuePairs = null;
    private EditText etKey = null;
    private EditText etValue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cognito_sample_main);
        lvKeyValuePairs = (ListView) findViewById(R.id.lv_cognito_dev_auth_sample);
        etKey = (EditText) findViewById(R.id.et_cognito_dev_auth_sample_key);
        etValue = (EditText) findViewById(R.id.et_cognito_dev_auth_sample_value);
        findViewById(R.id.btn_cognito_dev_auth_sample_sync).setOnClickListener(this);
        findViewById(R.id.btn_cognito_dev_auth_sample_refresh).setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cleanup();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cognito_dev_auth_sample_sync:
                onSync();
                break;
            case R.id.btn_cognito_dev_auth_sample_refresh:
                onRefresh();
                break;
        }
    }

    private void cleanup() {
    }

    private void onRefresh() {


        CognitoDataSetProvider instance = CognitoDataSetProvider.getInstance();
        if (instance == null) {
            Toast.makeText(this, "CognitoDataSetProvider is null", Toast.LENGTH_LONG).show();
            return;
        }
        Dataset dataset = CognitoDataSetProvider.getInstance().getDataSet(this, AwsConstants.AWS_DATASET_NAME);
        if (dataset == null) {
            Toast.makeText(this, "Dataset is null", Toast.LENGTH_LONG).show();
            return;
        }
        dataset.synchronize(new DatasetSyncCallback());


    }

    private void updateList(ArrayList<CognitoKeyValue> data) {
        CognitoKeyValueAdapter adapter = new CognitoKeyValueAdapter(this, data);
        lvKeyValuePairs.setAdapter(adapter);
        lvKeyValuePairs.invalidate();
    }

    private void onSync() {
        String key = etKey.getText().toString();
        String value = etValue.getText().toString();
        CognitoDataSetProvider instance = CognitoDataSetProvider.getInstance();
        if (instance == null) {
            Toast.makeText(this, "CognitoDataSetProvider is null", Toast.LENGTH_LONG).show();
            return;
        }
        Dataset dataset = CognitoDataSetProvider.getInstance().getDataSet(this, AwsConstants.AWS_DATASET_NAME);
        if (dataset == null) {
            Toast.makeText(this, "Dataset is null", Toast.LENGTH_LONG).show();
            return;
        }
        if (key != null && !key.isEmpty() && value != null && !value.isEmpty()) {
            dataset.put(key, value);
            dataset.synchronize(new DatasetSyncCallback());
        }
    }



    private class DatasetSyncCallback implements Dataset.SyncCallback {

        @Override
        public void onSuccess(final Dataset dataset, final List<Record> records) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CognitoDevAuthSample.this, "Sync successfully", Toast.LENGTH_LONG).show();
                    ArrayList<CognitoKeyValue> data = new ArrayList<CognitoKeyValue>();
                    for (Record record : dataset.getAllRecords())
                        data.add(new CognitoKeyValue(record.getKey(), record.getValue()));
                    updateList(data);
                }
            });
        }

        @Override
        public boolean onConflict(Dataset dataset, List<SyncConflict> syncConflicts) {
            // TODO: Students need to handle conflict here
            return false;
        }

        @Override
        public boolean onDatasetDeleted(Dataset dataset, String s) {
            return false;
        }

        @Override
        public boolean onDatasetsMerged(Dataset dataset, List<String> strings) {
            // TODO: students need to handle merge problem here.
            return false;
        }

        @Override
        public void onFailure(final DataStorageException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CognitoDevAuthSample.this, "Sync failed : " + e.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
