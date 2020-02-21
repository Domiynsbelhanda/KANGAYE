package cd.belhanda.kangaye.ui.SOS;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SOSViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SOSViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}