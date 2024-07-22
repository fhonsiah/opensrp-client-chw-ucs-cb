package org.smartregister.chw.model;

import org.smartregister.chw.contract.GeRegisterFragmentContract;
import org.smartregister.chw.core.utils.CoreConstants;

public class GeRegisterFragmentModel implements GeRegisterFragmentContract.Model {

    //implementing the methods defined in the contract
    //in the contract these methods were defined as abstract methods
    @Override
    public String getMainCondition() {
        return "is_closed = 0";
    }

    @Override
    public String getDefaultSortQuery() {
        return "ege.last_interacted_with DESC";
    }

    @Override
    public String getTableName() {
        return "ec_gender_equality";
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
//        return "SELECT id as _id, *  FROM " +getTableName() + " WHERE " +mainCondition;

        // here we are going to use a query that returns the exact individuals who have been enrolled
        return "SELECT " +
                "ege.id AS _id, " +
                "* " +
                "FROM " +getTableName() + " ege " +
                "JOIN " + CoreConstants.TABLE_NAME.FAMILY_MEMBER+ " efm ON " + " efm.base_entity_id = ege.base_entity_id " +
                "WHERE ege." +mainCondition;
    }
}
