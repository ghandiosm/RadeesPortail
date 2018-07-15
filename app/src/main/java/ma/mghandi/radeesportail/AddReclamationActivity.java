package ma.mghandi.radeesportail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class AddReclamationActivity extends AppCompatActivity {
    private String uid;
    private String password;
    private String database = "RADEEES";
    private String serverAdress = "http://54.36.189.195:8014";
    private long createReclamationTaskID;
    private OdooUtility odooUtility;
    private EditText tourneReclammation,policeReclammation,adresseReclammation,repereReclammation;
    private Button ajoutReclamation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reclamation);
        setTitle("Ajout Réclamation");

        uid = SharedData.getKey(AddReclamationActivity.this, "uid");
        password = SharedData.getKey(AddReclamationActivity.this, "password");

        odooUtility = new OdooUtility(serverAdress, "object");

        tourneReclammation = (EditText)findViewById(R.id.tourneReclammation);
        policeReclammation = (EditText)findViewById(R.id.policeReclammation);
        adresseReclammation = (EditText)findViewById(R.id.adresseReclammation);
        repereReclammation = (EditText)findViewById(R.id.repereReclammation);
        ajoutReclamation = (Button) findViewById(R.id.ajoutReclamation);

        ajoutReclamation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReclamationToOdoo();
            }
        });




    }

    private void createReclamationToOdoo(){
        List data = Arrays.asList(new HashMap(){{
            put("user_id",Integer.parseInt(uid));
            put("tourne",tourneReclammation.getText().toString());
            put("police",policeReclammation.getText().toString());
            put("landmark",repereReclammation.getText().toString());
            put("address",adresseReclammation.getText().toString());
        }});

        createReclamationTaskID = odooUtility.create(listner, database, uid, password, "crm.claim", data);
    }

    XMLRPCCallback listner = new XMLRPCCallback() {
        @Override
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if(id == createReclamationTaskID){
                String createResult = result.toString();
                if(createResult != null){
                    Log.v("Reclamation CREATE","Success");
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddReclamationActivity.this);
                    builder.setMessage("Votre Réclammation a été bien ajouté").setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(AddReclamationActivity.this,ListReclamationsActivity.class);
                            startActivity(intent);
                        }
                    }).create().show();
                }else {
                    odooUtility.MessageDialog(AddReclamationActivity.this," Creation Failed, Server Return was False ");
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
            odooUtility.MessageDialog(AddReclamationActivity.this,"LOGIN Error "+error.getMessage());
            Looper.loop();
        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("LOGIN", "Login Server Error"+error.getMessage());
            odooUtility.MessageDialog(AddReclamationActivity.this,"Login Server Error "+error.getMessage());
            Looper.loop();
        }
    };
}
