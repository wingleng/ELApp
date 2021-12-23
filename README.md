

# 关于这个项目ELAPP

该项目是一个基于java开发的服务器-客户端模式的安卓英语学习软件，主要功能点就是背单词，中英文翻译，OCR文字翻译。

服务器端使用springboot，mybatisplus，MySQL，mongodb，redis等技术，实现用户登录验证，token验证，以及返回客户端数据等功能。

客户端使用java开发安卓，也是首次使用了jackpet的部分组件进行开发，如ViewModel，LiveData，Navigation等组件

---


****

项目结构图：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218010013.svg)



---


## 前后端：

### 前后端交互逻辑：

#### Java端：

Controller 层有三个controller，负责接受安卓端发送来的请求，然后调用Service层的服务或者Dao层的服务（dao层有服务属于设计失误），然后服务处理完业务之后，就会将结果返回到controller中，最后controller发送结果到安卓端。

* 一个简单的示例：

功能一：

​       首先WordController接受到来自安卓客户端的请求，然后调用Dao层的MongodbMapperImpl，到Mongodb数据库查询，最后将结果序列化为json，返回到安卓端中。

功能二：

​        UserControler接受登录请求，然后调用LoginService ，LoginService使用Dao层的UserMapper 到Mysql数据库中查询验证，然后将验证结果返回到Usercontroller中，UserController再将数据返回到安卓客户端

### 1 服务端 Service_For_Android

#### 1.1 主要功能简述

##### 功能一：返回数据

**首先，安卓端需要的数据有两种，来源分别是这个服务端、第三方接口（有道翻译，百度翻译）。这里介绍服务端的数据**

* 一种是用户数据，也就是用户的账号密码，用户记了的单词，用户没有记的单词，用户收藏的单词。这种数据使用MySQL进行保存。
* 第二种是单词数据，也就是该App的词库，记录了每个单词的英文，中文，音频URL，示例图片，示例句子等信息。这种数据使用mongodb进行保存。

---


##### 功能二：验证用户的登录，拦截访问

* 验证用户的登录，这个功能就是获取到客户端发来的账号密码，然后到MySQL数据库中进行查询验证，验证失败则返回失败。验证成功，则使用用户的id为其生成一个token并返回；同时将用户对象序列化为json，存储到redis中，方便随取随用。
* 拦截访问，服务端配置了一个拦截器，用来检查请求头中是否有携带token，拥有token才能访问除登录以外的接口。


---

---


#### 1.2 结构简述

##### 1.2.1 总的项目结构：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211217213207.png)

![](https://gitee.com/wongGNOW/images/raw/master/img/20211217213303.png)

##### 类图：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218205007.svg)



****

##### 顺序图：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211219195515.svg)

****

##### 协作图：

![协作图](https://gitee.com/wongGNOW/images/raw/master/img/20211219195624.svg)

如图所示：大概可以分为，控制controller，服务service，数据提供dao，数据实体类vo ，拦截器LoginInterceptor，工具类utils等多个功能模块。

****

##### 结构图

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218000939.svg)

****

##### 组件图：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211220200724.svg)

##### 1.2.2 数据库：

* **MySQL**

很简单的几个表，一个用户表，用来存储用户信息，其他的表都是用户表的关联表，记录用户背了的单词，用户没有背的单词，用户收藏的单词。



![数据库表](https://gitee.com/wongGNOW/images/raw/master/img/20211218140024.svg)

****

**实体类图**：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218144553.png)

![实体类图：](https://gitee.com/wongGNOW/images/raw/master/img/20211218143503.svg)

****

* **MongoDB**

只用来存储单词数据。只读，不进行修改。所有的数据格式都一样。

**文档展示：**

![json类](https://gitee.com/wongGNOW/images/raw/master/img/20211218144142.png)

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218144419.png)

****

**实体类图：**

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218144829.png)



![](https://gitee.com/wongGNOW/images/raw/master/img/20211218144041.svg)

****

##### 1.2.3 数据层（dao）：

dao层对MySQL，mongodb进行操作，向外提供服务（其实这里算是设计失误，dao层应该只提供数据库的增删查改操作，而不应该把服务也放置在这里。）。

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218145537.png)

****

###### **mongodb**

​		因为mongodb只是存储单词数据的作用，所以mongodb部分的功能就只有随机查询单词而已。仅仅提供几个查询方法

* **interface QueryMapper**

  * **public List<Word> randomWords;**  

    根据参数返回指定数量的随机单词。

  * **public int wordNumber;** 

    返回当前词库中的单词总数。



* **类图如下**：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218152100.svg)





****

###### **MySQL**

