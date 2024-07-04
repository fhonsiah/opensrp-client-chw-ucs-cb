package org.smartregister.chw.contract;

import org.smartregister.view.contract.BaseRegisterFragmentContract;

public interface MazoeziRegisterFragmentContract  extends BaseRegisterFragmentContract {

    public interface Model {

        public String getMainCondition();

        public String getDefaultSortQuery();

        public String getTableName();

        public String getCountSelect(String maincondition);

        public String getMainSelect(String maincondition);
    }




}
