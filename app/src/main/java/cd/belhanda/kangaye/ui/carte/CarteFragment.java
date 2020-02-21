package cd.belhanda.kangaye.ui.carte;

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

public class CarteFragment extends Fragment {

    private CarteViewModel carteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        carteViewModel =
                ViewModelProviders.of(this).get(CarteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_carte, container, false);
        carteViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });


        return root;
    }
}