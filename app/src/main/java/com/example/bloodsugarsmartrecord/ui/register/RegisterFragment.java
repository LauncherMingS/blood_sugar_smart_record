package com.example.bloodsugarsmartrecord.ui.register;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bloodsugarsmartrecord.R;
import com.example.bloodsugarsmartrecord.databinding.FragmentRegisterBinding;

import java.io.File;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //儲存用戶帳密
        binding.registerBtn.setOnClickListener(v -> {
            write();
        });

        //返回登入畫面
        binding.toLoginBtn.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.navigation_login);
        });
//        LoginViewModel notificationsViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    //寫入一組用戶數據
    private void write() {
        //重設提示訊息
        resetMsg();

        //從帳號及密碼欄位取得輸入
        String username = binding.usernameRegister.getText().toString();
        String password = binding.passwordRegister.getText().toString();

        //檢查帳號、密碼是否為空
        if (username.isEmpty() || username.isBlank()) {
            binding.usernameMsgRegister.setText("帳號不得為空");
            return;
        }
        if (password.isEmpty() || password.isBlank()) {
            binding.passwordMsgRegister.setText("密碼不得為空");
            return;
        }

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AccountData", Context.MODE_PRIVATE);

        //檢查帳號是否被註冊過
        if (sharedPreferences.contains(username)) {
            binding.usernameMsgRegister.setText("此帳號已被註冊過了");
            return;
        }

        //儲存資料
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username, password);
        if (editor.commit()) {
            binding.usernameMsgRegister.setText("儲存成功");

            binding.usernameRegister.setText("");
            binding.passwordRegister.setText("");
        }
        else {
            binding.usernameMsgRegister.setText("儲存失敗");
        }
    }

    //透過帳號欄位查詢密碼(字串)
    private void query() {
        //重設提示訊息
        resetMsg();

        //從帳號欄位取得輸入
        String username = binding.usernameRegister.getText().toString();

        //檢查帳號是否為空
        if (username.isEmpty() || username.isBlank()) {
            binding.usernameMsgRegister.setText("查詢帳號不得為空");
            return;
        }

        //透過帳號作為key，取得密碼，並顯示
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AccountData", Context.MODE_PRIVATE);

        //result作為是否查詢到密碼的判斷依據
        String result = sharedPreferences.getString(username, "");
        binding.usernameMsgRegister.setText("您查詢的帳號為: " + username);
        if (result.isEmpty()) {
            binding.passwordMsgRegister.setText("查無此帳號");
        }
        else  {
            binding.passwordMsgRegister.setText(result);
        }
    }

    //移除一組用戶數據
    private  void remove() {
        String username = binding.usernameRegister.getText().toString();

        if (username.isEmpty() || username.isBlank()) {
            binding.usernameMsgRegister.setText("欲刪除的帳號不得為空");
            return;
        }

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("AccountData", Context.MODE_PRIVATE);

        String result = sharedPreferences.getString(username, "");
        binding.usernameMsgRegister.setText("您欲刪除的帳號為: " + username);
        if (result.isEmpty()) {
            binding.passwordMsgRegister.setText("查無此帳號，無法刪除");
        }
        else  {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(username);

            if (editor.commit()) {
                binding.passwordMsgRegister.setText("帳號刪除成功");
            }
            else {
                binding.passwordMsgRegister.setText("帳號刪除失敗");
            }
        }

    }

    private void resetMsg() {
        binding.usernameMsgRegister.setVisibility(View.VISIBLE);
        binding.passwordMsgRegister.setVisibility(View.VISIBLE);

        binding.usernameMsgRegister.setText("");
        binding.passwordMsgRegister.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
