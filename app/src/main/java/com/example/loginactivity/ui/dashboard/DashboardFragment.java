package com.example.loginactivity.ui.dashboard;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.loginactivity.R;
import com.example.loginactivity.databinding.FragmentDashboardBinding;
import com.example.loginactivity.domain.Star;
import com.example.loginactivity.util.StarOpenHelper;
import com.example.loginactivity.util.UserOpenHelper;
import com.example.loginactivity.util.VideoOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    private List<Star> listitem;

    private FragmentDashboardBinding binding;

    private VideoOpenHelper videoHelper;
    private UserOpenHelper userHelper;
    private StarOpenHelper starHelper;
    private static SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //加载SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        String uid = sharedPreferences.getString("uid", "");

        if (!uid.equals("")) {
            Log.d(TAG, "onCreateView: " + uid);
            if (starHelper == null) {
                Log.d(TAG, "userHelper: getInstance");
                starHelper = StarOpenHelper.getInstance(getActivity());
            }
            starHelper.openReadLink();
            starHelper.openWriteLink();
            listitem = new ArrayList<>();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Star run: " + uid);
                    if (!uid.equals("") && uid != null && !uid.equals("0")) {
                        //先把提示置空
                        TextView textView = view.findViewById(R.id.text_dashboard);
                        textView.setVisibility(View.GONE);
                        //查找uid=uid的star
                        for (Star star : starHelper.queryAll()) {
                            if (String.valueOf(star.getUid()).equals(uid)) {
                                listitem.add(star);
                            }
                        }

                    }
                }
            }, 500);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}