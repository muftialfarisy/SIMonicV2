package com.KP.simonicv2.Individu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.KP.simonicv2.R;
import com.KP.simonicv2.Registrasi.Registrasi_gs;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class IndividuAdapter extends RecyclerView.Adapter<IndividuAdapter.indViewHolder>  {
    private ArrayList<Individu> dataList = new ArrayList<>();

    private List<String> itemsFiltered;
    private OnindListener mOnindListener;
    private Context context;
    private IndividuAdapter listener;
    public IndividuAdapter(ArrayList<Individu> dataList ,OnindListener OnindListener) {
        this.dataList = dataList;
        this.context = context;
        this.mOnindListener = OnindListener;

    }
    @NonNull
    @Override
    public indViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_rs, parent, false);*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listview, parent, false);

        return new indViewHolder(view,mOnindListener);

    }

    @Override
    public void onBindViewHolder(@NonNull final IndividuAdapter.indViewHolder holder, int position) {
        String provinsi = dataList.get(position).getProvinsi();
        String kota = dataList.get(position).getKota();
        String kecamatan = dataList.get(position).getKecamatan();
        String kelurahan = dataList.get(position).getKelurahan();
        String device = dataList.get(position).getDevice_Id();
        //holder.txtNama.setText(dataList.get(position).getLat()+" ,"+dataList.get(position).getLat_Me()+" ,"+
                //dataList.get(position).getLng()+" ,"+dataList.get(position).getLng_Me()+" ,"+dataList.get(position).getDevice_Id());
        holder.txtNama.setText(dataList.get(position).getNama());
        holder.txtdevice.setText(device);
        holder.txtwilayah.setText(""+kelurahan+","+kecamatan+","+kota+","+provinsi);

    }


    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class indViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout parentLayout;
        OnindListener OnindListener;
        TextView txtNama, txtwilayah,txtdevice;
        public TextView buttonViewOption;
        private static final int REQUEST_CALL = 1;

        public indViewHolder(@NonNull View itemView, OnindListener OnindListener) {
            super(itemView);
            txtNama = (TextView) itemView.findViewById(R.id.txt_nama);
            txtwilayah = (TextView) itemView.findViewById(R.id.txt_wilayah);
            txtdevice = (TextView) itemView.findViewById(R.id.txt_device);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            mOnindListener = OnindListener;
            itemView.setOnClickListener(this);


        }



        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            mOnindListener.onindClick(getAdapterPosition());

        }
    }
    public interface OnindListener{
        void onindClick(int position);

    }
}