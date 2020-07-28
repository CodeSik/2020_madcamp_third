package com.example.madcampweek3.Message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.madcampweek3.R;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindColor;
import butterknife.BindView;

public class ChatMessageFragment extends Fragment {



    @BindView(R.id.usernameInputLayout)
    TextInputLayout usernameInputLayout;
    @BindView(R.id.usernameEditText)
    AppCompatEditText usernameEditText;
    @BindView(R.id.enteredButton)
    AppCompatButton enteredButton;



    @BindColor(R.color.colorWhite)
    int toolbarTitleColor;
    @BindColor(R.color.textEnabled)
    int enableTextColor;
    @BindColor(R.color.textDisabled)
    int disableTextColor;

    private ChatMessageViewModel mViewModel;

    public static ChatMessageFragment newInstance() {
        return new ChatMessageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_message_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChatMessageViewModel.class);
        // TODO: Use the ViewModel
    }

}