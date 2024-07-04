package org.smartregister.chw.presenter;

import org.smartregister.chw.contract.GeRegisterFragmentContract;
import org.smartregister.chw.fragment.GeRegisterFragment;
import org.smartregister.view.contract.BaseRegisterContract;
import org.smartregister.view.contract.BaseRegisterFragmentContract;

public class GeRegisterFragmentPresenter implements BaseRegisterFragmentContract.Presenter {

    //create a constructor for the class GeRegisterFragmentPresenter
    //the constructor will pass params that will be declared when the class is loaded
    //then instantiate the declared variables in the constructor
    GeRegisterFragmentContract.View view;
    GeRegisterFragmentContract.Model model;
    public GeRegisterFragmentPresenter(GeRegisterFragmentContract.View view, GeRegisterFragmentContract.Model model){
        this.view = view;
        this.model = model;
    }

    //get methods that were defined in the model for the whole query
    public String getMainCondition(){
        return model.getMainCondition();
    }

    public String getDefaultSortQuery(){
        return model.getDefaultSortQuery();
    }

    public String getTableName(){
        return model.getTableName();
    }

    @Override
    public void processViewConfigurations() {
    }

    //in this method were going to control what we want to see in the view
    //as in what we want to see in the fragment as the list of the users
    //we have queried from the database.
    @Override
    public void initializeQueries(String s) {

          //this is where the presenter gets the data from the model
          view.initializeQueryParams(getTableName(),model.getCountSelect(s),model.getMainSelect(s));

          //casting the initializeAdapter method which is a new method in to the fragment so
          // the view has to be casted to become the adapter
          ((GeRegisterFragment)view).initializeAdapter();

           //so this method helps to run the countSelect query
           view.countExecute();

           // so this method also helps to filter and sort the getDefault sort query
           ((GeRegisterFragment) view).initialFilterandSortExecute();
    }


    @Override
    public void startSync() {

    }

    @Override
    public void searchGlobally(String s) {

    }

}
