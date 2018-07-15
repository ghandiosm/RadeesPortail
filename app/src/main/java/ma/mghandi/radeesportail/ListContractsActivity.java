package ma.mghandi.radeesportail;

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
import android.widget.AdapterView;
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

public class ListContractsActivity extends AppCompatActivity {
    private String comming_from;
    private String uid;
    private String password;
    private String database = "RADEEES";
    private String serverAdress = "http://54.36.189.195:8014";
    private long searchTaskID;
    private ListView listViewContracts;
    private ArrayList<Contract> arrayListContracts;
    private OdooUtility odooUtility;
    private ListContractsAdapter contractsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contracts);

        setTitle("Contrats");

        uid = SharedData.getKey(ListContractsActivity.this, "uid");
        password = SharedData.getKey(ListContractsActivity.this, "password");
        comming_from = SharedData.getKey(ListContractsActivity.this, "coming_from");

        odooUtility = new OdooUtility(serverAdress, "object");

        arrayListContracts = new ArrayList();
        listViewContracts = (ListView) findViewById(R.id.list_contracts);

        List conditions = Arrays.asList(Arrays.asList(Arrays.asList("user_id", "=", Integer.parseInt(uid))));

        Map fields = new HashMap() {{
            put("fields", Arrays.asList("id", "tourne_num","contract_number"));
        }};
        searchTaskID = odooUtility.search_read(listner, database, uid, password, "user.contract", conditions, fields);
    }

    XMLRPCCallback listner = new XMLRPCCallback() {
        @Override
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if(id == searchTaskID){
                Object[] classObjts = (Object[])result;
                int length = classObjts.length;
                if(length > 0){
                    arrayListContracts.clear();
                    for(int i =0;i<length;i++){
                        @SuppressWarnings("unchecked")
                        Contract contract = new Contract();
                        Map<String,Object> classObj = (Map<String,Object>)classObjts[i];
                        contract.setListData(classObj);
                        arrayListContracts.add(contract);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            afficherListContracts();
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
            odooUtility.MessageDialog(ListContractsActivity.this,"LOGIN Error "+error.getMessage());
            Looper.loop();
        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("LOGIN", "Login Server Error"+error.getMessage());
            odooUtility.MessageDialog(ListContractsActivity.this,"Login Server Error "+error.getMessage());
            Looper.loop();
        }
    };

    public void afficherListContracts(){
        contractsAdapter =new ListContractsAdapter(ListContractsActivity.this,arrayListContracts);
        listViewContracts.setAdapter(contractsAdapter);
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
                contractsAdapter.getFilter().filter(text);
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
