package org.smartregister.chw.activity;

import android.content.Intent;

import com.opensrp.chw.core.utils.Constants;

import org.smartregister.chw.BaseActivityTest;

public class FamilyProfileMenuActivityTest extends BaseActivityTest<FamilyProfileMenuActivity> {
    @Override
    protected Class<FamilyProfileMenuActivity> getActivityClass() {
        return FamilyProfileMenuActivity.class;
    }

    @Override
    protected Intent getControllerIntent() {
        Intent intent = new Intent();
        intent.putExtra(org.smartregister.family.util.Constants.INTENT_KEY.BASE_ENTITY_ID, "12345");
        intent.putExtra(FamilyProfileMenuActivity.MENU, Constants.MenuType.ChangeHead);
        return intent;
    }
}
