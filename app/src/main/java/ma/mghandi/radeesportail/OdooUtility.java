package ma.mghandi.radeesportail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.timroes.axmlrpc.XMLRPCCallback;
import de.timroes.axmlrpc.XMLRPCClient;

/**
 * Created by jamal on 18/05/2017.
 */

public class OdooUtility {
    private URL url;
    private XMLRPCClient xmlrpcClient;

    public OdooUtility(String serverAdress, String path) {
        try {
            url = new URL(serverAdress + "/xmlrpc/2/" + path);
            xmlrpcClient = new XMLRPCClient(url);
        } catch (Exception ex) {
            Log.e("ODOO Utility:", ex.getMessage());
        }
    }

    public long login(XMLRPCCallback listner, String db, String username, String password) {
        HashMap<String, Object> emptyMap = new HashMap<String, Object>();
        long id = xmlrpcClient.callAsync(listner, "authenticate", db, username, password, emptyMap);
        return id;
    }

    public long search_read(XMLRPCCallback listner, String db, String uid, String password, String object, List conditions, Map<String, List> fields) {
        long id = xmlrpcClient.callAsync(listner,"execute_kw",db,Integer.parseInt(uid),password,object,"search_read",conditions,fields);
        return id;
    }

    public long create(XMLRPCCallback listner, String db, String uid, String password, String object, List data) {
        long id = xmlrpcClient.callAsync(listner,"execute_kw",db,Integer.parseInt(uid),password,object,"create",data);
        return id;
    }

    public long exec(XMLRPCCallback listner, String db, String uid, String password, String object, String method, List data) {
        long id = xmlrpcClient.callAsync(listner,"execute_kw",db,Integer.parseInt(uid),password,object,method,data);
        return id;
    }

    public long update(XMLRPCCallback listner, String db, String uid, String password, String object, List data) {
        long id = xmlrpcClient.callAsync(listner,"execute_kw",db,Integer.parseInt(uid),password,object,"write",data);
        return id;
    }

    public void MessageDialog(Context ctx,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(msg).setCancelable(false).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

}
