package ma.mghandi.radeesportail;

import android.content.Intent;
import android.os.Looper;
import android.os.Parcelable;
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

public class ListConsommationsActivity extends AppCompatActivity {
    private String contract_id;
    private String contract_number;
    private String tourne_number;
    private String uid;
    private String password;
    private String database = "RADEEES";
    private String serverAdress = "http://54.36.189.195:8014";
    private long searchTaskID;
    private ListView listViewConsommations;
    private ArrayList<Consommation> arrayListConsommations;
    private OdooUtility odooUtility;
    private ListConsommationsAdapter consommationsAdapter;
    /**** Periods de Consommations ***/
    private ArrayList<String> arrayListPeriodsConsommation;
    /**** Prix de Consommations ***/
    private ArrayList<String> arrayListPrixConsommation;
    /*********************/
    FloatingActionButton viewConsomGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_consommations);
        viewConsomGraph = (FloatingActionButton) findViewById(R.id.buttonViewGraph);
        contract_number = SharedData.getKey(ListConsommationsActivity.this, "contract_number");
        setTitle("Consommations - "+contract_number);

        uid = SharedData.getKey(ListConsommationsActivity.this, "uid");
        password = SharedData.getKey(ListConsommationsActivity.this, "password");

        odooUtility = new OdooUtility(serverAdress, "object");

        arrayListConsommations = new ArrayList();
        // Initialising Lists :
        arrayListPeriodsConsommation = new ArrayList<>();
        arrayListPrixConsommation = new ArrayList<>();

        listViewConsommations = (ListView) findViewById(R.id.list_consommations);

        List conditions = Arrays.asList(Arrays.asList(Arrays.asList("name", "=", Integer.parseInt(contract_number))));

        Map fields = new HashMap() {{
            put("fields", Arrays.asList("id", "name","tourne_num","period","index","consumption"));
        }};
        searchTaskID = odooUtility.search_read(listner, database, uid, password, "crm.consumption", conditions, fields);


        viewConsomGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListConsommationsActivity.this,ConsommationsGraphActivity.class);
                SharedData.saveArrayList(ListConsommationsActivity.this,"consommationPeriods",arrayListPeriodsConsommation);
                SharedData.saveArrayList(ListConsommationsActivity.this,"consommationPrix",arrayListPrixConsommation);
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
                    arrayListConsommations.clear();
                    for(int i =0;i<length;i++){
                        @SuppressWarnings("unchecked")
                        Consommation consommation = new Consommation();
                        Map<String,Object> classObj = (Map<String,Object>)classObjts[i];
                        // Passage des Données au Graphes
                        String consomPeriod,consomPrix;
                        consomPeriod = classObj.get("period").toString();
                        consomPrix = classObj.get("consumption").toString();
                        arrayListPeriodsConsommation.add(consomPeriod);
                        arrayListPrixConsommation.add(consomPrix);
                        // End Passage Données
                        consommation.setListData(classObj);
                        arrayListConsommations.add(consommation);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            afficherListConsommations();
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
            odooUtility.MessageDialog(ListConsommationsActivity.this,"LOGIN Error "+error.getMessage());
            Looper.loop();
        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("LOGIN", "Login Server Error"+error.getMessage());
            odooUtility.MessageDialog(ListConsommationsActivity.this,"Login Server Error "+error.getMessage());
            Looper.loop();
        }
    };

    public void afficherListConsommations(){
        consommationsAdapter =new ListConsommationsAdapter(ListConsommationsActivity.this,arrayListConsommations);
        listViewConsommations.setAdapter(consommationsAdapter);
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
                consommationsAdapter.getFilter().filter(text);
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
