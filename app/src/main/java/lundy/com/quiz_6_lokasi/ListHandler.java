package lundy.com.quiz_6_lokasi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListHandler extends RecyclerView.Adapter<ListHandler.CategoryViewHolder>{
        //private Context context;
        private ArrayList<Lokasi> listLokasi;
        //fungsi ini akan dipanggil nnti dalam void showrecylcle di main activity

               @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int
        viewType) {
            View itemRow =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.lokasi_row, parent, false);
            return new CategoryViewHolder(itemRow);
        }


        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {
            Lokasi p = listLokasi.get(position);
            final int number = position+1;
            //Untuk membuat kustomisasi Nama dengan penanda nomor. Fungsi holder tidak mendukung concate dua string secara langsung
            String urut = String.valueOf(number)+". Latitude : "+p.getLatitude();
           holder.tvLat.setText(urut);
           holder.tvLong.setText("Longitude : "+p.getLongitude());
        }
        @Override
        public int getItemCount() {
            return listLokasi.size();
        }


        public ListHandler (ArrayList<Lokasi> listLokasi) {
            this.listLokasi = listLokasi;
        }
        class CategoryViewHolder extends RecyclerView.ViewHolder{
            TextView tvLong;
            TextView tvLat;
            CardView Area;
            CategoryViewHolder(View itemView) {
                super(itemView);
                //koneksi view dan atribut control
                tvLong = itemView.findViewById(R.id.tvLat);
                tvLat = itemView.findViewById(R.id.tvLong);
                Area =  itemView.findViewById(R.id.area);

            }
        }

}

