package com.hallmark.yamba;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


public class StatusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = "yamba.StatusFragment";
    private EditText editStatus;
    private TextView textCount;
    private Button btnTweet;
    private int defaultTextColor;
    private String userName = "student";
    private String password = "password";
    private String serverUrl = "http://yamba.newcircle.com/api";


    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_status, container, false);

        editStatus = (EditText) view.findViewById(R.id.editStatus);
        btnTweet = (Button) view.findViewById(R.id.btnTweet);
        textCount = (TextView) view.findViewById(R.id.textCount);
        defaultTextColor = textCount.getTextColors().getDefaultColor();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userName = prefs.getString("userName", userName);
        password = prefs.getString("password", password);
        serverUrl = prefs.getString("serverUrl", serverUrl);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = editStatus.getText().toString();
                Log.d(TAG, "Status to send" + status);
                new PostTask().execute(status);
            }
        });



        return view;
    }

    private final class PostTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            String status = params[0];
            String result = "Unknown Result!";

            try {
                YambaClient yamba = new YambaClient(userName, password, serverUrl);
                yamba.postStatus(status);
                Log.d(TAG, "Status Sent: " + status);
                result = "Status Sent: " + status;
            } catch (YambaClientException e) {
                Log.e(TAG, "Yambda Exception: " +e.getMessage());
                e.printStackTrace();
                result = "Yambda Exception: " + e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(StatusFragment.this.getActivity(), result, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
