package ma.mghandi.radeesportail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class AddContractActivity extends AppCompatActivity {
    private String uid;
    private String password;
    private String database = "RADEEES";
    private String serverAdress = "http://54.36.189.195:8014";
    private long createContractTaskID;
    private OdooUtility odooUtility;
    private EditText numContract;
    private String idGerance="1";
    private String idLocalite="1";
    private Button ajoutContract;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contract);
        setTitle("Ajout Contrat");

        uid = SharedData.getKey(AddContractActivity.this, "uid");
        password = SharedData.getKey(AddContractActivity.this, "password");

        odooUtility = new OdooUtility(serverAdress, "object");

        numContract = (EditText)findViewById(R.id.numContract);
        ajoutContract = (Button) findViewById(R.id.addContactBtn);

        //Retrieve the IDs and Noms Gerances
        Set<String> idGerances = SharedData.getListKey(AddContractActivity.this, "idGerances");
        Set<String> nomsGerances = SharedData.getListKey(AddContractActivity.this, "nomsGerances");

        //Retrieve the IDs and Noms Localites
        Set<String> idLocalites = SharedData.getListKey(AddContractActivity.this, "idLocalites");
        Set<String> nomsLocalites = SharedData.getListKey(AddContractActivity.this, "nomsLocalites");

        // Spinner element
        Spinner gerances = (Spinner) findViewById(R.id.contratGerances);
        Spinner localites = (Spinner) findViewById(R.id.contratLocalites);

        // Spinner click listener
        //gerances.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        /*List&lt;String&gt; categories = new ArrayList&lt;String&gt;();*/
        List<String> listGerancesStatic = new ArrayList<>();
        listGerancesStatic.add("Eau");
        listGerancesStatic.add("Electricite");

        List<String> listLocalitesStatic = new ArrayList<>();
        listLocalitesStatic.add("Safi");
        listLocalitesStatic.add("Jemaa");
        listLocalitesStatic.add("Sebt");
        listLocalitesStatic.add("Telt");


        List<String> listGerances = new ArrayList<>();
        List<String> listLocalites = new ArrayList<>();

        listGerances.addAll(nomsGerances);
        listLocalites.addAll(nomsLocalites);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listGerancesStatic);
        ArrayAdapter<String> dataLocalitesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listLocalitesStatic);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataLocalitesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        gerances.setAdapter(dataAdapter);

        // attaching data adapter to spinner
        localites.setAdapter(dataLocalitesAdapter);

        ajoutContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createContractToOdoo();
            }
        });

    }

    private void createContractToOdoo(){
        List data = Arrays.asList(new HashMap(){{
            put("user_id",Integer.parseInt(uid));
            put("contract_number",numContract.getText().toString());
            put("gerance_id",idGerance);
            put("localite_id",idLocalite);
        }});

        createContractTaskID = odooUtility.create(listner, database, uid, password, "user.contract", data);
    }

    XMLRPCCallback listner = new XMLRPCCallback() {
        @Override
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if(id == createContractTaskID){
                String createResult = result.toString();
                if(createResult != null){
                    Log.v("Reclamation CREATE","Success");
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddContractActivity.this);
                    builder.setMessage("Votre Réclammation a été bien ajouté").setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(AddContractActivity.this,MenuActivity.class);
                            startActivity(intent);
                        }
                    }).create().show();
                }else {
                    odooUtility.MessageDialog(AddContractActivity.this," Creation Failed, Server Return was False ");
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
            odooUtility.MessageDialog(AddContractActivity.this,"LOGIN Error "+error.getMessage());
            Looper.loop();
        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("LOGIN", "Login Server Error"+error.getMessage());
            odooUtility.MessageDialog(AddContractActivity.this,"Login Server Error "+error.getMessage());
            Looper.loop();
        }
    };

}