​        MySQL负责对用户表User，记录单词表RememberedWord，ForgetWord，CollectionWord，进行操作，每个表对应一个Mapper，所以需要三个Mapper（collection表因为时间不足，没有什么操作）。同时因为使用的是mybatisplus，每个Mapper中已经内置了基本的增删改查方法，因此mapper类图就不画了。这里就只绘制service图：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218153521.png)



* **UserService**

* ```java
  public interface UserService {
      /**
       * 新增用户
       */
      Result AddNewUser(String userName, String userPassword);
  
      /**
       * 查询用户名密码
       * @param username
       * @param password
       * @return
       */
      User findUser(String username, String password);
  
      /**
       * 查询用户名是否有重复
       * @param username
       * @return
       */
      boolean isRepeatedUserName(String username);
  
      /**
       * 通过token获取用户信息
       * @param token
       * @return
       */
      Result getUserInfoByToken(String token);
  }
  ```

  * 类图如下:

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218163429.svg)



* **WordService**

  ```java
  public interface WordService {
  
      /**
       * 对RememberList进行操作
       * @param idlist
       * @return
       */
      Result insertRemember(String token,List<String> idlist);
  
      /**
       * 对ForgetWordList进行操作
       * @param token
       * @param idlist
       * @return
       */
      Result insertForget(String token,List<String> idlist);
  }
  ```

  * 类图如下（不完整，LoginService可以到Service层查看。）：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218160953.svg)

****

##### 1.2.4 业务层（service）：

接受控制层的调用，返回处理好的数据。该层只有一个LoginServiceImpl，提供用户的登录功能。（这里算是设计失误，很多的service都分散到了dao层当中。）

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218173732.svg)

****

##### 1.2.5 控制层（controller）：

* WordsController，负责随机返回mongodb里的单词数据。
* 提供的接口：
  * findwords/randWord/{num}
  * findwords/wordNums

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218181109.svg)

****

* UserController，负责对用户进行验证
* 提供的接口：
  * user/addUser，注册用户接口
  * user/login，登录用户接口
  * user/currentUser，获取当前用户接口

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218184915.svg)

****

* InsertController:，负责对用户单词进行插入操作。

* 提供的接口：
  * userinsert/rember，对RememberWord表进行操作
  * userinsert/forget，对ForgetWord表进行操作。

![InsertController](D:\WONG\Desktop\软件工程课程设计\图\InsertController.svg)

##### 1.2.6 拦截器（LoginIntercepter)：



#### 1.3 数据VO简述：







****











### 2 客户端 ELAPP

这个安卓客户端的功能也不是很多，就只有记单词（就所谓的展示单词，然后有图片和发音），和文本翻译（单词和句子都可以，使用有道翻译的接口），图片翻译（也是使用有道翻译的接口）

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218010013.svg)

****

#### 2.1 主要功能概述：

##### 功能零：登录

就一个登录功能，使用token进行认证，非登录者拦截除登录以外的功能。

![](https://gitee.com/wongGNOW/images/raw/master/img/20211220204945.jpg)

##### 功能一：展示单词，单词和句子都可以点击发音

如图所示：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211220204846.jpg)



##### 功能二：翻译，提供文字和图片翻译

