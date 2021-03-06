package com.example.scrapvend.ui.pickupperson;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.scrapvend.Adapters.PickuppersonAdapter;
import com.example.scrapvend.DatabaseConnect.MySqlConnector;
import com.example.scrapvend.Models.PickupPersonModel;
import com.example.scrapvend.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PickuppersonFragment extends Fragment {
    public View rootView;
    ListView listview;
//    TextView t1, t2, t3;
//    ImageView img;
    PickuppersonAdapter padapter;
    PickupPersonModel pickupPersonModel;
    ArrayList<PickupPersonModel> arr = new ArrayList<>();
    private PickuppersonViewModel toolsViewModel;
    private FloatingActionButton floatingActionButtonAddPickupPerson;
    Context context;
    private static final String TAG = "MyActivity";

    public View  onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(PickuppersonViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pickupperson, container, false);

        floatingActionButtonAddPickupPerson = (FloatingActionButton) root.findViewById(R.id.floatingActionButtonAddPickupPerson);

        floatingActionButtonAddPickupPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAddItemFragment();
            }
        });


        listview = (ListView) root.findViewById(R.id.list_view);
//        t1 = (TextView) root.findViewById(R.id.textView9);
//        t2 = (TextView) root.findViewById(R.id.textView10);
//        t3 = (TextView) root.findViewById(R.id.textView11);
//        img = (ImageView) root.findViewById(R.id.imageView2);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "onclick list 1"+position+id);

                 pickupPersonModel = arr.get(position);
                Log.d(TAG, "id to transfer : "+pickupPersonModel.getId()+pickupPersonModel.getAadhar_no()+pickupPersonModel.getSalary());
                pickupPersonModel = arr.get(position);
                Log.d(TAG, "id to transfer : "+ pickupPersonModel.getId()+ pickupPersonModel.getAadhar_no()+ pickupPersonModel.getSalary());
                 pickupPersonModel = arr.get(position);
                Log.d(TAG, "id to transfer : "+pickupPersonModel.getId()+pickupPersonModel.getAadhar_no()+pickupPersonModel.getSalary());
                Intent intent = new Intent(getActivity(),UpdatePickupPerson.class);
                intent.putExtra("GETName",pickupPersonModel.getName());
                intent.putExtra("GETRating",pickupPersonModel.getRating());
                intent.putExtra("GETAdhaar",pickupPersonModel.getAadhar_no());
                intent.putExtra("GETSalary",pickupPersonModel.getSalary());
                intent.putExtra("GETId",pickupPersonModel.getId());
                //Convert to byte array
                Log.d(TAG, pickupPersonModel.getName());
                Bitmap bitmap = pickupPersonModel.getBitmapImage();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("GETImage",byteArray);
                startActivity(intent);

            }
        });


        new PickupPersonTask().execute();
        Log.d(TAG, "back to oncreate again");
        context = this.getContext();
        return root;
    }

    private class PickupPersonTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            MySqlConnector connection = new MySqlConnector();
            Connection conn = connection.getMySqlConnection();
            try {
                Statement statement = conn.createStatement();
                ResultSet results = statement.executeQuery("SELECT * FROM `pickup_person_details` WHERE View_value = 1;");

                while (results.next()) {
                    Blob blob = results.getBlob(8);
                    Bitmap bitmap;
                    try {
                        int blobLength = (int) blob.length();
                        byte[] blobAsBytes = blob.getBytes(1,blobLength);
                        bitmap = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
                        pickupPersonModel = new PickupPersonModel(results.getString(1),results.getString(2), results.getString(3),results.getBlob(8),results.getString(4),results.getString(5),bitmap);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.d(TAG, results.getString(1)+results.getString(2));
                    arr.add(pickupPersonModel);
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
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
            padapter = new PickuppersonAdapter(context, R.layout.add_item_pickupperson, arr);
            listview.setAdapter(padapter);

            super.onPostExecute(aVoid);
        }
    }

    public void openAddItemFragment(){
        Intent intent = new Intent(getActivity(), AddPickupPerson.class);
        startActivity(intent);
    }
}

