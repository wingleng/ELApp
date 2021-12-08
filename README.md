#### 功能点

* 背单词
  * 这个功能点有点难搞，词库应该是没有什么办法了，现在手头上的数据就只有人人词典上的那些单词，而且存储方式也是一个问题，所有数据都是以json方式存储的，使用什么数据库？mongodb？MySQL？我是希望使用mongodb的，因为如果使用MySQL的话，这种数据肯定要涉及到大量的多表查询，这样做太麻烦了。但是mongodb又不会使用。。
  * 然后还要记录用户背单词，背了的单词以后不能再出现？还是说降低出现频率？然后还得记录用户每天背的单词数量，绘制成表格展示。
  * 然后估计可能还得设计一个收藏功能。。。
  * 然后就是单词展示的问题了，人人词典上面爬下来的数据，有单词中英文，还有很多例句，以及图片，音频，这个怎么处理是一个问题。
* 文字翻译
  * 这个感觉就随意了，我记得好像天行数据那里有免费1000次的调用，百度和有道那里也好像有免费的调用API

* OCR文字识别
  * 这个感觉也是随意吧，百度也是提供了免费的API调用，直接调用就好了。
* 后台管理？
  * 这个就算了吧，还嫌头发多是吧。

#### 关于界面设计的问题？

* Android界面设计是真的麻烦，使用第三方框架吧，又什么解释都没有。。。。


### 可以使用的资源

* 百度智能云机器翻译接口（包含文字普通翻译和图片翻译）

![](https://gitee.com/wongGNOW/images/raw/master/img/20211124235134.png)

* 百度语音合成接口（只有语音合成）
* 百度文字识别接口（通用文字识别)
* 有道翻译API，这个比百度的要好，返回的翻译有多个意思，并且有语音！
* 讯飞的API，翻译貌似还是和百度差不多，没有有道好。
* 上面的都是网络api，所以要使用json。。百度的翻译没有SDK提供，有道的Android应用创建看起来超级麻烦





# 正式开始搭建

## 首先是背单词

### 数据

​	数据从服务器上面获取，这里采用了SpringBoot，使用MongodbTemplate进行操作

记录一下进行随机读取的方法：

https://blog.csdn.net/weixin_45614626/article/details/119907683 -在springboot中实现随机查询

https://www.cnblogs.com/wslook/p/9831842.html -MongodbTemplate的聚合操作

```java
protected List<T> getRandom(Long size, Class<T> cls){
        Aggregation aggregation =
                Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("status").is(1)),
                        Aggregation.sample(size));
        AggregationResults<T> outputTypeCount =
                mongoTemplate.aggregate(aggregation, cls, cls);
        return outputTypeCount.getMappedResults();
    }
————————————————
版权声明：本文为CSDN博主「weixin_45614626」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/weixin_45614626/article/details/119907683
```

* 用户背了单词之后，数据会存进用户数据库中（关于用户的数据库使用MySQL实现），记录背了的单词的id，然后下一次背单词，也就是下一次进行随机查询时，这些已经背了的单词会一起发送过来（但是感觉还是觉得，用户背了的单词的id，还是在服务端进行操作比较好，如果用户背了几千个单词，那么岂不是要传输几千个json对象？）。

* 服务端得到了用户已经背了的单词的id，下一次随机查询的时候，就会排除这些id，防止重复。

### 界面

* 就使用普通的底部导航方式。展示三个Fragment，分别是背单词界面，展示学习数据界面，还有一个OCR文字识别界面。“我的”界面就算了，反正也没什么数据好设置的。
* 底部使用BottomNavigationView，然后使用Navigation对Fragment进行控制。
* 第一个界面，主要就是用来顺序展示那20个随机英语单词的，第一种想法是直接使用scrollView，直接弄个长条，简单粗暴。第二种是弄个ViewPager，可以左右滑动，就两个界面，左边的展示单词，右边的展示示例句子和图片。
* 其他界面就随便了

### 关于Android端的数据传输问题

##### 需要解决的网络传输问题：

* 设想是使用Retrofit来进行传输的。
* 但是要不要使用一下Dagger和Hilt学习一下？
* 然后就是界面之间数据传递的问题了。。viewmodel还不熟练，不知道能不能在多个fragment之间传递数据。。
* 还有网络图片加载和网络音频播放的问题。
* 还有第三方的API调用问题。。。

##### 服务器所需要提供的接口：

* 1. 点击开始背单词，然后向服务器请求指定个数量的随机单词（未完整实现，完整版应该是Android端发送当前用户的信息，然后客户端拿着用户的信息到MYSQl中进行查询，查看当前用户到底背了哪些单词，然后在随机选单词的时候排除这些已经背了的单词，再返回）

  * findwords/randWord/{num}

* 查询当前词库单词数量，以及用户已经背了的单词数量（用来在首页展示，用户已经背了多少单词百分比）：

  * 暂未实现

* 当Android端的用户背完了单词之后，应该会有两组分类的单词，一组是认识的单词，另外一组是不认识的单词。然后Android客户端将这些单词的id发送到服务器端，服务器端拿到了这些id之后，将这些id分别存到MysqL中，进行存储。方便以后实现其他功能的时候使用。

* 返回用户背单词日期的接口，用来展示用户的背单词天数，或者用来画日历。

* 第三方的接口，需要到第三方服务平台进行申请。这些就自求多福了。

* 需要一个单词翻译的接口，这个使用有道翻译的接口，因为有道翻译的接口返回值是有语音的。其他百度接口是没有的，而且也没有太详细。

* 需要一个OCR图片识别的接口，这个看情况，有道翻译和百度智能云的接口暂时不了解。





### 网络模块的搭建

网络模块是整个应用中非常重要的一环，这里是想使用Retrofit加Hilt注解的方式，创建一个全局的网络模块，方便各Fragment使用。。

