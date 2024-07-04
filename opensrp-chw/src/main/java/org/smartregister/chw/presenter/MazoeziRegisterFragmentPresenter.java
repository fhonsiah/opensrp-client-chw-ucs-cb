package org.smartregister.chw.presenter;

import org.smartregister.chw.contract.MazoeziRegisterFragmentContract;
import org.smartregister.chw.fragment.MazoeziRegisterFragment;
import org.smartregister.chw.model.MazoeziRegisterFragmentModel;
import org.smartregister.view.contract.BaseRegisterFragmentContract;

public class MazoeziRegisterFragmentPresenter implements BaseRegisterFragmentContract.Presenter {


    MazoeziRegisterFragmentContract.View view;
    MazoeziRegisterFragmentContract.Model model;
    public MazoeziRegisterFragmentPresenter(MazoeziRegisterFragmentContract.View view, MazoeziRegisterFragmentContract.Model model){
      this.view = view;
      this.model = model;
    }

    public String getMainCondition(){
        return model.getMainCondition();
    }

    public String getDefaultSortQuery(){
        return model.getDefaultSortQuery();
    }

    public  String getTableName(){
        return model.getTableName();
    }
    @Override
    public void processViewConfigurations() {

    }

    @Override
    public void initializeQueries(String s) {

        view.initializeQueryParams(getTableName(),model.getCountSelect(s),model.getMainSelect(s));

        ((MazoeziRegisterFragment)view).initializeAdapter();

        view.countExecute();

        ((MazoeziRegisterFragment)view).initialFilterandSortExecute();

    }

    @Override
    public void startSync() {

    }

    @Override
    public void searchGlobally(String s) {

    }
}
