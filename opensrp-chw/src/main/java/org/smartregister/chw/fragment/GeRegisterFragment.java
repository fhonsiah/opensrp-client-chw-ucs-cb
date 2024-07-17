package org.smartregister.chw.fragment;

import android.view.View;

import org.smartregister.chw.R;
import org.smartregister.chw.model.GeRegisterFragmentModel;
import org.smartregister.chw.presenter.GeRegisterFragmentPresenter;
import org.smartregister.chw.provider.OpdRegisterProvider;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.view.customcontrols.CustomFontTextView;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;

public class GeRegisterFragment extends BaseRegisterFragment {

    @Override
    protected void initializePresenter() {
        presenter = new GeRegisterFragmentPresenter(GeRegisterFragment.this, new GeRegisterFragmentModel());
    }

    @Override
    public void setupViews(View view) {
        super.setupViews(view);

        view.findViewById(R.id.scanQrCode).setVisibility(View.GONE);
        view.findViewById(R.id.left_menu).setVisibility(View.VISIBLE);
        view.findViewById(R.id.opensrp_logo_image_view).setVisibility(View.GONE);
        view.findViewById(R.id.txt_title_label).setVisibility(View.VISIBLE);
//        int y = 6;
//        float z = (float)y
//
//        view.findViewById(R.id.txt_title_label);
//        CustomFontTextView txt = (CustomFontTextView)view;
//        txt.setText(R.string.menu_ge);


        ((CustomFontTextView)view.findViewById(R.id.txt_title_label)).setText(R.string.menu_ge);

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

    @Override
    protected void onViewClicked(View view) {
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
