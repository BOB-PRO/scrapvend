package com.example.scrapvend.ui.pickupinfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.example.scrapvend.DatabaseConnect.MySqlConnector;
import com.example.scrapvend.Models.PickupinfoModel;
import com.example.scrapvend.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import androidx.appcompat.app.AppCompatActivity;

public class PickupInfoCompletedPickupView extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ViewHolder viewHolder = new ViewHolder();

    PickupinfoModel pickupinfoModel = new PickupinfoModel();
    String GET_PICKUPLIST_FLAG;
    String query;
    final String TAG = "MyPickupinfoView";
    String[] pickupinfoCategory;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pickupinfo_detailed_completed_pickup_view);

        Log.e(TAG, "From PickupinfoView");
        viewHolder.setUserNameTextView((TextView) findViewById(R.id.user_name));
        viewHolder.setBookingIdTextView((TextView) findViewById(R.id.booking_id));
        viewHolder.setAddressTextView((TextView) findViewById(R.id.Address));
        viewHolder.setCustomerNameTextView((TextView) findViewById(R.id.customer_name));
        viewHolder.setAssignedDateTimeTextView((TextView) findViewById(R.id.booking_assign_date_time));
        viewHolder.setBookingDateTimeTextView((TextView) findViewById(R.id.booked_date_time));
        viewHolder.setPaymentModeTextView((TextView) findViewById(R.id.payment_mode));
        viewHolder.setPaymentAmountTextView((TextView) findViewById(R.id.payment_amount));
        viewHolder.setPaymentStatusTextView((TextView) findViewById(R.id.payment_status));
        viewHolder.setScheduledDateTextView((TextView) findViewById(R.id.scheduled_date_time));
        viewHolder.setPickupStatusTextView((TextView) findViewById(R.id.pickup_status));
        viewHolder.setPickupPersonIdTextView((TextView) findViewById(R.id.pickup_person_id));
        viewHolder.setPickupPersonNameTextView((TextView) findViewById(R.id.pickup_person_name));
        viewHolder.setPickupDateTimeTextView((TextView) findViewById(R.id.pickup_date_time));
        viewHolder.setPickupRatingTextView((TextView) findViewById(R.id.pickup_rating));

        Bundle bundle = getIntent().getExtras();
        pickupinfoModel.setAddress(bundle.getString("ADDRESS"));
        pickupinfoModel.setBookingId(bundle.getString("BOOKING_ID"));
        GET_PICKUPLIST_FLAG = bundle.getString("GET_PICKUPLIST_FLAG");
        pickupinfoModel.setPickupStatus(GET_PICKUPLIST_FLAG);

        viewHolder.getAddressTextView().setText(pickupinfoModel.getAddress());
        viewHolder.getBookingIdTextView().setText(pickupinfoModel.getBookingId());

        pickupinfoCategory = getResources().getStringArray(R.array.pickupinfo_category_name);

        Button backButton = (Button)this.findViewById(R.id.done);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new PickupInfoViewTask().execute();

    }

    private class PickupInfoViewTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {


            MySqlConnector connection = new MySqlConnector();
            Connection conn = connection.getMySqlConnection();
            try {
                Statement statement = conn.createStatement();


                query = "SELECT booking_details.Booking_date_time, booking_details.Scheduled_pickup_date_time, " +
                        "user_details.Username, booking_details.Pickup_date_time, booking_details.Pickup_status, " +
                        "user_details.Name, booking_assigned.Assigned_date , booking_assigned.Pickup_rating, " +
                        "pickup_person_details.Pickup_person_id, pickup_person_details.Name, " +
                        "payment_details.Payment_mode, payment_details.Payment_amount " +
                        "FROM booking_details " +
                        "INNER JOIN user_details ON user_details.User_id = booking_details.User_id " +
                        "INNER JOIN booking_assigned ON booking_assigned.Booking_id = booking_details.Booking_id " +
                        "INNER JOIN payment_details ON booking_details.Booking_id = payment_details.Booking_id " +
                        "INNER JOIN pickup_person_details ON booking_assigned.Pickup_person_id = pickup_person_details.Pickup_person_id " +
                        "where booking_details.Booking_id =" + pickupinfoModel.getBookingId();

                Log.d(TAG, "query == "+ query);

                ResultSet results = statement.executeQuery(query);

                while (results.next()){
                    Log.d(TAG, results.getString(1) + results.getString(2));
                    pickupinfoModel.setBookedDate(results.getString(1));
                    pickupinfoModel.setSchuduleDate(results.getString(2));
                    pickupinfoModel.setUsername(results.getString(3));
                    pickupinfoModel.setPickupDate(results.getString(4));
                    pickupinfoModel.setPaymentStatus(results.getString(5));
                    pickupinfoModel.setCustomerName(results.getString(6));
                    pickupinfoModel.setAssignedDate(results.getString(7));
                    pickupinfoModel.setPickupRating(results.getString(8));
                    pickupinfoModel.setPickupPersonId(results.getString(9));
                    pickupinfoModel.setPickupPersonName(results.getString(10));
                    pickupinfoModel.setPaymentMode(results.getString(11));
                    pickupinfoModel.setPaymentAmount(results.getString(12));

                    Log.d(TAG, "name"+pickupinfoModel.getUsername());
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            textViewFiller(GET_PICKUPLIST_FLAG);

        }
    }

    private void textViewFiller(String get_pickuplist_flag)    {

        viewHolder.getBookingDateTimeTextView().setText(pickupinfoModel.getBookedDate());
        viewHolder.getScheduledDateTextView().setText(pickupinfoModel.getSchuduleDate());
        viewHolder.getUserNameTextView().setText(pickupinfoModel.getUsername());
        viewHolder.getPaymentStatusTextView().setText(pickupinfoModel.getPaymentStatus());
        viewHolder.getCustomerNameTextView().setText(pickupinfoModel.getCustomerName());
        viewHolder.getAssignedDateTimeTextView().setText(pickupinfoModel.getAssignedDate());
        viewHolder.getPickupRatingTextView().setText(pickupinfoModel.getPickupRating());
        viewHolder.getPickupPersonIdTextView().setText(pickupinfoModel.getPickupPersonId());
        viewHolder.getPickupPersonNameTextView().setText(pickupinfoModel.getPickupPersonName());
        viewHolder.getPickupStatusTextView().setText(pickupinfoModel.getPickupStatus());
        viewHolder.getPickupDateTimeTextView().setText(pickupinfoModel.getPickupDate());
        viewHolder.getPaymentModeTextView().setText(pickupinfoModel.getPaymentMode());
        viewHolder.getPaymentAmountTextView().setText(pickupinfoModel.getPaymentAmount());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class ViewHolder{
        TextView userNameTextView;
        TextView bookingIdTextView;
        TextView customerNameTextView;
        TextView addressTextView;
        TextView ScheduledDateTextView;
        TextView bookingDateTimeTextView;
        TextView assignedDateTimeTextView;
        TextView pickupStatusTextView;
        TextView pickupRatingTextView;

        public TextView getPickupRatingTextView() {
            return pickupRatingTextView;
        }

        public void setPickupRatingTextView(TextView pickupRatingTextView) {
            this.pickupRatingTextView = pickupRatingTextView;
        }

        TextView pickupPersonNameTextView;
        TextView pickupPersonIdTextView;
        TextView paymentStatusTextView;
        TextView paymentAmountTextView;
        TextView paymentModeTextView;
        TextView pickupDateTimeTextView;

        public TextView getPaymentAmountTextView() {
            return paymentAmountTextView;
        }

        public void setPaymentAmountTextView(TextView paymentAmountTextView) {
            this.paymentAmountTextView = paymentAmountTextView;
        }

        public TextView getScheduledDateTextView() {
            return ScheduledDateTextView;
        }

        public void setScheduledDateTextView(TextView scheduledDateTextView) {
            ScheduledDateTextView = scheduledDateTextView;
        }

        public TextView getBookingDateTimeTextView() {
            return bookingDateTimeTextView;
        }

        public void setBookingDateTimeTextView(TextView bookingDateTimeTextView) {
            this.bookingDateTimeTextView = bookingDateTimeTextView;
        }

        public TextView getAssignedDateTimeTextView() {
            return assignedDateTimeTextView;
        }

        public void setAssignedDateTimeTextView(TextView assignedDateTimeTextView) {
            this.assignedDateTimeTextView = assignedDateTimeTextView;
        }

        public TextView getPickupStatusTextView() {
            return pickupStatusTextView;
        }

        public void setPickupStatusTextView(TextView pickupStatusTextView) {
            this.pickupStatusTextView = pickupStatusTextView;
        }

        public TextView getPickupPersonNameTextView() {
            return pickupPersonNameTextView;
        }

        public void setPickupPersonNameTextView(TextView pickupPersonNameTextView) {
            this.pickupPersonNameTextView = pickupPersonNameTextView;
        }

        public TextView getPickupPersonIdTextView() {
            return pickupPersonIdTextView;
        }

        public void setPickupPersonIdTextView(TextView pickupPersonIdTextView) {
            this.pickupPersonIdTextView = pickupPersonIdTextView;
        }

        public TextView getPaymentStatusTextView() {
            return paymentStatusTextView;
        }

        public void setPaymentStatusTextView(TextView paymentStatusTextView) {
            this.paymentStatusTextView = paymentStatusTextView;
        }

        public TextView getPaymentModeTextView() {
            return paymentModeTextView;
        }

        public void setPaymentModeTextView(TextView paymentModeTextView) {
            this.paymentModeTextView = paymentModeTextView;
        }

        public TextView getPickupDateTimeTextView() {
            return pickupDateTimeTextView;
        }

        public void setPickupDateTimeTextView(TextView pickupDateTimeTextView) {
            this.pickupDateTimeTextView = pickupDateTimeTextView;
        }

        public TextView getUserNameTextView() {
            return userNameTextView;
        }

        public void setUserNameTextView(TextView userNameTextView) {
            this.userNameTextView = userNameTextView;
        }

        public TextView getBookingIdTextView() {
            return bookingIdTextView;
        }

        public void setBookingIdTextView(TextView bookingIdTextView) {
            this.bookingIdTextView = bookingIdTextView;
        }

        public TextView getCustomerNameTextView() {
            return customerNameTextView;
        }

        public void setCustomerNameTextView(TextView customerNameTextView) {
            this.customerNameTextView = customerNameTextView;
        }

        public TextView getAddressTextView() {
            return addressTextView;
        }

        public void setAddressTextView(TextView addressTextView) {
            this.addressTextView = addressTextView;
        }
    }


}
