package org.smartregister.chw.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.factory.FileSourceFactoryHelper;
import com.vijay.jsonwizard.utils.FormUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.anc.util.NCUtils;
import org.smartregister.chw.core.custom_views.NavigationMenu;
import org.smartregister.chw.fragment.GeRegisterFragment;
import org.smartregister.chw.presenter.GeRegisterActivityPresenter;
import org.smartregister.chw.util.Constants;
import org.smartregister.chw.util.Utils;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.helper.BottomNavigationHelper;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.view.activity.BaseRegisterActivity;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.Collections;
import java.util.List;

import okhttp3.internal.Util;
import timber.log.Timber;

public class GeRegisterActivity extends BaseRegisterActivity {

    //Opening an Activity from another activity using INTENTS
//    public static void launchGeRegisterActivity(String baseEntityId, Activity activity){
//        Intent geIntent = new Intent(activity,GeRegisterActivity.class);
//        geIntent.putExtra("BASE_ENTITY_ID",baseEntityId);
//        activity.startActivity(geIntent);
//    }

    @Override
    protected void initializePresenter() {
        presenter = new GeRegisterActivityPresenter();
    }

    @Override
    protected BaseRegisterFragment getRegisterFragment() {

        return new GeRegisterFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //obtaining the instance of the navigationMenu
        NavigationMenu.getInstance(this,null,null);

        //receiving the sent baseEntityId from the source activity
        String receivedBaseEntityId = getIntent().getStringExtra("BASE_ENTITY_ID");

        //creating a condition to check for the received intent
        if(receivedBaseEntityId != null){
            Toast.makeText(this,
                    "GeRegisterActivity : Open Enrollment Form",
                    Toast.LENGTH_LONG).show();
            startFormActivity("ge_enrollment_consent_form", receivedBaseEntityId,null);
        }
    }

    @Override
    protected Fragment[] getOtherFragments() {
        return new Fragment[0];
    }

    @Override
    public void startFormActivity(String formName, String entityId, String metadata) {

        //old way of loading the json form into the activity
//        try {
//            JSONObject myJsonForm =
//                    FileSourceFactoryHelper.
//                            getFileSource("").
//                            getFormFromFile(GeRegisterActivity.this,"sample_form");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

        //loading the json form  from resources  repository for  into the activity
        try {

            //getting the instance of the JSON object
            JSONObject myJsonForm = new FormUtils()
                    .getFormJsonFromRepositoryOrAssets(GeRegisterActivity.this,formName);

            //we are now patching the baseEntityId to the json form we have
            myJsonForm.put("entity_id",entityId);

            //we are going to call the method startFormActivity to call the form
            startFormActivity(myJsonForm);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startFormActivity(JSONObject jsonObject) {

        //here we are implementing how the Json Form will be called from the JsonObject
        Intent intent = new Intent(this, JsonFormActivity.class);
        //Declare the json form to the intent
        intent.putExtra("json",jsonObject.toString());
        //using the method startActivityFormResult() to get the intent and results from the form
//        startActivity(intent);
        startActivityForResult(intent,700);
    }

    @Override
    protected void onActivityResultExtended(int requestCode, int resultCode, Intent intent) {

        //handling th results we have got from the startActivityFormResult()
        if (requestCode==700 && resultCode == RESULT_OK ){

            //getting the jsonForm in form of a string and passing the intent
            String filledForm = intent.getStringExtra("json");
            //log  the json form instead of Toast message to view details of the form
//            Timber.d("Filled GE form" +filledForm);

               //persisting data in sharedPreferences which
                // is a text file in Android for storing data in form of key and value
                AllSharedPreferences allSharedPreferences = Utils.getAllSharedPreferences();

                //1.Convert the received filled form into and EVENT
                Event event = JsonFormUtils.processJsonForm(allSharedPreferences,filledForm,"ec_gender_equality");

                //2.Save and process the created EVENT

                 //initialize the method Gson() from google which converts objects to jsonString
                 Gson gson = JsonFormUtils.gson;
                 String jsonString = gson.toJson(event);

            try {
                //converting the jsonString into JSONObject
                JSONObject  form = new JSONObject(jsonString);

                //Processing the created EVENT which is now converted into JSONObject
                NCUtils.processEvent(event.getBaseEntityId(),form);
            } catch (Exception e) {
                //log error to avoid application from crashing
                Timber.e(e);
            }
        }

    }

    @Override
    public List<String> getViewIdentifiers() {
        return Collections.emptyList();
    }

    @Override
    public void startRegistration() {

    }
    @Override
    protected void registerBottomNavigation(){
        bottomNavigationHelper = new BottomNavigationHelper();
    }

}
