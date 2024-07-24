package org.smartregister.chw.activity;

import android.content.Intent;

import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.internal.service.Common;

import org.smartregister.chw.R;
import org.smartregister.chw.util.Utils;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.view.activity.BaseProfileActivity;
import org.smartregister.view.customcontrols.CustomFontTextView;

import de.hdodenhof.circleimageview.CircleImageView;

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

        //pass data to the view
        if (clientDetails != null){

            Integer age = Utils.getAgeFromDate(clientDetails.getColumnmaps().get("dob"));
            String fullName = clientDetails.getColumnmaps().get("first_name") + " " +
                    clientDetails.getColumnmaps().get("middle_name") + " " +
                    clientDetails.getColumnmaps().get("last_name") + " , " + age ;
            String gender = clientDetails.getColumnmaps().get("gender");
            String location  = clientDetails.getColumnmaps().get("village_town");
            String uniqueId = clientDetails.getColumnmaps().get( " unique_id");


            //setting text or matching the data to be displayed on the view that is customized
            txt_view_name.setText(fullName);
            txt_view_sex.setText(gender);
            txt_view_location.setText(location);
            txt_view_id.setText(uniqueId);
        }

    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    @Override
    protected void fetchProfileData() {

    }
}
