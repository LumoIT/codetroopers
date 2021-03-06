package cth.codetroopers.pixelwarfare.Views;

/**
 * Created by latiif on 5/7/17.
 */

public interface ILogInView extends IView{

    interface LoginViewListener{
        void onRequestLogin(String id);
        void onRequestSignup(String id);
    }

    void setListener(LoginViewListener listener);
}
