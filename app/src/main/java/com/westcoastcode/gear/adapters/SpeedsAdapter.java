package com.westcoastcode.gear.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.westcoastcode.gear.R;
import com.westcoastcode.gear.models.GearRatios;

/**
 * Created by Jeff on 11/15/2015.
 */
public class SpeedsAdapter extends RecyclerView.Adapter<SpeedsAdapter.SpeedsHolder>{

    private static final String TAG = "SpeedsAdapter";

    private SpeedAdapterListener mListener;

    public void setListener(SpeedAdapterListener listener){
        mListener = listener;
    }

    private static double[] mRatios = {
            GearRatios.GEAR_273,
            GearRatios.GEAR_308,
            GearRatios.GEAR_331,
            GearRatios.GEAR_354,
            GearRatios.GEAR_373,
            GearRatios.GEAR_411,
            GearRatios.GEAR_456,
            GearRatios.GEAR_488
    };

    @Override
    public SpeedsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gear_speed, parent, false);
        SpeedsHolder vh = new SpeedsHolder(v);
        vh.firstContainer = v.findViewById(R.id.viewItemFirstGear);
        vh.secondContainer = v.findViewById(R.id.viewItemSecondGear);
        vh.thirdContainer = v.findViewById(R.id.viewItemThirdGear);
        vh.fourthContainer = v.findViewById(R.id.viewItemFourthGear);
        vh.fifthContainer = v.findViewById(R.id.viewItemFifthGear);

        vh.gearRatio = (TextView) v.findViewById(R.id.textGear);
        vh.firstGearSpeed = (TextView) v.findViewById(R.id.textFirstSpeed);
        vh.secondGearSpeed = (TextView) v.findViewById(R.id.textSecondSpeed);
        vh.thirdGearSpeed = (TextView) v.findViewById(R.id.textThirdSpeed);
        vh.fourthGearSpeed = (TextView) v.findViewById(R.id.textFourthSpeed);
        vh.fifthGearSpeed = (TextView) v.findViewById(R.id.textFifthSpeed);
        return vh;
    }

    @Override
    public void onBindViewHolder(SpeedsHolder holder, int position) {

        double ratio = mRatios[position];

        double firstGearSpeed = 0;
        double secondGearSpeed = 0;
        double thirdGearSpeed = 0;
        double fourthGearSpeed = 0;
        double fifthGearSpeed = 0;

        if (mListener != null){
            firstGearSpeed = mListener.calcSpeed(ratio, 1);
            secondGearSpeed = mListener.calcSpeed(ratio, 2);
            thirdGearSpeed = mListener.calcSpeed(ratio, 3);
            fourthGearSpeed = mListener.calcSpeed(ratio, 4);
            fifthGearSpeed = mListener.calcSpeed(ratio, 5);
        }

        holder.gearRatio.setText(String.format("%.2f", ratio));

        int gearCount = mListener.getTransGearCount();

        if (gearCount > 0) {
            holder.firstGearSpeed.setText(String.format("%.0f", firstGearSpeed));
        } else {
            holder.firstContainer.setVisibility(View.GONE);
        }

        if (gearCount > 1) {
            holder.secondGearSpeed.setText(String.format("%.0f", secondGearSpeed));
        } else {
            holder.secondContainer.setVisibility(View.GONE);
        }

        if (gearCount > 2) {
            holder.thirdGearSpeed.setText(String.format("%.0f", thirdGearSpeed));
        } else {
            holder.thirdContainer.setVisibility(View.GONE);
        }

        if (gearCount > 3) {
            holder.fourthGearSpeed.setText(String.format("%.0f", fourthGearSpeed));
        } else {
            holder.fourthContainer.setVisibility(View.GONE);
        }

        if (gearCount > 4) {
            holder.fifthGearSpeed.setText(String.format("%.0f", fifthGearSpeed));
        } else {
            holder.fifthContainer.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mRatios.length;
    }

    public static class SpeedsHolder extends RecyclerView.ViewHolder{

        TextView gearRatio;
        TextView firstGearSpeed;
        TextView secondGearSpeed;
        TextView thirdGearSpeed;
        TextView fourthGearSpeed;
        TextView fifthGearSpeed;
        View firstContainer;
        View secondContainer;
        View thirdContainer;
        View fourthContainer;
        View fifthContainer;

        public SpeedsHolder(View itemView) {
            super(itemView);
        }
    }

    public interface SpeedAdapterListener{
        double calcSpeed(double gearRation, int transGear);
        int getTransGearCount();
    }

}
