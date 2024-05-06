package com.example.bloodsugarsmartrecord.ui.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bloodsugarsmartrecord.MainActivity;
import com.example.bloodsugarsmartrecord.R;
import com.example.bloodsugarsmartrecord.databinding.FragmentHomeBinding;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        checkLoginStatus();

        return root;
    }

    //確認登入狀態並更新文字
    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AccountData", Context.MODE_PRIVATE);
        String loginUsername = sharedPreferences.getString("loginStatus", "");
        if (loginUsername.isEmpty()) {
            binding.welcomeText.setText("登入失敗，請點擊右上角設定中的登出，嘗試再登入一次");
        }
        else {
            binding.welcomeText.setText("Hi~ " + loginUsername);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}