package org.smartregister.chw.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.internal.service.Common;
import com.google.gson.Gson;
import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.utils.FormUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.R;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.anc.util.NCUtils;
import org.smartregister.chw.util.Utils;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.view.activity.BaseProfileActivity;
import org.smartregister.view.customcontrols.CustomFontTextView;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class GeProfileActivity extends BaseProfileActivity {

    //initialize a variable of type CommonPersonObjectClient
     public CommonPersonObjectClient clientDetails;
    @Override
    protected void initializePresenter() {

    }

    //implement the method of setUpViews
    @Override
    protected void setupViews() {
        super.setupViews();

        //calling an intent for this activity
        Intent intent = getIntent();
        //using the intent to pass Extra information of client and casting it to CommonPersonObjectClient dataType
        clientDetails = (CommonPersonObjectClient) intent.getSerializableExtra("client_info");

        //Updating or setting up the views of the Profile
        //setContentView() is the method that is used to link layout and = activity android concept
        CircleImageView imageView = findViewById(R.id.imageview_profile);
        CustomFontTextView txt_view_name = findViewById(R.id.textview_name);
        CustomFontTextView txt_view_sex = findViewById(R.id.textview_detail_one);
        CustomFontTextView txt_view_location = findViewById(R.id.textview_detail_two);
        CustomFontTextView txt_view_id = findViewById(R.id.textview_detail_three);
        Button btnProfileServices = findViewById(R.id.btn_profile_registration_info);


        //pass data to the view
        if (clientDetails != null){

            Integer age = Utils.getAgeFromDate(clientDetails.getColumnmaps().get("dob"));
            String fullName = clientDetails.getColumnmaps().get("first_name") + " " +
                    clientDetails.getColumnmaps().get("middle_name") + " " +
                    clientDetails.getColumnmaps().get("last_name") + " , " + age ;
            String gender = clientDetails.getColumnmaps().get("gender");
            String location  = clientDetails.getColumnmaps().get("village_town");
            String uniqueId = clientDetails.getColumnmaps().get("unique_id");


            //setting text or matching the data to be displayed on the view that is customized
            txt_view_name.setText(fullName);
            txt_view_sex.setText(gender);
            txt_view_location.setText(location);
            txt_view_id.setText(uniqueId);
        }

        //this is to set an image on the profile
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_member));
        //changing text on the button
        btnProfileServices.setText("Record Provision of GE Services");

        //setting an onClickListener to the button to implement the clicking action on the profile view
        btnProfileServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //passing the JSONObject of the individual GE form to this activity
                try {
                     JSONObject myJsonForm = new FormUtils()
                            .getFormJsonFromRepositoryOrAssets(GeProfileActivity.this,"individual_ge_form");

                     //patching the baseEntityId to the jsonForm
                    myJsonForm.put("entity_id",clientDetails.getDetails().get("base_entity_id"));
                    startFormActivity(myJsonForm);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    @Override
    protected void fetchProfileData() {

    }

    //implementing startFormActivity method for the JsonObject
    private void startFormActivity(JSONObject myJsonForm) {
        //here we are implementing how the Json Form will be called from the JsonObject
        Intent intent = new Intent(this, JsonFormActivity.class);
        //Declare the json form to the intent
        intent.putExtra("json",myJsonForm.toString());
        //using the method startActivityFormResult() to get the intent and results from
        // the form we use startActivityForResult to pass the intent and a requestCode
        startActivityForResult(intent,700);
    }

    //handling the result from the individual services jsonForm
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        //condition statement to check for results in filled individual form
        if ( requestCode == 700 && resultCode == RESULT_OK) {
            //passing the json form into a string
            String idvGeForm = intent.getStringExtra("json");

            //log  the json form instead of Toast message to view details of the form
            Timber.d("Filled GE services" +idvGeForm);

            //storing data in the sharePreference in form of a text file
            AllSharedPreferences allSharedPreferences = Utils.getAllSharedPreferences();

            //Convert the data in text file to an Event
            Event event = JsonFormUtils.processJsonForm(allSharedPreferences,idvGeForm,"");

            //Save and process the data created as EVENT

            //Initializing Gson() class that converts data to an object
            Gson gson = JsonFormUtils.gson;
            String jsonString = gson.toJson(event);


            //processing the event into a JSONObject to save it in db
            try {
                JSONObject ge_service_form = new JSONObject(jsonString);
                //Processing the created EVENT which is now converted into JSONObject
                NCUtils.processEvent(event.getBaseEntityId(),ge_service_form);
            } catch (Exception e) {
                Timber.e(e);
            }
        }

        }
    }


