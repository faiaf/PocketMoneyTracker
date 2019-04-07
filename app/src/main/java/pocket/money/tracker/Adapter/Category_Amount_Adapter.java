package pocket.money.tracker.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import pocket.money.tracker.R;

public class Category_Amount_Adapter extends RecyclerView.Adapter {
    Context mContext;
    ArrayList<String>Category=new ArrayList<>();

    public static ArrayList<String> Amount=new ArrayList<>();


    public Category_Amount_Adapter(Context context, ArrayList<String> Cat)
    {
        mContext=context;
        Category=Cat;
        Amount.clear();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.set_amount_category,viewGroup,false);

        return new Category_Amount_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        setUpData((ViewHolder) viewHolder,i);
    }
    private void setUpData(final ViewHolder holder, final int position) {
        holder.name.setText(Category.get(position));
        Amount.add("0");


        holder.amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    Amount.set(position,s.toString());
                else if(s.length() == 0)
                {
                    Amount.set(position,"0");
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return Category.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public EditText amount;

        public ViewHolder(View itemView)
        {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            amount=itemView.findViewById(R.id.amount);
        }
    }

}
