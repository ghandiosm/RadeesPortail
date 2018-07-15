package ma.mghandi.radeesportail;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class LoginActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;
    private long loginTaskID;
    OdooUtility odooUtility;
    EditText editUsername;
    EditText editPassword;
    Button login;
    private String database = "RADEEES";
    private String serverAdress = "http://54.36.189.195:8014";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        // Slider Layout

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Radees", R.drawable.slider);


        for (String name : file_maps.keySet()) {
              /*
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout

            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);

            */

            CustomSliderView c = new CustomSliderView(this);
            c.image(file_maps.get(name));
            mDemoSlider.addSlider(c);

            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

            mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));

            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(this);

            PagerIndicator indic = (PagerIndicator) findViewById(R.id.custom_indicator);
            //indic.po

            mDemoSlider.setCustomIndicator(indic);
        }

        // End Slider Layout

        editUsername = (EditText) findViewById(R.id.login);
        editPassword = (EditText) findViewById(R.id.mot_de_pass);
        login = (Button) findViewById(R.id.loginBtn);

        String username = SharedData.getKey(LoginActivity.this, "username");
        String password = SharedData.getKey(LoginActivity.this, "password");

        editUsername.setText(username);
        editPassword.setText(password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();

                odooUtility = new OdooUtility(serverAdress, "common");
                loginTaskID = odooUtility.login(listener, database, username, password);

                SharedData.setKey(LoginActivity.this, "password", password);
                SharedData.setKey(LoginActivity.this, "username", username);
                SharedData.setKey(LoginActivity.this, "serverAddress", serverAdress);
                SharedData.setKey(LoginActivity.this, "database", database);
            }
        });



    }

    XMLRPCCallback listener = new XMLRPCCallback() {
        @Override
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if (id == loginTaskID) {
                if (result instanceof Boolean && (Boolean) result == false) {
                    Log.e("MessageErr", "Login Error Please Try Again");
                } else {
                    String uid = result.toString();
                    SharedData.setKey(LoginActivity.this, "uid", uid);
                    Log.e("MessageSucc", "Login Succed uid = " + uid);
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                }
            }
            Looper.loop();
        }

        @Override
        public void onError(long id, XMLRPCException error) {
            Looper.prepare();
            Log.e("LOGIN", error.getMessage());
            odooUtility.MessageDialog(LoginActivity.this, "LOGIN Error " + error.getMessage());
            Looper.loop();
        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("LOGIN", "Login Server Error " + error.getMessage());
            odooUtility.MessageDialog(LoginActivity.this, "Login Server Error " + error.getMessage());
            Looper.loop();
        }
    };

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            /*case R.id.action_custom_indicator:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                break;
            case R.id.action_custom_child_animation:
                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
                break;
            case R.id.action_restore_default:
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                break;*/
            case R.id.action_register:
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
