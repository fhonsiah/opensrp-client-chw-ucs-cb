package org.smartregister.chw.interactor;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.chw.R;
import org.smartregister.chw.actionhelper.DangerSignsAction;
import org.smartregister.chw.anc.AncLibrary;
import org.smartregister.chw.anc.actionhelper.HomeVisitActionHelper;
import org.smartregister.chw.anc.contract.BaseAncHomeVisitContract;
import org.smartregister.chw.anc.domain.MemberObject;
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.chw.anc.model.BaseAncHomeVisitAction;
import org.smartregister.chw.anc.util.JsonFormUtils;
import org.smartregister.chw.anc.util.VisitUtils;
import org.smartregister.chw.dao.PersonDao;
import org.smartregister.chw.domain.Person;
import org.smartregister.chw.util.Constants;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import timber.log.Timber;

public class PncHomeVisitInteractorFlv extends DefaultPncHomeVisitInteractorFlv {

    @Override
    public LinkedHashMap<String, BaseAncHomeVisitAction> calculateActions(BaseAncHomeVisitContract.View view, MemberObject memberObject, BaseAncHomeVisitContract.InteractorCallBack callBack) throws BaseAncHomeVisitAction.ValidationException {
        actionList = new LinkedHashMap<>();
        context = view.getContext();
        // get the preloaded data
        if (view.getEditMode()) {
            Visit lastVisit = AncLibrary.getInstance().visitRepository().getLatestVisit(memberObject.getBaseEntityId(), Constants.EventType.PNC_HOME_VISIT);
            if (lastVisit != null) {
                details = VisitUtils.getVisitGroups(AncLibrary.getInstance().visitDetailsRepository().getVisits(lastVisit.getVisitId()));
            }
        }

        children = PersonDao.getMothersChildren(memberObject.getBaseEntityId());
        if (children == null)
            children = new ArrayList<>();

        try {
            evaluateDangerSignsMother();
            evaluateDangerSignsBaby();
            evaluatePNCHealthFacilityVisitOne();
            evaluatePNCHealthFacilityVisitTwo();
            evaluatePNCHealthFacilityVisitThree();
            evaluatePNCHealthFacilityVisitFour();
            evaluateFamilyPlanning();
            evaluateImmunization();
            evaluateExclusiveBreastFeeding();
            evaluateCounselling();
            evaluateNutritionStatusMother();
            evaluateNutritionStatusBaby();
            evaluateMalariaPrevention();
            evaluateObsIllnessMother();
            evaluateObsIllnessBaby();
        } catch (BaseAncHomeVisitAction.ValidationException e) {
            throw (e);
        } catch (Exception e) {
            Timber.e(e);
        }
        return actionList;
    }

