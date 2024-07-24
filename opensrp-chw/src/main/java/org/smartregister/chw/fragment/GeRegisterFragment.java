package org.smartregister.chw.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.smartregister.chw.R;
import org.smartregister.chw.activity.GeProfileActivity;
import org.smartregister.chw.core.custom_views.NavigationMenu;
import org.smartregister.chw.model.GeRegisterFragmentModel;
import org.smartregister.chw.presenter.GeRegisterFragmentPresenter;
import org.smartregister.chw.provider.OpdRegisterProvider;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.view.customcontrols.CustomFontTextView;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;

import javax.json.Json;

import timber.log.Timber;

public class  GeRegisterFragment extends BaseRegisterFragment {

    @Override
    protected void initializePresenter() {
        presenter = new GeRegisterFragmentPresenter(GeRegisterFragment.this, new GeRegisterFragmentModel());
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);

        //Update top left icon
        View qrCodeView = view.findViewById(R.id.scanQrCode);
        qrCodeView.setVisibility(View.GONE);

        //Update title name
        view.findViewById(R.id.scanQrCode).setVisibility(View.GONE);
        view.findViewById(R.id.opensrp_logo_image_view).setVisibility(View.GONE);
        view.findViewById(R.id.txt_title_label).setVisibility(View.VISIBLE);
        ((CustomFontTextView)view.findViewById(R.id.txt_title_label)).setText(R.string.menu_ge);

        //update the Navigation menu in the fragment
        Toolbar toolbar = view.findViewById(R.id.register_toolbar);

        //Obtaining the instance of the Navigation menu
        NavigationMenu.getInstance(getActivity(),null,toolbar);

    }

    @Override
    public void setUniqueID(String s) {

    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> hashMap) {

    }

    @Override
    protected String getMainCondition() {
        return ((GeRegisterFragmentPresenter)presenter).getMainCondition();     //this is normal casting in java ot change the value of the presenter
    }

    @Override
    protected String getDefaultSortQuery() {
        return ((GeRegisterFragmentPresenter)presenter).getDefaultSortQuery();    //this is normal casting in java ot change the value of the presenter
    }

    @Override
    public String getTablename() {
        return ((GeRegisterFragmentPresenter)presenter).getTableName();    ////this is normal casting in java ot change the value of the presenter
    }

    @Override
    protected void startRegistration() {
    }


    //this method is called once an item in the register list is clicked
    //view is passed when this register is being clicked
    @SuppressLint("TimberArgCount")
    @Override
    protected void onViewClicked(View view) {
        //we are going to create an object that will help us get
        // the client from the view that is passed in this method after getting the client
        // object we are going to cast the object to the view tag
        //view tag has an onClickListener and and object of the client(a container that contains client details)
        CommonPersonObjectClient clientName = (CommonPersonObjectClient) view.getTag(R.id.VIEW_CLIENT);

        //we have to create an intent that will enable us to open the Profile Activity
        Intent intent = new Intent(getActivity(), GeProfileActivity.class);
        intent.putExtra("client_info",clientName);

        //after getting th intent we are going to start the Activity we have intended to
        startActivity(intent);

        //we can pass the intent on logcat to view what is being passed
        Timber.d("Client - ...", new Gson().toJson(clientName));
//        Timber.d("Passed: %s", new Gson().toJson(intent));




    }

    @Override
    public void showNotFoundPopup(String s) {

    }

    //this adapter is made to provide data to the recycler view
    //or to the list view we have not dived into this yet (for future reference)
    public void initializeAdapter() {
        OpdRegisterProvider childRegisterProvider = new OpdRegisterProvider(getActivity(), registerActionHandler, paginationViewHandler);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, childRegisterProvider, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }
}
