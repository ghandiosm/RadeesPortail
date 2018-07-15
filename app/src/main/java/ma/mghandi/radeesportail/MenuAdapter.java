package ma.mghandi.radeesportail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by mghandi on 20/10/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private Context mContext;
    private List<ItemMenu> itemMenusList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public MenuAdapter(Context mContext, List<ItemMenu> itemMenusList) {
        this.mContext = mContext;
        this.itemMenusList = itemMenusList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ItemMenu itemMenu = itemMenusList.get(position);
        holder.title.setText(itemMenu.getName());

        // loading album cover using Glide library
        Glide.with(mContext).load(itemMenu.getThumbnail()).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemMenu.getName().equals("Demandes")){
                    Intent intent = new Intent(mContext,ListDemandesActivity.class);
                    mContext.startActivity(intent);
                }else if(itemMenu.getName().equals("Résiliations")){
                    Intent intent = new Intent(mContext,ListContractsActivity.class);
                    SharedData.setKey(mContext,"coming_from","resiliation");
                    mContext.startActivity(intent);
                }else if(itemMenu.getName().equals("Réclamations")){
                    Intent intent = new Intent(mContext,ListReclamationsActivity.class);
                    mContext.startActivity(intent);
                }else if(itemMenu.getName().equals("Consommations")){
                    Intent intent = new Intent(mContext,ListContractsActivity.class);
                    SharedData.setKey(mContext,"coming_from","consommation");
                    mContext.startActivity(intent);
                }else if(itemMenu.getName().equals("Ajout Contrat")){
                    Intent intent = new Intent(mContext,AddContractActivity.class);
                    mContext.startActivity(intent);
                }else if(itemMenu.getName().equals("Simulations")){
                    Intent intent = new Intent(mContext,ListContractsActivity.class);
                    SharedData.setKey(mContext,"coming_from","simulation");
                    mContext.startActivity(intent);
                }
            }
        });
        /*
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
        */
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    /*
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }
    */

    /**
     * Click listener for popup menu items
     */
    /*
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }
    */
    @Override
    public int getItemCount() {
        return itemMenusList.size();
    }
}
