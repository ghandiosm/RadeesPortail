package ma.mghandi.radeesportail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class ListReclamationsActivity extends AppCompatActivity {
    private String uid;
    private String partnerid;
    private String password;
    private String database = "RADEEES";
    private String serverAdress = "http://54.36.189.195:8014";
    private long searchTaskID;
    private ListView listViewReclamations;
    private ArrayList<Reclamation> arrayListReclamations;
    private OdooUtility odooUtility;
    private ListReclamationsAdapter reclamationsAdapter;
    FloatingActionButton addReclamation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reclamations);
        setTitle("Réclamations");
        addReclamation = (FloatingActionButton) findViewById(R.id.addReclamation);

        uid = SharedData.getKey(ListReclamationsActivity.this, "uid");
        password = SharedData.getKey(ListReclamationsActivity.this, "password");

        odooUtility = new OdooUtility(serverAdress, "object");
        arrayListReclamations = new ArrayList();
        listViewReclamations = (ListView) findViewById(R.id.list_reclamations);

        // Conditions et Champs à Lister

        List conditions = Arrays.asList(Arrays.asList(Arrays.asList("user_id", "=", Integer.parseInt(uid))));

        Map fields = new HashMap() {{
            put("fields", Arrays.asList("id", "name","cin","date"));
        }};

        searchTaskID = odooUtility.search_read(listner, database, uid, password, "crm.claim", conditions, fields);


        addReclamation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListReclamationsActivity.this,AddReclamationActivity.class);
                startActivity(intent);
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
                    arrayListReclamations.clear();
                    for(int i =0;i<length;i++){
                        @SuppressWarnings("unchecked")
                        Reclamation reclamation = new Reclamation();
                        Map<String,Object> classObj = (Map<String,Object>)classObjts[i];
                        reclamation.setListData(classObj);
                        arrayListReclamations.add(reclamation);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            afficherListReclamations();
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            msgListReclamationsEmpty();
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
            odooUtility.MessageDialog(ListReclamationsActivity.this,"LOGIN Error "+error.getMessage());
            Looper.loop();
        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("LOGIN", "Login Server Error"+error.getMessage());
            odooUtility.MessageDialog(ListReclamationsActivity.this,"Login Server Error "+error.getMessage());
            Looper.loop();
        }
    };

    public void afficherListReclamations(){
        reclamationsAdapter =new ListReclamationsAdapter(ListReclamationsActivity.this,arrayListReclamations);
        listViewReclamations.setAdapter(reclamationsAdapter);
    }
    public void msgListReclamationsEmpty(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Aucun résultat pour votre recherche").setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String text = query.toString().toLowerCase(Locale.getDefault());
                reclamationsAdapter.getFilter().filter(text);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onQueryTextSubmit(newText);
                return false;
            }
        });

        return true;
    }
}
