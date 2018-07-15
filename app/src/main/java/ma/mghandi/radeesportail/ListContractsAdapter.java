package ma.mghandi.radeesportail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by icomme on 10/10/2016.
 */
public class ListContractsAdapter extends BaseAdapter implements Filterable {

    public static final String ADRESSE_ERROR = "L'Adresse N'existe Pas";
    private ArrayList<Contract> contractsList = null;
    private ArrayList<Contract> filteredList = null;
    private ItemFilter itemFilter;
    private Context context;
    private LayoutInflater layoutInflater;

    public ListContractsAdapter(Context context, ArrayList<Contract> arraylistcontracts) {
        this.contractsList = arraylistcontracts;
        this.filteredList = arraylistcontracts;
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getFilter();
    }

    @Override
    public int getCount() {
        if (filteredList == null) {
            filteredList = new ArrayList<>();
        }
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.contract_list_row, null);
            holder = new Holder();
            holder.ContractNumber = (TextView) v.findViewById(R.id.contractNumber);
            holder.ContractIcon = (ImageView) v.findViewById(R.id.imageContract);
            holder.ContractDispplayMenu = (ImageView) v.findViewById(R.id.displayMenu);
            v.setTag(holder);
        } else {
            holder = (Holder) v.getTag();
        }
        Picasso.with(context).load(R.drawable.ic_demandes).into(holder.ContractIcon);
        holder.ContractNumber.setText(filteredList.get(position).getContract_number());
        //holder.PartnerEmail.setText(filteredList.get(position).getEmail());
        /*holder.ContractDispplayMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filteredList.get(position).getContract_number() == null ) {
                    Toast.makeText(context, "L un des paramètres indisponsable est Null", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(filteredList.get(position).getContract_number()) ) {
                        Toast.makeText(context, "Adresse n'existe pas", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(context, ListConsommationsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        SharedData.setKey(context,"contract_id",filteredList.get(position).getId());
                        SharedData.setKey(context,"contract_number",filteredList.get(position).getContract_number());
                        SharedData.setKey(context,"tourne_number",filteredList.get(position).getTourne_number());
                        context.getApplicationContext().startActivity(intent);
                    }
                }
            }
        });*/
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filteredList.get(position).getContract_number() == null ) {
                    Toast.makeText(context, "L un des paramètres indisponsable est Null", Toast.LENGTH_SHORT).show();
                } else {
                    if (TextUtils.isEmpty(filteredList.get(position).getContract_number()) ) {
                        Toast.makeText(context, "Adresse n'existe pas", Toast.LENGTH_SHORT).show();
                    } else {
                        String comming_from = SharedData.getKey(context, "coming_from");
                        if (comming_from.equals("consommation")){
                            Intent intent = new Intent(context, ListConsommationsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            SharedData.setKey(context,"contract_id",filteredList.get(position).getId());
                            SharedData.setKey(context,"contract_number",filteredList.get(position).getContract_number());
                            SharedData.setKey(context,"tourne_number",filteredList.get(position).getTourne_number());
                            context.getApplicationContext().startActivity(intent);
                        }else if (comming_from.equals("resiliation")){
                            Intent intent = new Intent(context, ListResiliationsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            SharedData.setKey(context,"contract_id",filteredList.get(position).getId());
                            SharedData.setKey(context,"contract_number",filteredList.get(position).getContract_number());
                            SharedData.setKey(context,"tourne_number",filteredList.get(position).getTourne_number());
                            context.getApplicationContext().startActivity(intent);
                        }else if (comming_from.equals("simulation")){
                            Intent intent = new Intent(context, SimulationActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            SharedData.setKey(context,"contract_id",filteredList.get(position).getId());
                            SharedData.setKey(context,"contract_number",filteredList.get(position).getContract_number());
                            SharedData.setKey(context,"tourne_number",filteredList.get(position).getTourne_number());
                            context.getApplicationContext().startActivity(intent);
                        }

                    }
                }
            }
        });
       /*holder.MapMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PartnerGeolocationActivity.class);
                intent.putExtra("partnerlat",filteredList.get(position).getLat());
                intent.putExtra("partnerlng",filteredList.get(position).getLng());
                intent.putExtra("partnerName",filteredList.get(position).getName());
                intent.putExtra("partnerTelephone",filteredList.get(position).getPhone());
                context.startActivity(intent);
            }
        });*/
        return v;
    }

    class Holder {
        TextView ContractNumber, TourneNumber;
        ImageView ContractIcon,ContractDispplayMenu;
    }

    public static void AlerteMsg(Context context, final int position, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public Filter getFilter() {
        if (itemFilter == null) {
            itemFilter = new ItemFilter();
        }

        return itemFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Contract> tempList = new ArrayList<>();
                // search content in friend list
                for (Contract contract : contractsList) {
                    if (contract.getContract_number() != null && contract.getTourne_number() != null) {
                        if (contract.getContract_number().toLowerCase().contains(constraint) || contract.getTourne_number().toLowerCase().contains(constraint)) {
                            tempList.add(contract);
                        }
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = contractsList.size();
                filterResults.values = contractsList;
            }

            return filterResults;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Contract>) results.values;
            notifyDataSetChanged();
        }

    }
}
