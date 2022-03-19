package org.smartregister.chw.activity;


import android.app.Activity;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import org.smartregister.chw.R;
import org.smartregister.chw.core.activity.CorePmtctRegisterActivity;
import org.smartregister.chw.core.custom_views.NavigationMenu;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.fragment.MotherChampionRegisterFragment;
import org.smartregister.chw.fragment.PmtctRegisterFragment;
import org.smartregister.chw.listener.ChwPmtctBottomNavigationListener;
import org.smartregister.helper.BottomNavigationHelper;
import org.smartregister.listener.BottomNavigationListener;
import org.smartregister.view.fragment.BaseRegisterFragment;

import androidx.fragment.app.Fragment;

public class PmtctRegisterActivity extends CorePmtctRegisterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationMenu.getInstance(this, null, null);
    }

    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        return new MotherChampionRegisterFragment();
    }

    @Override
    protected void registerBottomNavigation() {
        bottomNavigationHelper = new BottomNavigationHelper();
        bottomNavigationView = findViewById(org.smartregister.R.id.bottom_navigation);

        if (bottomNavigationView != null) {
            bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
            bottomNavigationView.getMenu().removeItem(org.smartregister.R.id.action_clients);
            bottomNavigationView.getMenu().removeItem(org.smartregister.chw.hiv.R.id.action_register);
            bottomNavigationView.getMenu().removeItem(org.smartregister.R.id.action_search);
            bottomNavigationView.getMenu().removeItem(org.smartregister.R.id.action_library);

            bottomNavigationView.inflateMenu(getMenuResource());
            bottomNavigationHelper.disableShiftMode(bottomNavigationView);

            BottomNavigationListener pmtctBottomNavigationListener = getBottomNavigation(this);
            bottomNavigationView.setOnNavigationItemSelectedListener(pmtctBottomNavigationListener);

        }
    }

    public BottomNavigationListener getBottomNavigation(Activity activity) {
        return new ChwPmtctBottomNavigationListener(activity);
    }

    @Override
    protected Fragment[] getOtherFragments() {
        return new PmtctRegisterFragment[]{
                new PmtctRegisterFragment()
        };
    }


    @Override
    public int getMenuResource() {
        return R.menu.pmtct_bottom_nav_menu;
    }

    @Override
    protected void onResumption() {
        super.onResumption();
        NavigationMenu menu = NavigationMenu.getInstance(this, null, null);
        if (menu != null) {
            menu.getNavigationAdapter().setSelectedView(CoreConstants.DrawerMenu.PMTCT_FOLLOWUP);
        }
    }

}
