package com.qhh.opencvdemo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qhh.opencvdemo.R;

import java.util.List;

/**
 * @author qinhaihang_vendor
 * @version $Rev$
 * @time 2019/4/24 17:57
 * @des
 * @packgename com.qhh.opencvdemo.adapter
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes
 */
public class MainAdatper extends RecyclerView.Adapter<MainAdatper.VH> {

    private List<String> datas;
    private OnItemClickListener listener;

    public MainAdatper(List<String> datas,OnItemClickListener listener) {
        this.datas = datas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_main, viewGroup, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, final int position) {
        String item = datas.get(position);
        vh.tv_item.setText(item);
        vh.tv_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setDatas(List<String> dataList) {
        this.datas = dataList;
        notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView tv_item;

        public VH(@NonNull View itemView) {
            super(itemView);
            tv_item = itemView.findViewById(R.id.tv_item);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

}
