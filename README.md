# 介绍
## 功能
  简约的记事app。有笔记的增删查改，笔记的分组，笔记的拖动排序，左滑删除；主界面接入了deepseek，在记事之余可以对话聊天；设置界面可以更改头像和个性签名（目前没什么用）。
## GIF展示
### 增|改
![mmexport1771856729572](https://github.com/user-attachments/assets/ef514830-5773-4697-a272-ad127aca413f)
### 删
![mmexport1771856803425](https://github.com/user-attachments/assets/412ce208-0355-49b9-8bc3-bafd30b0b545)
### 查
![mmexport1771856909112](https://github.com/user-attachments/assets/6f810607-c186-47e5-8b2f-dfc300fda2c9)
### 排序
![mmexport1771859348863](https://github.com/user-attachments/assets/47f80d9e-a071-41b0-b315-7d80882d2c34)
### 组
  增与删
![mmexport1771857028301](https://github.com/user-attachments/assets/8c1069f8-1a0e-4838-9bc7-343f6b88aca4)
![mmexport1771857141688](https://github.com/user-attachments/assets/5ea531a8-11fa-41a7-b1df-7dd0366557c6)
![mmexport1771857275154](https://github.com/user-attachments/assets/1d724c97-3fcd-4353-ae0a-7d565cc38983)
![mmexport1771857353192](https://github.com/user-attachments/assets/8c4a6bd5-5459-4ce7-a10a-c9b2bb46c8b9)
![mmexport1771857433909](https://github.com/user-attachments/assets/4c0f37f2-214f-47a9-aeb6-defec9f8557e)
### AI对话
![mmexport1771857525060](https://github.com/user-attachments/assets/3ba2a2ec-41ac-4b3b-b81b-9f02848614c2)
## 功能实现思路
### 增删查改
  主要使用 SQLiteOpenHelper 实现，在 NoteDataBase 中建表，表中列名是笔记的各个属性，也对应着笔记实体类。利用 SQLiteOpenHelper 中的方法，写增删查改功能，统一管理所有笔记。每条笔记都有独一无二的id，凭借id就可以对相应笔记做出相应操作。
```
private static final String createNotes = "create table " + NOTE_TABLE_NAME +
        "(id INTEGER PRIMARY KEY AUTOINCREMENT," + //0
        "title," +                                 //1
        "content," +                               //2
        "create_time," +                           //3
        "update_time," +                           //4
        "word_count," +                            //5
        "user_id INTEGER NOT NULL," +              //6
        "group_id INTEGER)";                       //7
```
  笔记分组的思路和笔记一样，每个组也有独属于自己的id，当新建笔记或移动笔记时就可以给对应笔记加上组id，表示这条笔记属于该组。
### item交互
  笔记列表的拖动排序和单条笔记的左滑删除都是用 ItemTouchHelper.Callback 实现的，这个类主要是监听作用，当监听到了，就接口回调，让adapter实现相关的方法。
  ```
public interface ItemMoveListener {
    boolean onItemMove(int fromPos,int toPos); //拖动排序时实现
    void onItemRemove(int pos);                //左滑删除时实现
    void itemMoveFinished();                   //拖动的动作结束后实现
}
```
  itemMoveFinished是为了防止频繁拖拽时频繁使用 SharedPreference ，因为我的排序功能实现是使用 sp 读写。将拖动后的笔记列表的顺序（笔记id顺序）用字符串存储下来，下次 rv 刷新列表时就从 sp 中读取，实现顺序的保存。所以 onItemMove 方法只实现了即时的笔记条目交换，itemMoveFinished 方法将交换后的顺序存了下来。
  onItemRemove 方法也是由NoteAdapter（继承自 RecyclerView.Adapter，笔记列表界面的适配器）实现，当 callback 监听到，回调 NoteAdaper 进行删除笔记的流程，方法内部就是弹出一个 Dialog ，点击确认后才会真正把笔记从database中删除。
### AI对话
  用apikey和URL接入deepseek，拉取网络请求得到回复，使用 OkHttpClient 实现。把设定好的对话格式转化为json（用Gson类），然后以此为基础构建请求体，再使用 client 发送请求，在发送请求的 activity 中实现callback（分别是获取回应成功的方法和失败的方法），这样就能得到 ai 的回复了，最后把回复的内容设置到相应控件上，整个 ai 对话流程就结束了。
  用handler做控件休眠，当人物被点击时切换表情，三秒后恢复，默认状态才可被点击。
# 心得体会
虽然功能很少，但耗时却不短，主要是不熟练，磕磕碰碰，搜教程问ai，边理解边敲，中途还会卡壳。总的来说学到了很多，对一些概念和方法的使用更熟练了。一开始搜了很多教程看，但实际开始写的时候仍然迷茫，费了时间也没学到什么，于是我后来就先开始写，不懂再看，b站上安卓教程似乎不多，于是去网上找别人写的教学，也能学到很多，效率也提高了。写的时候看了看之前课程的教程文档，从刚开始接触时的不理解和懵，到现在已经可以完全看懂并运用了，虽然最后的app没有达到预期，但还是很欣喜。
# 待优化
  - 代码的复用性不足，没有很好做到解耦，导致简单的实现使用了复杂的代码。
  - 功能单一，比很多同类型app逊色不少。
  - 缺少排序，缺少文本编辑工具，缺少删除撤销（回收站）。
  - UI不够美观，设计感不足，没有夜间模式。
