package org.smartregister.chw.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.smartregister.chw.R;
import org.smartregister.chw.model.ReferralTypeModel;

import java.util.ArrayList;
import java.util.List;

public class ReferralTypeAdapter extends RecyclerView.Adapter<ReferralTypeAdapter.ReferralTypeViewHolder> {

    private List<ReferralTypeModel> referralTypes = new ArrayList<>();

    @NonNull
    @Override
    public ReferralTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.referral_type_list_row, viewGroup, false);
        return new ReferralTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferralTypeViewHolder referralTypeViewHolder, int position) {
        ReferralTypeModel referralTypeModel = referralTypes.get(position);
        referralTypeViewHolder.referralType.setText(referralTypeModel.getReferralType());
        referralTypeViewHolder.referralType.setTag(R.id.referral_type_form_name, referralTypeModel.getFormName());
        referralTypeViewHolder.referralType.setTag(R.id.referral_type_client, referralTypeModel.getClient());
    }

    @Override
    public int getItemCount() {
        return referralTypes.size();
    }

    public void setReferralTypes(List<ReferralTypeModel> referralTypes) {
        if (referralTypes != null) {
            this.referralTypes.addAll(referralTypes);
        }
    }

    class ReferralTypeViewHolder extends RecyclerView.ViewHolder {
        private TextView referralType;

        private ReferralTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            referralType = itemView.findViewById(R.id.referralType);
        }
    }
}
