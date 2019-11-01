package org.smartregister.chw.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.smartregister.chw.R;
import org.smartregister.chw.adapter.MedicalHistoryAdapter;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.core.activity.CoreChildMedicalHistoryActivity;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.chw.core.utils.Utils;
import org.smartregister.chw.domain.MedicalHistory;
import org.smartregister.chw.util.CustomDividerItemDecoration;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.Vaccine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class DefaultChildMedicalHistoryActivity implements CoreChildMedicalHistoryActivity.Flavor {

    protected LayoutInflater inflater;
    private Context context;
    private List<Visit> visits;
    private Map<String, List<Visit>> visitMap = new LinkedHashMap<>();
    private Map<String, List<Vaccine>> vaccineMap = new LinkedHashMap<>();
    private List<ServiceRecord> serviceTypeListMap;
    private LinearLayout parentView;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy", Locale.getDefault());

    @Override
    public View bindViews(Activity activity) {
        inflater = activity.getLayoutInflater();
        this.context = activity;
        parentView = new LinearLayout(activity);
        parentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        parentView.setOrientation(LinearLayout.VERTICAL);
        return parentView;
    }

    @Override
    public void processViewData(List<Visit> visits, Map<String, List<Vaccine>> vaccineMap, List<ServiceRecord> serviceTypeListMap, Context context) {
        this.visits = visits;
        this.vaccineMap = vaccineMap;
        this.serviceTypeListMap = serviceTypeListMap;

        for (Visit v : visits) {
            List<Visit> type_visits = visitMap.get(v.getVisitType());
            if (type_visits == null) type_visits = new ArrayList<>();

            type_visits.add(v);
            visitMap.put(v.getVisitType(), type_visits);
        }

        evaluateLastVisitDate();
        evaluateImmunizations();
        evaluateGrowthAndNutrition();
        evaluateECD();
        evaluateLLITN();
    }

    private void evaluateLastVisitDate() {
        if (visits.size() > 0) {
            View view = inflater.inflate(R.layout.medical_history_item, null);
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            tvTitle.setText(context.getString(R.string.last_visit));

            int days = Days.daysBetween(new DateTime(visits.get(0).getDate()), new DateTime()).getDays();
            TextView tvInfo = view.findViewById(R.id.tvInfo);
            tvInfo.setText(context.getString(R.string.last_visit_40_days_ago, Integer.toString(days)));
            parentView.addView(view);
        }
    }

    private void evaluateImmunizations() {
        if (vaccineMap != null && vaccineMap.size() > 0) {

            // generate data
            List<MedicalHistory> medicalHistories = new ArrayList<>();
            for (Map.Entry<String, List<Vaccine>> entry : vaccineMap.entrySet()) {
                MedicalHistory history = new MedicalHistory();
                history.setTitle(getVaccineTitle(entry.getKey(), context));
                List<String> content = new ArrayList<>();
                for (Vaccine vaccine : entry.getValue()) {
                    String val = vaccine.getName().toLowerCase().replace(" ", "_");
                    String translated = Utils.getStringResourceByName(val, context);
                    content.add(String.format("%s - %s %s", translated, context.getString(R.string.done), sdf.format(vaccine.getDate())));
                }
                history.setText(content);
                medicalHistories.add(history);

            }

            View view = renderData(
                    context.getString(R.string.immunization),
                    medicalHistories,
                    false,
                    R.layout.medical_history_vaccine_item
            );

            parentView.addView(view);
        }
    }

    private String getVaccineTitle(String name, Context context) {
        return name.contains("birth") ? context.getString(R.string.at_birth) :
                name.replace("w", " " + context.getString(R.string.week_full))
                        .replace("m", " " + context.getString(R.string.month_full));
    }

    private View renderData(String title, List<MedicalHistory> medicalHistories, boolean hasSeparator, int layoutID) {
        View view = inflater.inflate(R.layout.medical_history_nested, null);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        tvTitle.setAllCaps(true);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new MedicalHistoryAdapter(medicalHistories, layoutID));

        if (hasSeparator)
            recyclerView.addItemDecoration(new CustomDividerItemDecoration(ContextCompat.getDrawable(context, R.drawable.divider)));

        return view;
    }

    private void evaluateGrowthAndNutrition() {
        if (visitMap.size() > 0) {

            // generate data
            List<MedicalHistory> medicalHistories = new ArrayList<>();
            for (Map.Entry<String, List<Vaccine>> entry : vaccineMap.entrySet()) {
                MedicalHistory history = new MedicalHistory();
                history.setTitle(getVaccineTitle(entry.getKey(), context));
                List<String> content = new ArrayList<>();
                for (Vaccine vaccine : entry.getValue()) {
                    String val = vaccine.getName().toLowerCase().replace(" ", "_");
                    String translated = Utils.getStringResourceByName(val, context);
                    content.add(String.format("%s - %s %s", translated, context.getString(R.string.done), sdf.format(vaccine.getDate())));
                }
                history.setText(content);
                medicalHistories.add(history);

            }

            View view = renderData(
                    context.getString(R.string.growth_and_nutrition),
                    medicalHistories,
                    true,
                    R.layout.medical_history_nested_sub_item
            );

            parentView.addView(view);
        }
    }

    private void evaluateECD() {
        if (visits.size() > 0) {

            List<Visit> visits = visitMap.get(CoreConstants.EventType.ECD);
            if (visits != null) {

                // generate data
                List<MedicalHistory> medicalHistories = new ArrayList<>();


                View view = renderData(
                        context.getString(R.string.growth_and_nutrition),
                        medicalHistories,
                        true,
                        R.layout.medical_history_nested_sub_item
                );

                parentView.addView(view);
            }
        }
    }

    private void evaluateLLITN() {
        if (visits.size() > 0) {
            View view = inflater.inflate(R.layout.medical_history_item, null);
            TextView tvTitle = view.findViewById(R.id.tvTitle);
            tvTitle.setText(context.getString(R.string.llitn_title));
            tvTitle.setAllCaps(true);

            int days = Days.daysBetween(new DateTime(visits.get(0).getDate()), new DateTime()).getDays();
            TextView tvInfo = view.findViewById(R.id.tvInfo);
            tvInfo.setText(context.getString(R.string.last_visit_40_days_ago, Integer.toString(days)));

            parentView.addView(view);
        }
    }
}
