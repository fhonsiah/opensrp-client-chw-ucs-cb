package org.smartregister.chw.contract;

import org.smartregister.view.contract.BaseRegisterFragmentContract;

public interface GeRegisterFragmentContract extends BaseRegisterFragmentContract {

    //this is where we created the skeleton of the databases
    //that we pulled by using the data we selected from the table

    public interface  Model {
          public String getMainCondition();

          public String getDefaultSortQuery();

          public  String getTableName();

          public String getCountSelect(String mainCondition);

          public String getMainSelect(String mainCondition);


    }
}