    private void evaluateDangerSignsMother() throws Exception {
        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, context.getString(R.string.pnc_danger_signs_mother))
                .withOptional(false)
                .withDetails(details)
                .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getDangerSignsMother())
                .withHelper(new PNCDangerSignsMotherHelper())
                .build();
        actionList.put(context.getString(R.string.pnc_danger_signs_mother), action);
    }

    private class PNCDangerSignsMotherHelper extends HomeVisitActionHelper {
        private String danger_signs_present_mama;

        @Override
        public void onPayloadReceived(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                danger_signs_present_mama = JsonFormUtils.getCheckBoxValue(jsonObject, "danger_signs_present_mama");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String evaluateSubTitle() {
            return MessageFormat.format("{0}: {1}", context.getString(R.string.anc_home_visit_danger_signs), danger_signs_present_mama);
        }

        @Override
        public BaseAncHomeVisitAction.Status evaluateStatusOnPayload() {
            if (StringUtils.isNotBlank(danger_signs_present_mama)) {
                return BaseAncHomeVisitAction.Status.COMPLETED;
            } else {
                return BaseAncHomeVisitAction.Status.PENDING;
            }
        }
    }

    private void evaluateDangerSignsBaby() throws Exception {
        for (Person baby : children) {
            BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, MessageFormat.format(context.getString(R.string.pnc_danger_signs_baby), baby.getFullName()))
                    .withOptional(false)
                    .withDetails(details)
                    .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getDangerSignsBaby())
                    .withHelper(new PNCDangerSignsBabyHelper())
                    .build();
            actionList.put(MessageFormat.format(context.getString(R.string.pnc_danger_signs_baby), baby.getFullName()), action);
        }
    }

    private class PNCDangerSignsBabyHelper extends HomeVisitActionHelper {
        private String danger_signs_present_child;

        @Override
        public void onPayloadReceived(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                danger_signs_present_child = JsonFormUtils.getCheckBoxValue(jsonObject, "danger_signs_present_child");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String evaluateSubTitle() {
            return MessageFormat.format("{0}: {1}", context.getString(R.string.anc_home_visit_danger_signs), danger_signs_present_child);
        }

        @Override
        public BaseAncHomeVisitAction.Status evaluateStatusOnPayload() {
            if (StringUtils.isNotBlank(danger_signs_present_child)) {
                return BaseAncHomeVisitAction.Status.COMPLETED;
            } else {
                return BaseAncHomeVisitAction.Status.PENDING;
            }
        }
    }

    private void evaluatePNCHealthFacilityVisitOne() throws Exception {
        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, context.getString(R.string.pnc_health_facility_visit_within_fourty_eight_hours))
                .withOptional(false)
                .withDetails(details)
                .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getHealthFacilityVisitOne())
                .withHelper(new PNCHealthFacilityVisitHelper())
                .build();
        actionList.put(context.getString(R.string.pnc_health_facility_visit_within_fourty_eight_hours), action);
    }

    private class PNCHealthFacilityVisitHelper extends HomeVisitActionHelper {

        @Override
        public void onPayloadReceived(String s) {

        }

        @Override
        public String evaluateSubTitle() {
            return null;
        }

        @Override
        public BaseAncHomeVisitAction.Status evaluateStatusOnPayload() {
            return null;
        }
    }

    private void evaluatePNCHealthFacilityVisitTwo() throws Exception {
        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, context.getString(R.string.pnc_health_facility_visit_days_three_to_seven))
                .withOptional(false)
                .withDetails(details)
                .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getDangerSigns())
                .withHelper(new DangerSignsAction())
                .build();
        actionList.put(context.getString(R.string.pnc_health_facility_visit_days_three_to_seven), action);
    }

    private void evaluatePNCHealthFacilityVisitThree() throws Exception {
        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, context.getString(R.string.pnc_health_facility_visit_days_eight_to_twenty_eight))
                .withOptional(false)
                .withDetails(details)
                .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getDangerSigns())
                .withHelper(new DangerSignsAction())
                .build();
        actionList.put(context.getString(R.string.pnc_health_facility_visit_days_eight_to_twenty_eight), action);
    }

    private void evaluatePNCHealthFacilityVisitFour() throws Exception {
        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, context.getString(R.string.pnc_health_facility_visit_days_twenty_nine_to_forty_two))
                .withOptional(false)
                .withDetails(details)
                .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getDangerSigns())
                .withHelper(new DangerSignsAction())
                .build();
        actionList.put(context.getString(R.string.pnc_health_facility_visit_days_twenty_nine_to_forty_two), action);
    }

    private void evaluateFamilyPlanning() throws Exception {
        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, context.getString(R.string.pnc_family_planning))
                .withOptional(false)
                .withDetails(details)
                .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getDangerSigns())
                .withHelper(new DangerSignsAction())
                .build();
        actionList.put(context.getString(R.string.pnc_family_planning), action);
    }

    private void evaluateImmunization() throws Exception {
        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, context.getString(R.string.pnc_immunization_at_birth))
                .withOptional(false)
                .withDetails(details)
                .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getDangerSigns())
                .withHelper(new DangerSignsAction())
                .build();
        actionList.put(context.getString(R.string.pnc_immunization_at_birth), action);
    }

    private void evaluateExclusiveBreastFeeding() throws Exception {
        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, context.getString(R.string.pnc_exclusive_breastfeeding))
                .withOptional(false)
                .withDetails(details)
                .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getDangerSigns())
                .withHelper(new DangerSignsAction())
                .build();
        actionList.put(context.getString(R.string.pnc_exclusive_breastfeeding), action);
    }

    private void evaluateCounselling() throws Exception {
        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, context.getString(R.string.pnc_counselling))
                .withOptional(false)
                .withDetails(details)
                .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getCOUNSELLING())
                .withHelper(new CounsellingHelper())
                .build();
        actionList.put(context.getString(R.string.pnc_counselling), action);
    }

    private class CounsellingHelper extends HomeVisitActionHelper {

        private String couselling_pnc;

        @Override
        public void onPayloadReceived(String jsonPayload) {
            try {
                JSONObject jsonObject = new JSONObject(jsonPayload);
                couselling_pnc = JsonFormUtils.getCheckBoxValue(jsonObject, "couselling_pnc");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String evaluateSubTitle() {
            return MessageFormat.format("{0}: {1}", "Counselling", couselling_pnc);
        }

        @Override
        public BaseAncHomeVisitAction.Status evaluateStatusOnPayload() {
            if (StringUtils.isNotBlank(couselling_pnc)) {
                return BaseAncHomeVisitAction.Status.COMPLETED;
            } else {
                return BaseAncHomeVisitAction.Status.PENDING;
            }
        }
    }

    private void evaluateNutritionStatusMother() throws Exception {
        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, context.getString(R.string.pnc_nutrition_status))
                .withOptional(false)
                .withDetails(details)
                .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getNutritionStatusMother())
                .withHelper(new NutritionStatusMotherHelper())
                .build();
        actionList.put(context.getString(R.string.pnc_nutrition_status), action);
    }

    private class NutritionStatusMotherHelper extends HomeVisitActionHelper {

        private String nutrition_status_mama;

        @Override
        public void onPayloadReceived(String jsonPayload) {
            try {
                JSONObject jsonObject = new JSONObject(jsonPayload);
                nutrition_status_mama = JsonFormUtils.getValue(jsonObject, "nutrition_status_mama");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String evaluateSubTitle() {
            return MessageFormat.format("{0}: {1}", "Nutrition Status ", nutrition_status_mama);
        }

        @Override
        public BaseAncHomeVisitAction.Status evaluateStatusOnPayload() {
            if (StringUtils.isNotBlank(nutrition_status_mama)) {
                return BaseAncHomeVisitAction.Status.COMPLETED;
            } else {
                return BaseAncHomeVisitAction.Status.PENDING;
            }
        }
    }

    private void evaluateNutritionStatusBaby() throws Exception {
        for (Person baby : children) {
            BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, MessageFormat.format(context.getString(R.string.pnc_nutrition_status_baby_name), baby.getFullName()))
                    .withOptional(false)
                    .withDetails(details)
                    .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getNutritionStatusInfant())
                    .withHelper(new NutritionStatusBabyHelper())
                    .build();
            actionList.put(MessageFormat.format(context.getString(R.string.pnc_nutrition_status_baby_name), baby.getFullName()), action);
        }
    }

    private class NutritionStatusBabyHelper extends HomeVisitActionHelper {

        private String nutrition_status_1m;

        @Override
        public void onPayloadReceived(String jsonPayload) {
            try {
                JSONObject jsonObject = new JSONObject(jsonPayload);
                nutrition_status_1m = JsonFormUtils.getValue(jsonObject, "nutrition_status_1m");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String evaluateSubTitle() {
            return MessageFormat.format("{0}: {1}", "Nutrition Status ", nutrition_status_1m);
        }

        @Override
        public BaseAncHomeVisitAction.Status evaluateStatusOnPayload() {
            if (StringUtils.isNotBlank(nutrition_status_1m)) {
                return BaseAncHomeVisitAction.Status.COMPLETED;
            } else {
                return BaseAncHomeVisitAction.Status.PENDING;
            }
        }
    }

    private void evaluateMalariaPrevention() throws Exception {
        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, context.getString(R.string.pnc_malaria_prevention))
                .withOptional(false)
                .withDetails(details)
                .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getDangerSigns())
                .withHelper(new DangerSignsAction())
                .build();
        actionList.put(context.getString(R.string.pnc_malaria_prevention), action);
    }

    private void evaluateObsIllnessMother() throws Exception {
        BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, context.getString(R.string.pnc_observation_and_illness_mother))
                .withOptional(true)
                .withDetails(details)
                .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getDangerSigns())
                .withHelper(new DangerSignsAction())
                .build();
        actionList.put(context.getString(R.string.pnc_observation_and_illness_mother), action);
    }

    private void evaluateObsIllnessBaby() throws Exception {
        for (Person baby : children) {
            BaseAncHomeVisitAction action = new BaseAncHomeVisitAction.Builder(context, MessageFormat.format(context.getString(R.string.pnc_observation_and_illness_baby), baby.getFullName()))
                    .withOptional(true)
                    .withDetails(details)
                    .withFormName(Constants.JSON_FORM.PNC_HOME_VISIT.getDangerSigns())
                    .withHelper(new DangerSignsAction())
                    .build();
            actionList.put(MessageFormat.format(context.getString(R.string.pnc_observation_and_illness_baby), baby.getFullName()), action);
        }
    }


}