但是不会写啊。。。。教程里多了一个RxJava，这个东西我哪里会啊

#### 大概的思想：

* 就是使用Retrofit，Okhttp，Hilt，配合搭建一个全局的网络模块。
* 然后，服务器返回的数据需要进行一下统一，因为需要服务器返回的数据有点多，不可能定义一堆json实体类。
* 不过有点麻烦的就是，这个gson怎么处理。。。



#### 搭建详细步骤：

##### 1. 配置Hilt：

在module的gradle中配置插件：

```groovy
id 'dagger.hilt.android.plugin'
```



![](https://gitee.com/wongGNOW/images/raw/master/img/20211130194250.png)

然后再Project的gradle中配置classpath：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211130200338.png)

最后再添加依赖：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211130200357.png)

##### 2. 编写代码：

* 使用Hilt，先要设置当前Application

  * 继承Application

  ![](https://gitee.com/wongGNOW/images/raw/master/img/20211130204534.png)

  * 在AndroidManifest.xml中配置

![](https://gitee.com/wongGNOW/images/raw/master/img/20211130204625.png)

* 定义模块，配置好想要注入的类：（这里定义的范围是整个应用可用）

```java
package com.wong.test_retrofit.hilt;

import com.wong.test_retrofit.retrofit.Apiservice;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(ApplicationComponent.class)
public class NetWorkModule {
    /**
     * 该模块为网络模块
     */

    /**
     * 这个okhttp可有可无，但是如果某些请求需要在头中添加一些key，加个什么拦截器之类的，建议还是再创建一个新的okhttp
     */
    @Singleton
    @Provides
    OkHttpClient provideOkhttp(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }


    /**
     * Retrofit ，也是和okhttp一样，有可能需要两个不一样的，一个负责本地服务器，一个负责第三方服务器
     */
    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.31.131.110:8888/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }


    /**
     * 创建ApiService，这个定义请求的网址
     */
    @Singleton
    @Provides
    Apiservice provideApiservice(Retrofit retrofit){
        return retrofit.create(Apiservice.class);
    }


}
```

* 最后，在要注入的组件上面配置

![](https://gitee.com/wongGNOW/images/raw/master/img/20211130204904.png)

* 测试注入的apiService能否正常使用：

```java
 Button btn = findViewById(R.id.button);
        btn.setOnClickListener((View v)->{
//            request();
           Call<List<RandomList>> call=  apiservice.getCall();
           call.enqueue(new Callback<List<RandomList>>() {
               @Override
               public void onResponse(Call<List<RandomList>> call, Response<List<RandomList>> response) {
                   Log.i("芜湖！" ,"请求成功！");
                   String result = response.body().toString();
                   System.out.println(result);
               }

               @Override
               public void onFailure(Call<List<RandomList>> call, Throwable t) {
                    Log.i("芜湖！","请求失败啊啊啊啊啊啊");
               }
           });
        });
```

* 以上内容没有包括Apiservice接口，以及pojo类

### 关于用户数据存储的问题

* 用户背完了单词，肯定得有个地方存着他背了哪些单词，以后随机查询的时候，不再查这些数据

* 并且还得有个收藏的功能，能够将单词数据收藏起来。。。。

* 这个数据存储，感觉服务器端和用户端都必须有。。。这个就麻烦了

  ****

  * 用户表
    * 用户ID
    * 用户名
    * 密码
  * 用户已经记了的单词：
    * 用户ID
    * 已经背了的单词ID
  * 用户不认识的单词
    * 用户ID
    * 不认识的单词ID





### Fragment

home对应的是背单词，即第一个

dashborad对应的是翻译，即第二个

notification对应的是记录，即第三个。



### ViewPager的使用

这个很难说的清啊

### Room的基本使用

这个暂时感觉没有使用必要，安卓端貌似没有什么需要序列化的对象。

### 服务器端 用户注册和登录功能的实现

#### 注册

* 没什么好说的，就是使用mybatisplus进行插入操作

#### 登录

* 这个有点麻烦，因为需要维持状态，所以需要一个token
  * 步骤：
    * 判空，判断用户名和密码是否为空
    * 到数据库中进行查找
    * 将查找到的User对象，json序列化到redis中，维持状态，或者说拦截的时候用得到。
    * 根据User对象的Id生成token
    * 返回token

### 关于app的签名：

开发中，Androidstudio提供的默认签名：对了，这个签名必须使用jdk8才能获取到md5，jdk版本8以后都不支持md5了。

![(https://gitee.com/wongGNOW/images/raw/master/img/20211208183316.png)

```shell
C:\Users\WONG\.android>keytool -v -list -keystore debug.keystore
输入密钥库口令:
密钥库类型: PKCS12
密钥库提供方: SUN

您的密钥库包含 1 个条目

别名: androiddebugkey
创建日期: 2021-11-14
条目类型: PrivateKeyEntry
证书链长度: 1
证书[1]:
所有者: C=US, O=Android, CN=Android Debug
发布者: C=US, O=Android, CN=Android Debug
序列号: 1
有效期为 Sun Nov 14 18:30:35 CST 2021 至 Tue Nov 07 18:30:35 CST 2051
证书指纹:
         MD5:  9F:BD:E1:73:1E:10:06:BA:80:99:6B:51:1F:B8:29:AD:14:72:25:A7
         SHA1: 1A:20:84:D8:98:C6:8F:4C:E4:F3:DB:E9:25:79:E0:46:0C:39:F5:2D:81:2E:53:73:96:9D:62:EC:9D:6C:49:EB
         SHA256: SHA1withRSA (弱)
签名算法名称: 2048 位 RSA 密钥
主体公共密钥算法: 1
版本: {10}


*******************************************
*******************************************
```

