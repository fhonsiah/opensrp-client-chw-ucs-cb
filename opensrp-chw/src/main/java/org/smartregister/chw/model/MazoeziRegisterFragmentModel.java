package org.smartregister.chw.model;

import org.smartregister.chw.contract.MazoeziRegisterFragmentContract;

public class MazoeziRegisterFragmentModel implements MazoeziRegisterFragmentContract.Model {
    @Override
    public String getMainCondition() {
        return "landmark = 'Near Bus Station'";
    }

    @Override
    public String getDefaultSortQuery() {
        return "last_interacted_with";
    }

    @Override
    public String getTableName() {
        return "ec_family";
    }

    @Override
    public String getCountSelect(String maincondition) {
        return "SELECT COUNT(*) FROM " +getTableName();
    }

    @Override
    public String getMainSelect(String maincondition) {
        return "SELECT id as _id, * FROM " +getTableName()+ " WHERE " +getMainCondition();
    }
}
