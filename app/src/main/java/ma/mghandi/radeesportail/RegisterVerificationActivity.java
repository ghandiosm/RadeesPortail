package ma.mghandi.radeesportail;

import android.content.Intent;
import android.os.Looper;
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

public class RegisterVerificationActivity extends AppCompatActivity {
    private String numeroContrat;
    private String geranceVar;
    private String localiteVar;
    private String uid;
    private String password;
    private String database = "RADEEES";
    private String serverAdress = "http://54.36.189.195:8014";
    private long searchTaskID;
    private ListView listViewContracts;
    private ArrayList<Contract> arrayListContracts;
    private OdooUtility odooUtility;
    private ListContractsAdapter contractsAdapter;
    Button verifyBtn;
    EditText numContrat, localite, gerance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verification);
        setTitle("Vérification");
        verifyBtn = (Button)findViewById(R.id.verifyBtn);
        numContrat = (EditText)findViewById(R.id.customerCode);
        localite = (EditText)findViewById(R.id.customerLocalite);
        gerance = (EditText)findViewById(R.id.customerGerance);


        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numContrat.getText().toString().isEmpty() || localite.getText().toString().isEmpty() || gerance.getText().toString().isEmpty()){
                    Toast.makeText(RegisterVerificationActivity.this, "Merci de saisir tout les données nécessaires pour la vérification", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterVerificationActivity.this,MainActivity.class);
                    startActivity(intent);
                }else {

                    uid = "1";
                    password = "admin";
                    numeroContrat = numContrat.getText().toString();
                    geranceVar = gerance.getText().toString();
                    localiteVar = localite.getText().toString();
                    odooUtility = new OdooUtility(serverAdress, "object");

                    arrayListContracts = new ArrayList();
                    listViewContracts = (ListView) findViewById(R.id.list_contracts);

                    List conditions = Arrays.asList(Arrays.asList(Arrays.asList("contract_number", "=", numeroContrat),Arrays.asList("localite_id", "=", 1),Arrays.asList("gerance_id", "=", 1)));

                    Map fields = new HashMap() {{
                        put("fields", Arrays.asList("id", "tourne_num","contract_number"));
                    }};

                    searchTaskID = odooUtility.search_read(listner, database, uid, password, "user.contract", conditions, fields);

                }
            }
        });
    }

    XMLRPCCallback listner = new XMLRPCCallback() {
        @Override
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if(id == searchTaskID){
                Object[] classObjts = (Object[])result;
                int length = classObjts.length;
                if(length > 0){
                    Intent intent = new Intent(RegisterVerificationActivity.this,RegisterActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(RegisterVerificationActivity.this, "Les Inputs données ne corespondent à aucun contrat, Merci de les vérifier ...", Toast.LENGTH_SHORT).show();
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
            odooUtility.MessageDialog(RegisterVerificationActivity.this,"LOGIN Error "+error.getMessage());
            Looper.loop();
        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("LOGIN", "Login Server Error"+error.getMessage());
            odooUtility.MessageDialog(RegisterVerificationActivity.this,"Login Server Error "+error.getMessage());
            Looper.loop();
        }
    };


}
