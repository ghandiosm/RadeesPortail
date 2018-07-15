package ma.mghandi.radeesportail;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Looper;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCException;
import de.timroes.axmlrpc.XMLRPCServerException;

public class MenuActivity extends AppCompatActivity {
    // Searching ... Partner ID : variables
    private String libelleContrat;
    private String uid;
    private String password;
    private String database = "RADEEES";
    private String serverAdress = "http://54.36.189.195:8014";
    private long searchTaskID;
    private long searchLocaliteID;
    private OdooUtility odooUtility;
    /**** Gerances IDS et Noms ***/
    private ArrayList<String> arrayListIDsGerances;
    private ArrayList<String> arrayListNamesGerances;
    /**** Localites IDS et Noms ***/
    private ArrayList<String> arrayListIDsLocalites;
    private ArrayList<String> arrayListNamesLocalites;

    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private List<ItemMenu> albumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setTitle("Menu");
        // Searching ... Partner ID : get user id and password
        uid = SharedData.getKey(MenuActivity.this, "uid");
        password = SharedData.getKey(MenuActivity.this, "password");

        odooUtility = new OdooUtility(serverAdress, "object");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Filters et Champs :

        List conditions = Arrays.asList(Arrays.asList(Arrays.asList("id", "!=", 0)));

        Map fields = new HashMap() {{
            put("fields", Arrays.asList("id", "name"));
        }};

        searchTaskID = odooUtility.search_read(listner, database, uid, password, "radees.gerance", conditions, fields);
        searchLocaliteID = odooUtility.search_read(listner, database, uid, password, "radees.localite", conditions, fields);
        // Initialising Lists :
        arrayListIDsGerances = new ArrayList<>();
        arrayListNamesGerances = new ArrayList<>();
        arrayListIDsLocalites = new ArrayList<>();
        arrayListNamesLocalites = new ArrayList<>();
        albumList = new ArrayList<>();
        adapter = new MenuAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();
    }

    /**
     * Getting Gerances Localites
     */

    XMLRPCCallback listner = new XMLRPCCallback() {
        @Override
        public void onResponse(long id, Object result) {
            Looper.prepare();
            if(id == searchTaskID){
                Object[] classObjts = (Object[])result;
                int length = classObjts.length;
                if(length > 0){
                    arrayListIDsGerances.clear();
                    arrayListNamesGerances.clear();
                    for(int i =0;i<length;i++){
                        @SuppressWarnings("unchecked")
                        Map<String,Object> classObj = (Map<String,Object>)classObjts[i];
                        String idGerance,nomGerance;
                        idGerance = classObj.get("id").toString();
                        nomGerance = classObj.get("name").toString();
                        arrayListIDsGerances.add(idGerance);
                        arrayListNamesGerances.add(nomGerance);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setListSharedGerances();
                        }
                    });
                }
            }else if (id == searchLocaliteID){
                Object[] classObjts = (Object[])result;
                int length = classObjts.length;
                if(length > 0){
                    arrayListIDsLocalites.clear();
                    arrayListNamesLocalites.clear();
                    for(int i =0;i<length;i++){
                        @SuppressWarnings("unchecked")
                        Map<String,Object> classObj = (Map<String,Object>)classObjts[i];
                        String idLocalite,nomLocalite;
                        idLocalite = classObj.get("id").toString();
                        nomLocalite = classObj.get("name").toString();
                        arrayListIDsLocalites.add(idLocalite);
                        arrayListNamesLocalites.add(nomLocalite);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setListSharedLocalites();
                        }
                    });
                }

            } else {
                Log.e("Error", "1st Error Partners Not Found");
            }
            Looper.loop();
        }

        @Override
        public void onError(long id, XMLRPCException error) {
            Looper.prepare();
            Log.e("LOGIN", error.getMessage());
            odooUtility.MessageDialog(MenuActivity.this,"LOGIN Error "+error.getMessage());
            Looper.loop();
        }

        @Override
        public void onServerError(long id, XMLRPCServerException error) {
            Looper.prepare();
            Log.e("LOGIN", "Login Server Error"+error.getMessage());
            odooUtility.MessageDialog(MenuActivity.this,"Login Server Error "+error.getMessage());
            Looper.loop();
        }
    };

    public void setListSharedGerances(){
        SharedData.setListKey(MenuActivity.this,"idGerances",arrayListIDsGerances);
        SharedData.setListKey(MenuActivity.this,"nomsGerances",arrayListNamesGerances);
    }

    public void setListSharedLocalites(){
        SharedData.setListKey(MenuActivity.this,"idLocalites",arrayListIDsLocalites);
        SharedData.setListKey(MenuActivity.this,"nomsLocalites",arrayListNamesLocalites);
    }


    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.ic_consommation,
                R.drawable.ic_reclamations,
                R.drawable.ic_demandes_2,
                R.drawable.ic_resiliation_2,
                R.drawable.ic_addcontrat,
                R.drawable.ic_simulation,
        };
        ItemMenu a = new ItemMenu("Consommations", covers[0]);
        albumList.add(a);

        a = new ItemMenu("Réclamations", covers[1]);
        albumList.add(a);

        a = new ItemMenu("Demandes", covers[2]);
        albumList.add(a);

        a = new ItemMenu("Résiliations", covers[3]);
        albumList.add(a);

        a = new ItemMenu("Ajout Contrat", covers[4]);
        albumList.add(a);

        a = new ItemMenu("Simulations", covers[5]);
        albumList.add(a);



        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
