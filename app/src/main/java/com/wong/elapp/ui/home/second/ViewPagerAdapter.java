package com.wong.elapp.ui.home.second;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView;
import com.wong.elapp.R;
import com.wong.elapp.pojo.RandomList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.MyViewHolder> {
    private List<RandomList> mlist;//数据列表
    Context context;//上下文
    int cur_position;//当前的位置
    String curVoc_word;//当前单词的语音的位置
    boolean has_voc;//当前单词是否有语音。
    String curVoc_img;//当前图片的语音的位置
    boolean has_voc_img;//当前图片是否有语音

    private MediaPlayer mediaPlayer;//音乐播放器
    public ViewPagerAdapter(List<RandomList> list) {
        mlist = list;
    }

    /**
     * 看网上的做法，貌似是在这个onCreateViewHolder方法中，对item内部的控件添加时间监听。。
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singleword,parent,false);
        context = parent.getContext();
        MyViewHolder viewHolder = new MyViewHolder(itemView);



        return viewHolder;
    }

    //这个方法负责设置组件
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    /*
    设置组件
     */
        holder.mtextView.setText(mlist.get(position).getNames());//设置单词
        setGlide(holder,position);//设置图片
        holder.imgsentence.setText(mlist.get(position).getExamples().get(0).getSentence());//设置图片配套句子
        holder.fromText.setText(mlist.get(position).getExamples().get(0).getTitle());//设置图片出处

        /*
        设置卡片下部分的嵌套的RecycleView
         */
        //设置卡片中的RecycleView，为其设置适配器。
        holder.wordlist.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));//设置好布局方向
        List<String> means = mlist.get(position).getMeans()== null ? new ArrayList<>() : new ArrayList<>(mlist.get(position).getMeans());//数据处理，防止为空
        List<String> derives = mlist.get(position).getDerive()==null ? new ArrayList<>() :new ArrayList<>( mlist.get(position).getDerive());

        Log.i("中文释义",""+mlist.get(position).getMeans());
        Log.i("派生",""+mlist.get(position).getDerive());
        holder.wordlist.setAdapter(new RecyAdapt(means,derives));
    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }


    //TODO:这里可能要嵌套一个ViewPager，只显示一张图片真的有点可惜。
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView mtextView;
        QMUIRadiusImageView imageView;
        RecyclerView wordlist;
        TextView imgsentence;
        TextView fromText;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            mtextView = itemView.findViewById(R.id.textView2);//标题
            imageView = itemView.findViewById(R.id.img1);//图片
            wordlist = itemView.findViewById(R.id.wordlist);//recycleview
            imgsentence = itemView.findViewById(R.id.img_sentence);
            fromText = itemView.findViewById(R.id.from_text);



            /*
            设置点击单词播放语音的监听器
             */
            mtextView.setOnClickListener(v -> {
                cur_position = getAdapterPosition();

            if (mlist.get(cur_position).getVoices().size()==0){
                has_voc = false;
                curVoc_word = "";
            }else {
                has_voc = true;
                curVoc_word = mlist.get(cur_position).getVoices().get(0);//默认选择第一个，忘记是美式发音还是英式发音了。
            };
            Log.i("has_voc"+has_voc,"curVoc_word"+ curVoc_word);

//            播放音乐
                try {
                    playMusic(curVoc_word);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            /*
            * 设置点击图片句子播放语音的播放器
             */
            imageView.setOnClickListener(v->{
                int cur = getAdapterPosition();
                //判断是否有语音，其实这个判断完全没必要，因为后端数据设置了过滤器，能够返回的数据，全部都是有图片和音频的。。。。。。
                if (mlist.get(cur).getExamples().size()==0){
                    curVoc_img = "";
                    has_voc_img = false;
                }else{
                    curVoc_img = mlist.get(cur).getExamples().get(0).getAudio();
                    has_voc_img = true;
                }
                //播放音乐
                try {
                    playMusic(curVoc_img);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
    }


    //TODO:以后这里要升级为ViewPager
    //设置图片，暂时的，以后会将图片区域换成ViewPager
    void setGlide(MyViewHolder holder,int position){
        int example_num = mlist.get(position).getExamples().size();
        if (example_num>0){
            Glide.with(context)
                    .load(mlist.get(position).getExamples().get(0).getImg())
                    .into(holder.imageView);
        }
        if (example_num==0){
            Glide.with(context)
                    .load(R.drawable.loading)
                    .into(holder.imageView);
        }
    }

    //TODO:编写一个播放当前位置音乐的方法，需要能够比较准时的切换音乐
    void playMusic(String source) throws IOException {
        if (source !=""){
            if (mediaPlayer==null){
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(source);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }else if(mediaPlayer.isPlaying()){
                mediaPlayer.reset();
                mediaPlayer.setDataSource(source);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }else{
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(source);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
        }else{
            Toast.makeText(context,"当前单词没有语音",Toast.LENGTH_LONG).show();
        }

    }

}
