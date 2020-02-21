package cd.belhanda.kangaye.ui.alerte;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import cd.belhanda.kangaye.R;

public class AlerteFragment extends Fragment {

    private AlerteViewModel alerteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        alerteViewModel =
                ViewModelProviders.of(this).get(AlerteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_alerte, container, false);
        alerteViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });


        return root;
    }
}