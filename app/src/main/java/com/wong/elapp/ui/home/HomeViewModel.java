package com.wong.elapp.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wong.elapp.pojo.RandomList;
import com.wong.elapp.pojo.vo.Youdaoresult.YoudaoResult;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeViewModel extends ViewModel {
    public HomeViewModel() {
        word_ui = new MutableLiveData<>();
        itera_randlist = new MutableLiveData<>();
        wasLogined = new MutableLiveData<>();
        wasLogined.setValue(false);

        YoudaoResult = new MutableLiveData<>();
    }
    private MutableLiveData<Boolean> wasLogined;//一个用来检测是否登录过的变量。

    /**
     @data ReciteFragment的数据，展示单词。
     */
    private MutableLiveData<List<RandomList>> list_word;

    //设置要展示的单词，这个应该需要进行监视了
    private MutableLiveData<RandomList> word_ui;

    //这里需要维护一个迭代器。使用这个迭代器对list进行操作。
    private MutableLiveData<Iterator<RandomList>> itera_randlist;

    /**
    @data dash_firstFragment的界面的数据，保存翻译结果
     */
    private MutableLiveData<YoudaoResult> YoudaoResult;

    /**
     @data dash_firstFragment 控制一些组件是否显示。
     */
    private MutableLiveData<Boolean> isVisable;

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

    /**
     * Getter and Setter Youdaoresult
     * @data dash_firstFragment的界面的数据，保存翻译结果
     * @return
     */
    public MutableLiveData<YoudaoResult> getYoudaoResult() {

        return YoudaoResult;
    }

    public void setYoudaoResult(YoudaoResult youdaoResult) {
        YoudaoResult.setValue(youdaoResult);
    }

    /**
     * Getter and Setter isVisable
     * @data dash_firstFragment 控制一些组件是否显示。
     * @return
     */
    public MutableLiveData<Boolean> getIsVisable() {
        if (isVisable == null){
            MutableLiveData<Boolean> cur = new MutableLiveData<>();
            cur.setValue(false);
            isVisable = cur;
        }
        return isVisable;
    }

    public void setIsVisable(Boolean isVisable) {
        this.isVisable.setValue(isVisable);
    }
}