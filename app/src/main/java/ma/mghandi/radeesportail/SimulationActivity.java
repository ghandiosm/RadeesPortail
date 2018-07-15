package ma.mghandi.radeesportail;

import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class SimulationActivity extends AppCompatActivity {
    private String uid;
    private String password;
    private String database = "RADEEES";
    private String serverAdress = "http://54.36.189.195:8014";
    private long priceConsumptionID;
    private OdooUtility odooUtility;
    private String contract_number;
    private EditText simConsom;
    private Button verifyBtn;
    private String consommation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);
        simConsom = (EditText)findViewById(R.id.simConsommation);
        verifyBtn = (Button) findViewById(R.id.verifyBtn);
        contract_number = SharedData.getKey(SimulationActivity.this, "contract_number");
        setTitle("Consommations - "+contract_number);

        uid = SharedData.getKey(SimulationActivity.this, "uid");
        password = SharedData.getKey(SimulationActivity.this, "password");

        odooUtility = new OdooUtility(serverAdress, "object");

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                consommation = simConsom.getText().toString();

                List data = Arrays.asList(new HashMap(){{
                    put("user_id",Integer.parseInt(uid));
                    put("contract_num",Integer.parseInt(contract_number));
                    put("comsumption",Integer.parseInt(consommation));
                }});

                priceConsumptionID = odooUtility.exec(listner, database, uid, password, "radees.pricing.line","get_price_android", data);
            }
        });



    }

    XMLRPCCallback listner = new XMLRPCCallback() {
        @Override
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if(id == priceConsumptionID){
                Object[] classObjts = (Object[])result;
                int length = classObjts.length;
                if(length > 0){
                    Toast.makeText(SimulationActivity.this, "Result == "+classObjts, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SimulationActivity.this, "Result == Noting", Toast.LENGTH_SHORT).show();
                }
            }else {
                Log.e("Error", "1st Error Partners Not Found");
            }
            Looper.loop();
        }

        @Override
        public void onError(long id, XMLRPCException error) {
            Looper.prepare();
            Log.e("LOGIN", error.getMessage());
            odooUtility.MessageDialog(SimulationActivity.this,"LOGIN Error "+error.getMessage());
            Looper.loop();
        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("LOGIN", "Login Server Error"+error.getMessage());
            odooUtility.MessageDialog(SimulationActivity.this,"Login Server Error "+error.getMessage());
            Looper.loop();
        }
    };


}
