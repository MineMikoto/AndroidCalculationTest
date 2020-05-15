package com.minemikoto.calculationtest;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.MutableLiveData;

import java.util.Random;

public class MyViewModel extends AndroidViewModel {
    private SavedStateHandle handle;
    private static String KEY_HIGH_SCORE = "key_high_score";// 最高分
    private static String KEY_LEFT_NUMBER = "key_left_number";// 运算符左边数字
    private static String KEY_RIGHT_NUMBER = "key_right_number";// 运算符右边数字
    private static String KEY_OPERATOR = "key_operator";// 运算符
    private static String KEY_ANSWER = "key_answer";// 运算结果
    private static String KEY_CURRENT_SCORE = "key_current_score";//当前分数
    private static String SAVE_SHP_DATA_NAME = "save_shp_data_name";// SharedPreferences 需要的常量
    boolean win_flag = false;   // 获胜状态，为 true 则当前为获胜，false 则当前为失败

    public MyViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        // 最高分是需要被永久存储的数据，如果没有存储，说明是第一次运行，则将所有数据初始化
        if (!handle.contains(KEY_HIGH_SCORE)) {//是否有值
            SharedPreferences shp = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME, Context.MODE_PRIVATE);
            handle.set(KEY_HIGH_SCORE, shp.getInt(KEY_HIGH_SCORE, 0));
            handle.set(KEY_LEFT_NUMBER, 0);
            handle.set(KEY_RIGHT_NUMBER, 0);
            handle.set(KEY_OPERATOR, "+");
            handle.set(KEY_ANSWER, 0);
            handle.set(KEY_CURRENT_SCORE, 0);
        }
        this.handle = handle;
    }

    public MutableLiveData<Integer> getHighScore() {
        return handle.getLiveData(KEY_HIGH_SCORE);
    }

    public MutableLiveData<Integer> getCurrentScore() {
        return handle.getLiveData(KEY_CURRENT_SCORE);
    }

    public MutableLiveData<Integer> getLeftNumber() {
        return handle.getLiveData(KEY_LEFT_NUMBER);
    }

    public MutableLiveData<Integer> getRightNumber() {
        return handle.getLiveData(KEY_RIGHT_NUMBER);
    }

    public MutableLiveData<String> getOperator() {
        return handle.getLiveData(KEY_OPERATOR);
    }

    public MutableLiveData<Integer> getAnswer() {
        return handle.getLiveData(KEY_ANSWER);
    }

    void generator() {// 生成一道题目
        int LEVEL = 20;//难度
        Random random = new Random();
        int x, y;
        x = random.nextInt(LEVEL) + 1; // x 为 1 到 LEVEL-1 的随机数
        y = random.nextInt(LEVEL) + 1; // y 也为 1 到 LEVEL-1 的随机数
        if (x % 2 == 0) {
            getOperator().setValue("+"); // x 为偶数则运算符为"+"
            if (x > y) {
                getAnswer().setValue(x); // 将较大的数设为答案，则加数与被加数都可以表达出来
                getLeftNumber().setValue(y);
                getRightNumber().setValue(x - y);
            } else {
                getAnswer().setValue(y);
                getLeftNumber().setValue(x);
                getRightNumber().setValue(y - x);
            }
        } else {
            getOperator().setValue("-"); // x 不是偶数则运算符为"-"
            if (x > y) {
                getLeftNumber().setValue(x);
                getRightNumber().setValue(y);
                getAnswer().setValue(x - y);
            } else {
                getLeftNumber().setValue(y);
                getRightNumber().setValue(x);
                getAnswer().setValue(y - x);
            }
        }
    }

    void save() {//存储数据
        SharedPreferences shp = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putInt(KEY_HIGH_SCORE, getHighScore().getValue());
        editor.apply();
    }

    void answerCorrect() { // 答对问题
        getCurrentScore().setValue(getCurrentScore().getValue() + 1); // 当前分数 +1
        if (getCurrentScore().getValue() > getHighScore().getValue()) { // 如果当前分数比最高分要高
            getHighScore().setValue(getCurrentScore().getValue());  // 将当前分设为最高分
            win_flag = true; // 将状态设置为获胜
        }
        generator(); // 生成一道新题
    }
}