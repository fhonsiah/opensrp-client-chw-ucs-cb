package org.smartregister.chw.model;

import org.smartregister.chw.contract.GeRegisterFragmentContract;

public class GeRegisterFragmentModel implements GeRegisterFragmentContract.Model {

    //implementing the methods defined in the contract
    //in the contract these methods were defined as abstract methods
    @Override
    public String getMainCondition() {
        return "is_closed = 0";
    }

    @Override
    public String getDefaultSortQuery() {
        return "last_interacted_with DESC";
    }

    @Override
    public String getTableName() {
        return "ec_family_member";
    }

    //in this method we are going to write  a full query name as written in executing
    //sql queries that are countSelect query and mainSelect query
    @Override
    public String getCountSelect(String mainCondition) {
        return "SELECT COUNT(*) FROM " + getTableName();
    }

    @Override
    public String getMainSelect(String mainCondition) {

        //adding a column that will be the primary key of the table
        //the table  result is expected to have a column of id in the result
        return "SELECT id as _id, *  FROM " +getTableName() + " WHERE " +mainCondition;
    }
}