![](https://gitee.com/wongGNOW/images/raw/master/img/20211220205019.jpg)

****



![](https://gitee.com/wongGNOW/images/raw/master/img/20211220205047.jpg)



![](http://pic.wingloong.top/%E7%BF%BB%E8%AF%91%E5%8E%9F%E5%9B%BE.jpg)



##### 功能三：数据图表（没有实现）

按道理说这个功能应该可以简单实现，但是没有太多时间了，所以放弃掉。



#### 2.2 结构简述：

* 界面简述

这个安卓应用使用了单Activity，多fragment的模式（就是应用的三个tab，共享Activity的ViewModel），来设计整个应用。

同时使用Navigation来对每个Fragment进行管理。

![](https://gitee.com/wongGNOW/images/raw/master/img/20211223230210.png)

* 业务

  * 业务一，展示单词
  * 业务二，提供文本翻译和图片翻译

  这些业务一句话概括，其实就是发送请求，然后收到请求，将收到的请求渲染到界面中。所以安卓端的业务就只有请求的发送和接收（所以实际上写界面的时间要比业务的时间要久）。 好了，废话不多说，安卓的业务就是请求的发送和接收，所以花费了很大的精力，去搭建了一个网络模块，方便后面的请求发送和接受（后面的开发也证明，这个网络模块所耗费的时间是完全值得的)。下面的篇幅来详细介绍这个网络模块。

![](https://gitee.com/wongGNOW/images/raw/master/img/20211223231131.png)



##### 2.3 网络模块

网络模块使用了Okhttp和Retrofit，同时还使用了依赖注入框架Hilt，可以在整个安卓应用内随时注入使用，非常的方便。

* 首先编写Hilt的模块类，相当于spring的xml配置，定义好需要实例化的类。

```java
package com.wong.elapp.hilt;

import android.media.MediaPlayer;

import com.wong.elapp.hilt.types.BaiduRetrofit;
import com.wong.elapp.hilt.types.LocalMapper;
import com.wong.elapp.hilt.types.LocalRetrofit;
import com.wong.elapp.hilt.types.YoudaoMapper;
import com.wong.elapp.hilt.types.YoudaoRetrofit;
import com.wong.elapp.network.TokenIncepter;
import com.wong.elapp.network.mapper.BaiduService;
import com.wong.elapp.network.mapper.LocalService;
import com.wong.elapp.network.mapper.YoudaoService;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
@InstallIn(ApplicationComponent.class)
public class NetworkModule {
    /**
     * 此类为网络模块
     */

    /**
     * 配置okhttp,
     * 这个okhttp是为连接本地的java服务器提供的。
     */

    @Singleton
    @Provides
    OkHttpClient provideOkhttpForLocalService(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new TokenIncepter())
                .build();
        return okHttpClient;
    }

    /**
     * 配置第二个okhttp
     * 这个okhttp是位有道翻译准备的。
     */
//    @Singleton
//    @Provides
//    OkHttpClient provideOkhttpForYoudao(){
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
////                .addInterceptor(new TokenIncepter())
//                .build();
//        return okHttpClient;
//    }

    /**
     * 配置Retrofit，
     * 这个retrofit是为本地java服务器提供的
     */
    @LocalRetrofit
    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.wingloong.top/")
//                .baseUrl("http://172.31.129.139:8888/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 配置第二个retrofit,
     * 这个retrofit是为有道翻译准备的
     */
    @YoudaoRetrofit
    @Singleton
    @Provides
    Retrofit provideRetrofitForYoudao(){
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("https://openapi.youdao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit2;
    }

    /**
     * 配置第三个Retrofit，
     * 这个Retrofit是为百度翻译准备的
     */
    @BaiduRetrofit
    @Singleton
    @Provides
    Retrofit provideRetrofitForBaidu(){
        Retrofit retrofit3 = new Retrofit.Builder()
                .baseUrl("https://aip.baidubce.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit3;
    }

    /**
     * 配置Service，
     * 这个是为Java服务器准备的。
     */
    @Singleton
    @Provides
    LocalService provideLocalService(@LocalRetrofit Retrofit retrofit){
       return retrofit.create(LocalService.class);
    }

    /**
     * 配置Service
     * 这个是为有道翻译准备的
     */
    @Singleton
    @Provides
    YoudaoService provideYoudaoService(@YoudaoRetrofit Retrofit retrofit2){
        return retrofit2.create(YoudaoService.class);
    }

    /**
     * 配置Service
     * 这个是为百度翻译准备的。
     * @param retrofit3
     * @return
     */
    @Singleton
    @Provides
    BaiduService provideBaiduService(@BaiduRetrofit Retrofit retrofit3){
        return retrofit3.create(BaiduService.class);
    }


}

```

* 然后配置Retrofit的服务提供接口，这些定义好的接口可以直接使用：

```java
package com.wong.elapp.network.mapper;

import com.wong.elapp.pojo.RandomList;
import com.wong.elapp.pojo.vo.LoginParam;
import com.wong.elapp.pojo.vo.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LocalService {
    /**
     * 这个接口是连接到本地的服务器中的，
     * 本地服务器主要提供mongodb的查询服务
     * MySQL的增删改操作
     *
     */

    @GET("findwords/randWord/10")
    Call<Result<List<RandomList>>> getRandomWords();

    @GET("findwords/test")
    Call<String> getTest();

    /**
     * 配置用户注册接口
     */
    @POST("user/addUser")
    Call<Result> registe(@Body LoginParam loginParam);

    /**
     * 配置用户登录接口
     * @param loginParam
     * @return
     */
    @POST("user/login")
    Call<Result<String>> login(@Body LoginParam loginParam);

    /**
     * 配置插入记录单词接口
     * @param ids
     * @return
     */
    @POST("userinsert/rember")
    Call<Result<String>> insertRember(@Body List<String> ids);

    @POST("userinsert/forget")
    Call<Result<String>> insertForget(@Body List<String> ids);
}

```

* 最后就是注入这些网络服务对象，发送请求。

![](https://gitee.com/wongGNOW/images/raw/master/img/20211224001104.png)

![](https://gitee.com/wongGNOW/images/raw/master/img/20211224001154.png)

