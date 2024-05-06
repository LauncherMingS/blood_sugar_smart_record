package com.example.bloodsugarsmartrecord.ui.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.bloodsugarsmartrecord.R;
import com.example.bloodsugarsmartrecord.databinding.FragmentDashboardBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.SimpleFormatter;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //用戶資料集，用來取得當前的用戶
        SharedPreferences sharedPreferencesLogin = getContext().getSharedPreferences("AccountData", Context.MODE_PRIVATE);
        String loginStatus = sharedPreferencesLogin.getString("loginStatus", "");

        //當前用戶個人的資料集，用來取得當前用戶的血糖記錄，包含時間跟血糖值
        SharedPreferences sharedPreferencesCreate = getContext().getSharedPreferences(loginStatus, Context.MODE_PRIVATE);

        //時間戳記
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日hh點mm分          ");

        //顯示血糖記錄，包含時間跟血糖
        //取得時間資料集

        final LinkedHashSet<String> timeSet = new LinkedHashSet<>(sharedPreferencesCreate.getStringSet("time", new LinkedHashSet<>()));
        //取得血糖資料集
        final Set<String> bloodSugarSet = new HashSet<>(sharedPreferencesCreate.getStringSet("bloodSugar", new HashSet<>()));

        //將每一筆血糖記錄，以按鈕的形式製作，並顯示出來
        String[] times = new String[timeSet.size()];
        times = timeSet.toArray(times);
        String[] records = new String[bloodSugarSet.size()];
        records = bloodSugarSet.toArray(records);

        for (int i = 0;i < times.length;++i) {
            //
            final String time = times[i];
            final String record = records[i];

            //以按鈕的形式，新增血糖記錄
            Button btn = new Button(getContext());
            btn.setText(time + record + " mg/dL");
//                int[][] states = new int[2][];
//                states[0] = new int[]{android.R.attr.state_enabled};
//                states[1] = new int[]{android.R.attr.state_pressed};
            btn.setBackgroundTintList(new ColorStateList(new int[][]{{android.R.attr.state_enabled}}, new int[]{0xFFBB86FC}));
            btn.setTextColor(0xFFFFFFFF);
            btn.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            btn.setLayoutParams(binding.recordLayout.getLayoutParams());

            //設定此按鈕的點擊事件處理程序:針對這筆血糖記錄顯示一個刪除對話框
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                    View alertDialogDeleteView = layoutInflater.inflate(R.layout.alertdialog_deleterecord, null);

                    AlertDialog.Builder alertDialogDeleteBuilder = new AlertDialog.Builder(getContext());
                    alertDialogDeleteBuilder.setView(alertDialogDeleteView);
                    alertDialogDeleteBuilder.setTitle("新增血糖記錄");

                    TextView timeRecordText = alertDialogDeleteView.findViewById(R.id.timeRecordText);
                    TextView bloodSugarRecordText = alertDialogDeleteView.findViewById(R.id.bloodSugarRecordText);

                    timeRecordText.setText(time);
                    bloodSugarRecordText.setText(record);

                    //取消按鈕
                    alertDialogDeleteBuilder.setPositiveButton("返回", null);

                    //刪除按鈕:刪除此筆血糖記錄，並重新導回記錄頁面
                    alertDialogDeleteBuilder.setNegativeButton("刪除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LinkedHashSet<String> alteredTimeSet = new LinkedHashSet<>(timeSet);
                            alteredTimeSet.remove(time);
                            Set<String> alteredBloodSugarSet = new HashSet<>(bloodSugarSet);
                            alteredBloodSugarSet.remove(record);

                            SharedPreferences.Editor userDataEditor = sharedPreferencesCreate.edit();
                            userDataEditor.putStringSet("time", alteredTimeSet);
                            userDataEditor.putStringSet("bloodSugar", alteredBloodSugarSet);
                            userDataEditor.apply();

                            Navigation.findNavController(root).navigate(R.id.navigation_dashboard);
                        }
                    });

                    //將刪除對話框渲染出來
                    alertDialogDeleteBuilder.show();
                }
            });

            //將血糖記錄按鈕設定好後，記得加到要顯示的容器裡面
            binding.recordLayout.addView(btn);
        }

        //設定新增血糖記錄的按鈕
        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Q", Toast.LENGTH_LONG).show();

                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View alertDialogCreateView = layoutInflater.inflate(R.layout.alertdialog_createrecord, null);

                AlertDialog.Builder alertDialogCreateBuilder = new AlertDialog.Builder(getContext());
                alertDialogCreateBuilder.setView(alertDialogCreateView);
                alertDialogCreateBuilder.setTitle("新增血糖記錄");

                //新增按鈕
                alertDialogCreateBuilder.setPositiveButton("新增", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //從血糖欄位取得輸入
                        EditText bloodSugarEdit = alertDialogCreateView.findViewById(R.id.bloodSugarEdit);
                        String bloodSugar = bloodSugarEdit.getText().toString();
                        //取得當前時間
                        Date date = new Date(System.currentTimeMillis());
                        String time = simpleDateFormat.format(date);

                        //先取得先前儲存的記錄，包含時間跟血糖值
                        LinkedHashSet timeSetPrevious = new LinkedHashSet<>(sharedPreferencesCreate.getStringSet("time", new LinkedHashSet<>()));
                        Set<String> bloodSugarSetPrevious = new HashSet<>(sharedPreferencesCreate.getStringSet("bloodSugar", new HashSet<>()));

                        if (bloodSugar.isEmpty()) {
                            Toast.makeText(getContext(), "Cannot create Record. Please input a value!!!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (timeSetPrevious.contains(time)) {
                            Toast.makeText(getContext(), "Cannot create Record. Please wait a minute!!!", Toast.LENGTH_LONG).show();
                            return;
                        }

                        timeSetPrevious.add(time);
                        bloodSugarSetPrevious.add(bloodSugar);

                        SharedPreferences.Editor userDataEditor = sharedPreferencesCreate.edit();
                        //儲存時間
                        userDataEditor.putStringSet("time", timeSetPrevious);
                        //儲存血糖值
                        userDataEditor.putStringSet("bloodSugar", bloodSugarSetPrevious);
                        //設定好要儲存的
                        userDataEditor.apply();

                        //新增完之後記得刷新片段
                        Navigation.findNavController(root).navigate(R.id.navigation_dashboard);
                    }
                });

                //取消按鈕
                alertDialogCreateBuilder.setNegativeButton("取消", null);

                //將新增對話框渲染出來
                alertDialogCreateBuilder.show();

//                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//                alert.setTitle("新增血糖記錄");
//                alert.setMessage("內容");
//                alert.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getContext(), "Q", Toast.LENGTH_LONG).show();
//                    }
//                });
//                alert.setNeutralButton("中立", null);
//                alert.setNegativeButton("取消", null);
//                alert.show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}