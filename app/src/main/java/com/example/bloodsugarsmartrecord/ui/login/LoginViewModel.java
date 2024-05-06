package com.example.bloodsugarsmartrecord.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<String> username = new MutableLiveData<>("no user");

    //注意:ViewModel的用途單純是從Source取得資料，再讓活動或片段藉由此ViewModel取得資料，ViewModel的生命週期是跟著當前的活動或是片段的。
    // 另一個用途是透過即時事件的觸發來刷新資料(LiveData<T>.observe)
    public void setUsername(String username) {
        this.username.setValue(username);
    }


    public LiveData<String> getUsername() {
        return username;
    }
}