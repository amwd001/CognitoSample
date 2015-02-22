package edu.neu.madcourse.cognitosample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import edu.neu.madcourse.cognito.core.CognitoKeyValue;


/**
 * Created by kevin on 2/8/15.
 */
public class CognitoKeyValueAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CognitoKeyValue> data;

    public CognitoKeyValueAdapter(Context context, ArrayList<CognitoKeyValue> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public CognitoKeyValue getItem(int position) {
        return data == null || position >= data.size() ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data == null || position >= data.size() ? -1 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        TextView tvKey = null;
        TextView tvValue = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_cognito_keyvalue, null);
        } else
            view = convertView;
        tvKey = (TextView) view.findViewById(R.id.tv_item_cognito_key);
        tvValue = (TextView) view.findViewById(R.id.tv_item_cognito_value);

        CognitoKeyValue keyValue = getItem(position);
        tvKey.setText(keyValue.getKey());
        tvValue.setText(keyValue.getValue());

        return view;
    }
}
