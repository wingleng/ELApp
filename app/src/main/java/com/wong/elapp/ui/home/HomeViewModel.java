package com.wong.elapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wong.elapp.pojo.RandomList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeViewModel extends ViewModel {
    public HomeViewModel() {
        word_ui = new MutableLiveData<>();
        itera_randlist = new MutableLiveData<>();
        wasLogined = new MutableLiveData<>();
        wasLogined.setValue(false);
    }
    private MutableLiveData<Boolean> wasLogined;//一个用来检测是否登录过的变量。

    //设置随机单词，但是感觉没有必要使用livedata进行监视，因为这个数据应该只在请求的时候设置一次。不过为了所谓的生命周期风险，还是写一下吧。
    private MutableLiveData<List<RandomList>> list_word;

    //设置要展示的单词，这个应该需要进行监视了
    private MutableLiveData<RandomList> word_ui;

    //这里需要维护一个迭代器。使用这个迭代器对list进行操作。
    private MutableLiveData<Iterator<RandomList>> itera_randlist;


    //随机单词列表，list_word的设置
    public MutableLiveData<List<RandomList>> getList_word() {
        return list_word;
    }
    public void setList_word(List<RandomList> list_word) {
        if (this.list_word ==null){
            this.list_word = new MutableLiveData<>();
            this.list_word.setValue(new ArrayList<>());
        }
        this.list_word.getValue().addAll(list_word);
        itera_randlist.setValue(this.list_word.getValue().iterator());
    }


    //单个单词，word_ui的设置
    public MutableLiveData<RandomList> getWord_ui() {
        return word_ui;
    }
    public void setWord_ui(RandomList word_ui) {
        this.word_ui.setValue(word_ui);
    }

   //迭代器
    public MutableLiveData<Iterator<RandomList>> getItera_randlist() {
        return itera_randlist;
    }

    //是否登录标志


    public MutableLiveData<Boolean> getWasLogined() {
        return wasLogined;
    }

    public void setWasLogined(MutableLiveData<Boolean> wasLogined) {
        this.wasLogined = wasLogined;
    }
}