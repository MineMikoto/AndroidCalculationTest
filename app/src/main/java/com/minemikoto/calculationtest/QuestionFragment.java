package com.minemikoto.calculationtest;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.minemikoto.calculationtest.databinding.FragmentQusetionBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionFragment extends Fragment {

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final MyViewModel myViewModel;
        myViewModel = new ViewModelProvider(requireActivity(), new SavedStateViewModelFactory(requireActivity().getApplication(),this)).get(MyViewModel.class);
        myViewModel.generator(); // 出题
        myViewModel.getCurrentScore().setValue(0); // 重新开始则置零
        final FragmentQusetionBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qusetion, container, false);
        binding.setData(myViewModel);
        binding.setLifecycleOwner(this);
        final StringBuilder builder = new StringBuilder();
        View.OnClickListener listener = new View.OnClickListener() { // 按下 数字键 以及 清零键 的事件
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.button0:
                        builder.append("0");
                        break;
                    case R.id.button1:
                        builder.append("1");
                        break;
                    case R.id.button2:
                        builder.append("2");
                        break;
                    case R.id.button3:
                        builder.append("3");
                        break;
                    case R.id.button4:
                        builder.append("4");
                        break;
                    case R.id.button5:
                        builder.append("5");
                        break;
                    case R.id.button6:
                        builder.append("6");
                        break;
                    case R.id.button7:
                        builder.append("7");
                        break;
                    case R.id.button8:
                        builder.append("8");
                        break;
                    case R.id.button9:
                        builder.append("9");
                        break;
                    case R.id.buttonClear: // 如果按了清零键
                        builder.setLength(0); // 将可变字符串清零
                        break;
                }
                if(builder.length() == 0){
                    binding.textView10.setText(getString(R.string.input_indicator));
                } else {
                    binding.textView10.setText(builder);
                }
            }
        };

        binding.button0.setOnClickListener(listener);
        binding.button1.setOnClickListener(listener);
        binding.button2.setOnClickListener(listener);
        binding.button3.setOnClickListener(listener);
        binding.button4.setOnClickListener(listener);
        binding.button5.setOnClickListener(listener);
        binding.button6.setOnClickListener(listener);
        binding.button7.setOnClickListener(listener);
        binding.button8.setOnClickListener(listener);
        binding.button9.setOnClickListener(listener);
        binding.buttonClear.setOnClickListener(listener);
        binding.buttonSubmmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(builder.toString().equals("")) {
                    Toast.makeText(getContext(),"你没做题",Toast.LENGTH_SHORT).show();
                }else {
                    if (Integer.valueOf(builder.toString()).intValue() == myViewModel.getAnswer().getValue()) {
                        myViewModel.answerCorrect();
                        builder.setLength(0);
                        binding.textView10.setText(getResources().getString(R.string.anwser_correct_message));
                        //builder.append(getString(R.string.anwser_correct_message));

                    } else {
                        NavController controller = Navigation.findNavController(v);
                        if (myViewModel.win_flag) {
                            controller.navigate(R.id.action_qusetionFragment_to_winFragment);
                            myViewModel.win_flag = false;
                            myViewModel.save();
                        } else {
                            controller.navigate(R.id.action_qusetionFragment_to_loseFragment);
                        }

                    }
                }
            }
        });
        return binding.getRoot();
    }
}