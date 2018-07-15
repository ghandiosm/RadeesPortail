package ma.mghandi.radeesportail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Looper;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class ListDemandesActivity extends AppCompatActivity {
    private String libelleContrat;
    private String uid;
    private String partnerid;
    private String password;
    private String database = "RADEEES";
    private String serverAdress = "http://54.36.189.195:8014";
    private long searchTaskID;
    private ListView listViewDemandes;
    private ArrayList<Demande> arrayListDemandes;
    private OdooUtility odooUtility;
    private ListDemandesAdapter demandesAdapter;
    private EditText editNomDemande;
    private Button chercherDemande;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_demandes);
        libelleContrat = SharedData.getKey(ListDemandesActivity.this, "contract_number");
        setTitle("Demandes");
        partnerid = SharedData.getKey(ListDemandesActivity.this, "partner_id");
        uid = SharedData.getKey(ListDemandesActivity.this, "uid");
        password = SharedData.getKey(ListDemandesActivity.this, "password");
        Log.e("PARTNER ID IS  ",partnerid);
        odooUtility = new OdooUtility(serverAdress, "object");

        arrayListDemandes = new ArrayList();
        listViewDemandes = (ListView) findViewById(R.id.list_demandes);
        chercherDemande = (Button) findViewById(R.id.searchBtn);
        editNomDemande = (EditText) findViewById(R.id.numDemande);

        // Filters et Champs :

        chercherDemande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewDemandes.setAdapter(null);
                String nomReclamation="";
                nomReclamation = editNomDemande.getText().toString();
                List conditions = Arrays.asList(Arrays.asList(Arrays.asList("name", "=", nomReclamation)));

                Map fields = new HashMap() {{
                    put("fields", Arrays.asList("id", "name", "contract_number", "nature", "date"));
                }};

                searchTaskID = odooUtility.search_read(listner, database, uid, password, "crm.demand", conditions, fields);
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
                    arrayListDemandes.clear();
                    for(int i =0;i<length;i++){
                        @SuppressWarnings("unchecked")
                        Demande demande = new Demande();
                        Map<String,Object> classObj = (Map<String,Object>)classObjts[i];
                        demande.setListData(classObj);
                        arrayListDemandes.add(demande);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            afficherListDemandes();
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            msgListDemandesEmpty();
                        }
                    });
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
            odooUtility.MessageDialog(ListDemandesActivity.this,"LOGIN Error "+error.getMessage());
            Looper.loop();
        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("LOGIN", "Login Server Error"+error.getMessage());
            odooUtility.MessageDialog(ListDemandesActivity.this,"Login Server Error "+error.getMessage());
            Looper.loop();
        }
    };

    public void afficherListDemandes(){
        demandesAdapter =new ListDemandesAdapter(ListDemandesActivity.this,arrayListDemandes);
        listViewDemandes.setAdapter(demandesAdapter);
    }

    public void msgListDemandesEmpty(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Aucun résultat pour votre recherche").setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

}
