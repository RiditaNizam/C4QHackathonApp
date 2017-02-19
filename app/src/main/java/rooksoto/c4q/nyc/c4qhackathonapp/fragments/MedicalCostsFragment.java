package rooksoto.c4q.nyc.c4qhackathonapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rooksoto.c4q.nyc.c4qhackathonapp.HhcData.api.HhcClient;
import rooksoto.c4q.nyc.c4qhackathonapp.HhcData.model.HhcData;
import rooksoto.c4q.nyc.c4qhackathonapp.R;

/**
 * Created by rook on 2/18/17.
 */
public class MedicalCostsFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener {

    View view;
    Spinner spnSelectService;
    TextView tvCostOfService;

    HhcClient hhcClient;
    List<HhcData> hhcData;
    ArrayList<String> spinnerArray;

    ArrayAdapter spinnerArrayAdapter;

    int feeLevel;
    private static final String TAG = "MedicalCostsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_medical_costs, container, false);
        feeLevel = getArguments().getInt("fee_level");

        spnSelectService = (Spinner) view.findViewById(R.id.spn_select_service);
        tvCostOfService = (TextView) view.findViewById(R.id.tv_cost_of_service);

        hideCostView();

        loadHHCData();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spnSelectService.setOnItemSelectedListener(this);
    }

    private void loadHHCData() {

        hhcClient = HhcClient.getInstance();
        Call<List<HhcData>> call = hhcClient.getData();

        call.enqueue(new Callback<List<HhcData>>() {

            @Override
            public void onResponse(Call<List<HhcData>> call, Response<List<HhcData>> response) {
                hhcData = response.body();
                Log.d(TAG, "onViewCreated: " + hhcData.size());
                populateArray();
            }

            @Override
            public void onFailure(Call<List<HhcData>> call, Throwable t) {
            }
        });
    }

    private void populateArray() {
        spinnerArray = new ArrayList<>();

        for (int i = 0; i < hhcData.size(); i++) {
            if (Integer.valueOf(hhcData.get(i).getHhcOptionsReducedFeeLevel()) == feeLevel) {
                spinnerArray.add(hhcData.get(i).getService());
            }
        }

        spinnerArrayAdapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArray);
        spinnerArrayAdapter.notifyDataSetChanged();
        spnSelectService.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                getCost(0);
                break;
            case 1:
                getCost(1);
                break;
            case 2:
                getCost(2);
                break;
            case 3:
                getCost(3);
                break;
            case 4:
                getCost(4);
                break;
            case 5:
                getCost(5);
                break;
            case 6:
                getCost(6);
                break;
            case 7:
                getCost(7);
                break;
            default:
                getCost(7);
                break;
        }
    }

    private void getCost(int position) {
        String cost = hhcData.get(position).getFee();
        String formattedCost = NumberFormat.
                getCurrencyInstance().
                format(Long.valueOf(cost));
        tvCostOfService.setText(formattedCost);
        showCostView();
    }

    private void showCostView() {
        tvCostOfService.setEnabled(true);
        tvCostOfService.setVisibility(View.VISIBLE);
    }

    private void hideCostView() {
        tvCostOfService.setEnabled(false);
        tvCostOfService.setVisibility(View.GONE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do Nothing
    }
}
