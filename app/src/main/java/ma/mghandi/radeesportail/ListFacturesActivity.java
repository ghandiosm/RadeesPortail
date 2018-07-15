package ma.mghandi.radeesportail;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class ListFacturesActivity extends AppCompatActivity {
    private String uid;
    private String password;
    private String database = "HOTEL";
    private String serverAdress = "http://54.36.189.195:8014";
    private long searchTaskID;
    private ListView listViewContracts;
    private ArrayList<Contract> arrayListContracts;
    private OdooUtility odooUtility;
    private ListContractsAdapter contractsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_factures);


        uid = SharedData.getKey(ListFacturesActivity.this, "uid");
        password = SharedData.getKey(ListFacturesActivity.this, "password");

        odooUtility = new OdooUtility(serverAdress, "object");

        arrayListContracts = new ArrayList();
        listViewContracts = (ListView) findViewById(R.id.list_contracts);

        List conditions = Arrays.asList(Arrays.asList(Arrays.asList("state", "=", "open")));

        Map fields = new HashMap() {{
            put("fields", Arrays.asList("id", "number","date_invoice","amount_total"));
        }};
        searchTaskID = odooUtility.search_read(listner, database, uid, password, "account.invoice", conditions, fields);
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
                            afficherListPartners();
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
            odooUtility.MessageDialog(ListFacturesActivity.this,"LOGIN Error "+error.getMessage());
            Looper.loop();
        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("LOGIN", "Login Server Error"+error.getMessage());
            odooUtility.MessageDialog(ListFacturesActivity.this,"Login Server Error "+error.getMessage());
            Looper.loop();
        }
    };

    public void afficherListPartners(){
        contractsAdapter =new ListContractsAdapter(ListFacturesActivity.this,arrayListContracts);
        listViewContracts.setAdapter(contractsAdapter);
    }
}
