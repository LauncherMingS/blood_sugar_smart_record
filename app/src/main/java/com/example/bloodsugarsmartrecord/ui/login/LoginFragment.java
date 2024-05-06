package com.example.bloodsugarsmartrecord.ui.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavType;
import androidx.navigation.Navigation;

import com.example.bloodsugarsmartrecord.MainActivity;
import com.example.bloodsugarsmartrecord.R;
import com.example.bloodsugarsmartrecord.databinding.FragmentLoginBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
//        loginViewModel.getUsername().observe(this.getViewLifecycleOwner(), binding.usernameMsgLogin::setText);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //綁定密碼輸入欄位的監聽事件及處理程序
        binding.passwordLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //如果輸入Enter鍵，直接呼叫Login的按鈕處理程序
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.loginBtn.callOnClick();
                    return false;
                }

                //回傳true表示保留鍵盤，false表示收起鍵盤
                return true;
            }
        });

        //綁定登入按紐的監聽事件及處理程序
        binding.loginBtn.setOnClickListener(v -> {
            //如果驗證成功，執行兩個動作
            if (verifyAccount()) {
                //顯示底部導覽
                getActivity().findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
//              執行淡出動畫；動畫結束時，透過導向控制器將Fragment導到Home
                binding.loginLayout.animate().alpha(0f).setDuration(2000).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public  void onAnimationEnd(Animator animator) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("username", verifiedResult);
//                        navController.navigate(R.id.LoginToHomeAction);

                        NavController navController = Navigation.findNavController(root);
//                        這是導向控制器將當前片段導向目標片段的事件，觸發此事件時的處理程序
//                        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
//                            @Override
//                            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
//                                navDestination.addArgument("username", new NavArgument.Builder().setDefaultValue(verifiedResult).build());
//                            }
//                        });
                        navController.navigate(R.id.LoginToHomeAction);
                    }
                });
            }
        });

        //綁定註冊按鈕的監聽事件及處理程序
        binding.toRegisterBtn.setOnClickListener(v -> {
            //透過導向控制器將Fragment導到Register
            Navigation.findNavController(root).navigate(R.id.LoginToRegisterAction);
        });

        //加快測試用
        binding.usernameLogin.setText("user1");
        binding.passwordLogin.setText("12345");

        return root;
    }

    //驗證帳密的函式，只有驗證的動作，驗證會回傳結果，不會做導向等等其他任何動作
    private Boolean verifyAccount() {
        //重設提示訊息
        resetMsg();

        //從帳號及密碼欄位取得輸入
        String username = binding.usernameLogin.getText().toString();
        String password = binding.passwordLogin.getText().toString();

        //檢查帳號、密碼是否為空
        if (username.isEmpty() || username.isBlank()) {
            binding.usernameMsgLogin.setText("登入帳號不得為空");
            return false;
        }
        if (password.isEmpty() || password.isBlank()) {
            binding.passwordMsgLogin.setText("登入密碼不得為空");
            return false;
        }

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AccountData", Context.MODE_PRIVATE);

        //檢查帳號是否被註冊過
        if (!sharedPreferences.contains(username)) {
            binding.usernameMsgLogin.setText("查無此帳號");
            return false;
        }

        //驗證
        String result = sharedPreferences.getString(username, "");
        if (!result.equals(password)) {
            binding.usernameMsgLogin.setText("您登入的帳號為: " + username);
            binding.passwordMsgLogin.setText("密碼錯誤");
            return false;
        }

        //顯示登入成功訊息
        binding.usernameMsgLogin.setText("登入成功");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loginStatus", username);
        editor.apply();

        return true;
    }

    private void resetMsg() {
        binding.usernameMsgLogin.setVisibility(View.VISIBLE);
        binding.passwordMsgLogin.setVisibility(View.VISIBLE);

        binding.usernameMsgLogin.setText("");
        binding.passwordMsgLogin.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}