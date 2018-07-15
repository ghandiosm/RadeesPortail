package ma.mghandi.radeesportail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class RegisterActivity extends AppCompatActivity {
    String URL_SignUp_Radees = "http://54.36.189.195:8014/web/signup";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Enregistrement");

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl(URL_SignUp_Radees);
    }
}
