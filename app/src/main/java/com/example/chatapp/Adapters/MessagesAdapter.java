package com.example.chatapp.Adapters;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Models.Messages;
import com.example.chatapp.R;
import com.example.chatapp.databinding.ItemReciveBinding;
import com.example.chatapp.databinding.ItemsentBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

//we have to extend two view holder so we did not choose any one
public class MessagesAdapter extends RecyclerView.Adapter{

  Context context;
  ArrayList<Messages>messages;
//if 1 then sent typ or if 2 then Recive type
  final int ITEM_SENT=1;
  final  int ITEM_RECIVE=2;

    public MessagesAdapter(Context context, ArrayList<Messages> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType== ITEM_SENT){

            View view= LayoutInflater.from(context).inflate(R.layout.itemsent,parent,false);
            return new SentViewHolder(view);

        }else {
            View view2= LayoutInflater.from(context).inflate(R.layout.item_recive,parent,false);
            return new ReciveViewHolder(view2);

        }

    }

    @Override
    public int getItemViewType(int position) {

        Messages ms= messages.get(position);
         //if loggedin id=senderId then it theb msg would be a sent message
        if (FirebaseAuth.getInstance().getUid().equals(ms.getSenderId())){

            return ITEM_SENT;
        }else {
         //if not then it comes from else so it will become recived message
          return ITEM_RECIVE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages message = messages.get(position);
             if (holder.getClass()==SentViewHolder.class){

                 SentViewHolder viewHolder= (SentViewHolder) holder;
                 viewHolder.binding.messag.setText(message.getMessage());
             }else {
                 ReciveViewHolder viewHolder= (ReciveViewHolder) holder;
                 viewHolder.binding.message.setText(message.getMessage());

             }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class SentViewHolder extends RecyclerView.ViewHolder{

     ItemsentBinding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);

            binding=ItemsentBinding.bind(itemView);
        }
    }

    public class ReciveViewHolder extends RecyclerView.ViewHolder{

       ItemReciveBinding  binding;
        public ReciveViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ItemReciveBinding.bind(itemView);
        }
    }
}
