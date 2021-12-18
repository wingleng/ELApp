# 关于这个项目ELAPP

该项目是一个基于java开发的服务器-客户端模式的安卓英语学习软件，主要功能点就是背单词，中英文翻译，OCR文字翻译。

服务器端使用springboot，mybatisplus，MySQL，mongodb，redis等技术，实现用户登录验证，token验证，以及返回客户端数据等功能。

客户端使用java开发安卓，也是首次使用了jackpet的部分组件进行开发，如ViewModel，LiveData，Navigation等组件

****

项目结构图：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218010013.svg)





## 前后端：

放一张图：

### 1 服务端 Service_For_Android

#### 1.1 主要功能简述

##### 功能一：返回数据

**首先，安卓端需要的数据有两种，来源分别是这个服务端、第三方接口（有道翻译，百度翻译）。这里介绍服务端的数据**

* 一种是用户数据，也就是用户的账号密码，用户记了的单词，用户没有记的单词，用户收藏的单词。这种数据使用MySQL进行保存。
* 第二种是单词数据，也就是该App的词库，记录了每个单词的英文，中文，音频URL，示例图片，示例句子等信息。这种数据使用mongodb进行保存。

##### 功能二：验证用户的登录，拦截访问

* 验证用户的登录，这个功能就是获取到客户端发来的账号密码，然后到MySQL数据库中进行查询验证，验证失败则返回失败。验证成功，则使用用户的id为其生成一个token并返回；同时将用户对象序列化为json，存储到redis中，方便随取随用。
* 拦截访问，服务端配置了一个拦截器，用来检查请求头中是否有携带token，拥有token才能访问除登录以外的接口。

#### 1.2 结构简述

##### 1.2.1 总的项目结构：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211217213207.png)

![](https://gitee.com/wongGNOW/images/raw/master/img/20211217213303.png)

##### 类图：

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218205007.svg)



如图所示：大概可以分为，控制controller，服务service，数据提供dao，数据实体类vo ，拦截器LoginInterceptor，工具类utils等多个功能模块。

****

##### 结构图

![](https://gitee.com/wongGNOW/images/raw/master/img/20211218000939.svg)

****

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





### 2 客户端 ELAPP







