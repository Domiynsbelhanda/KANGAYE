package cd.belhanda.kangaye.ui.alerte;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AlerteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AlerteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}