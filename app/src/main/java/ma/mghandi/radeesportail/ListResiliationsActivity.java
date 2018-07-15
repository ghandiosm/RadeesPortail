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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class ListResiliationsActivity extends AppCompatActivity {
    private String libelleContrat;
    private String uid;
    private String password;
    private String database = "RADEEES";
    private String serverAdress = "http://54.36.189.195:8014";
    private long searchTaskID;
    private ListView listViewResilitions;
    private ArrayList<Resiliation> arrayListResilitions;
    private OdooUtility odooUtility;
    private ListResiliationsAdapter resiliationsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_resiliations);
        setTitle("Résiliations");
        libelleContrat = SharedData.getKey(ListResiliationsActivity.this, "contract_number");
        uid = SharedData.getKey(ListResiliationsActivity.this, "uid");
        password = SharedData.getKey(ListResiliationsActivity.this, "password");

        odooUtility = new OdooUtility(serverAdress, "object");

        arrayListResilitions = new ArrayList();
        listViewResilitions = (ListView) findViewById(R.id.list_resiliations);

        // Filters et Champs :

        List conditions = Arrays.asList(Arrays.asList(Arrays.asList("contract_number", "=", libelleContrat)));

        Map fields = new HashMap() {{
            put("fields", Arrays.asList("id", "name", "contract_number", "nature","state"));
        }};

        searchTaskID = odooUtility.search_read(listner, database, uid, password, "crm.cancel", conditions, fields);
    }


    XMLRPCCallback listner = new XMLRPCCallback() {
        @Override
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if(id == searchTaskID){
                Object[] classObjts = (Object[])result;
                int length = classObjts.length;
                if(length > 0){
                    arrayListResilitions.clear();
                    for(int i =0;i<length;i++){
                        @SuppressWarnings("unchecked")
                        Resiliation resiliation = new Resiliation();
                        Map<String,Object> classObj = (Map<String,Object>)classObjts[i];
                        resiliation.setListData(classObj);
                        arrayListResilitions.add(resiliation);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            afficherListResiliations();
                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            msgListResiliationsEmpty();
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
            odooUtility.MessageDialog(ListResiliationsActivity.this,"LOGIN Error "+error.getMessage());
            Looper.loop();
        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("LOGIN", "Login Server Error"+error.getMessage());
            odooUtility.MessageDialog(ListResiliationsActivity.this,"Login Server Error "+error.getMessage());
            Looper.loop();
        }
    };

    public void afficherListResiliations(){
        resiliationsAdapter =new ListResiliationsAdapter(ListResiliationsActivity.this,arrayListResilitions);
        listViewResilitions.setAdapter(resiliationsAdapter);
    }

    public void msgListResiliationsEmpty(){
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
                resiliationsAdapter.getFilter().filter(text);
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
