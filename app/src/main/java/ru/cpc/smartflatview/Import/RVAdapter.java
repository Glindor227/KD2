package ru.cpc.smartflatview.Import;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.cpc.smartflatview.Import.FileRepository.ServerFile;
import ru.cpc.smartflatview.R;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolder> {
//    private TextView fileResult;
    private List<String> data;
    private ServerFile serverFile;

    RVAdapter(ServerFile serverFile,List<String> data) {
        Log.d(ImportActivity.TAG, "RVAdapter");

        this.data = data;
        this.serverFile = serverFile;
    }

    @NonNull
    @Override
    public RVViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.import_file_note, viewGroup,
                false);
        return new RVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolder rvViewHolder, int i) {
        rvViewHolder.nameFile.setText(data.get(i));
        rvViewHolder.dateFile.setText("-");
        rvViewHolder.nameFile.setOnClickListener(v -> {
            TextView clTV =(TextView) v;
            String selectedFile =clTV.getText().toString();
            serverFile.initFile(selectedFile);
            Log.d("Import",clTV.getText().toString());
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class RVViewHolder extends RecyclerView.ViewHolder {
        TextView nameFile;
        TextView dateFile;

        RVViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
        }

        private void initViews(@NonNull View itemView) {
            nameFile = itemView.findViewById(R.id.if_name);
            dateFile = itemView.findViewById(R.id.if_date);
        }
    }
}
