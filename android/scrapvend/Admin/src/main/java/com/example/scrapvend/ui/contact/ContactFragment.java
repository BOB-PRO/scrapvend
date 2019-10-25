package com.example.scrapvend.ui.contact;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.scrapvend.Adapters.ContactAdapter;
import com.example.scrapvend.DatabaseConnect.MySqlConnector;
import com.example.scrapvend.Models.ContactModel;
import com.example.scrapvend.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ContactFragment extends Fragment {

    public View rootView;

    TextView name, subject;
    ContactAdapter padapter;
    ContactModel pmodel;
    ListView listview;
    Context context;
    ArrayList<ContactModel> arr = new ArrayList<>();

    ContactViewModel contactViewModel;


    private static final String TAG = "MyActivity";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "inside shareFragment.java");

         contactViewModel=
                ViewModelProviders.of(this).get(ContactViewModel.class);
        View root = inflater.inflate(R.layout.fragment_contact, container, false);
        listview = (ListView) root.findViewById(R.id.list_view02);
        name = (TextView) root.findViewById(R.id.author_name);
        subject = (TextView) root.findViewById(R.id.subject);

        Button button = (Button) root.findViewById(R.id.button);

        new MyTask().execute();

        Log.d(TAG, "before onClickListener");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "inside onClickListener");
                new MyTask().execute();
            }
        });

        context = this.getContext();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "initiating Intent");
                Intent intent = new Intent(getActivity(),DetailView.class);
                startActivity(intent);
            }
            });


        return root;
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            MySqlConnector connection = new MySqlConnector();
            Connection conn = connection.getMySqlConnection();
            try {
                Statement statement = conn.createStatement();
                ResultSet results = statement.executeQuery("SELECT * FROM `contact_us`;");

                while (results.next()) {
                    Log.d(TAG, results.getString(1) + results.getString(2));
                    pmodel = new ContactModel(results.getString(1), results.getString(4));
                    arr.add(pmodel);
                }


            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        protected void onPostExecute(Void aVoid)
        {
            Log.d(TAG, "inside onpostexecute");
            padapter = new ContactAdapter(context, R.layout.contact_list_layout, arr);
            listview.setAdapter(padapter);

            super.onPostExecute(aVoid);
        }
    }

}
