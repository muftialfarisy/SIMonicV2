package com.KP.simonicv2.Registrasi;




import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.KP.simonicv2.Individu.Individu;
import com.KP.simonicv2.Individu.IndividuAdapter;
import com.KP.simonicv2.R;

import java.util.ArrayList;

public class RegistrasiAdapter extends RecyclerView.Adapter<RegistrasiAdapter.myviewholder> {
    private ArrayList<Registrasi_gs> registrasilist = new ArrayList<>();
    private Context context;
    public RegistrasiAdapter(ArrayList<Registrasi_gs> registrasilist, Context context) {
        this.registrasilist = registrasilist;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_rs, parent, false);*/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_listview, parent, false);

        return new myviewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final RegistrasiAdapter.myviewholder holder, int position) {
    /*holder.nama.setText(registrasilist.get(position).getNama());
    holder.nik.setText(registrasilist.get(position).getNik());
    holder.alamat.setText(registrasilist.get(position).getAlamat());
        holder.id.setText(registrasilist.get(position).getDevice());
        holder.riwayat.setText(registrasilist.get(position).getRiwayat());
        holder.coordinate.setText(registrasilist.get(position).getCoordinate());
        holder.durasi.setText(registrasilist.get(position).getDurasi());
        holder.selesai.setText(registrasilist.get(position).getSelesai());
        holder.jkp.setText(registrasilist.get(position).getJkp());
        holder.jkw.setText(registrasilist.get(position).getJkw());*/
    }

    @Override
    public int getItemCount() {
        return (registrasilist != null) ? registrasilist.size() : 0;
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        EditText nama,nik,alamat,id,riwayat,coordinate;
        TextView durasi,selesai;
        RadioButton jkp,jkw;
        private static final int REQUEST_CALL = 1;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            /*nama = (EditText) itemView.findViewById(R.id.txt_nama);
            nik = (EditText) itemView.findViewById(R.id.txt_nik);
            alamat =(EditText) itemView.findViewById(R.id.txt_alamat);
            jkp =(RadioButton) itemView.findViewById(R.id.pria);
            jkw =(RadioButton) itemView.findViewById(R.id.wanita);
            id =(EditText) itemView.findViewById(R.id.txt_device);
            durasi =(TextView) itemView.findViewById(R.id.txt_durasi);
            selesai =(TextView) itemView.findViewById(R.id.txt_selesai);
            riwayat =(EditText) itemView.findViewById(R.id.txt_riwayat);
            coordinate =(EditText) itemView.findViewById(R.id.txt_coordinate);*/
        }
    }
}
