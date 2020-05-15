package com.minemikoto.calculationtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    NavController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = Navigation.findNavController(this,R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this,controller);//返回
    }

    @Override
    public boolean onSupportNavigateUp() { // 给返回箭头添加功能
        if(controller.getCurrentDestination().getId() == R.id.qusetionFragment){ // 进入问答界面出现返回箭头
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setTitle(R.string.quit_dialog_title);// 返回箭头提示语
            builder.setPositiveButton(R.string.dialog_postive_message, new DialogInterface.OnClickListener() { // 选 OK
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    controller.navigateUp();
                }
            });
            builder.setNegativeButton(R.string.dialog_negative_message, new DialogInterface.OnClickListener() { // 选 Cancel
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else if (controller.getCurrentDestination().getId() == R.id.titleFragment) { // 如果是欢迎界面，则退出
            finish();
        }else{ // 除了问答界面按返回会提示，其他界面都会直接回到 欢迎界面，欢迎界面则直接退出
            controller.navigate(R.id.titleFragment); // 回到 欢迎界面
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {//按下返回键做到的事情
        onSupportNavigateUp();
    }
}