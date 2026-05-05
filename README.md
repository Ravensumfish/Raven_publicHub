# 仿知乎日报客户端
基于 Android实现的知乎日报仿写项目，完成核心页面与基础交互功能。

## 项目介绍
本项目是对**知乎日报**移动端的仿写实现，遵循原生界面设计风格，实现新闻展示、详情查看、评论互动、内容分享等功能，专注于页面结构还原与基础交互体验。

## 已实现功能
-  首页 Banner 轮播图（非同方向侧滑无限循环）
-  新闻列表预览（标题 + 缩略图 + hint）
-  下拉刷新、上拉加载更多
-  新闻详情页（html）
-  评论列表页
-  分享（可分享新闻链接）

## 项目结构
- 首页：Banner + 新闻列表
- 详情页：顶部大图和文章内容展示
- 评论页：短评列表展示
- 工具类：分享功能模块

## 展示
### 首页
使用coordinatorlayout将banner的vp2与rv联动。其中还用到了appbar，页面滚动时使banner折叠，并以rv作为滚动参考

<img width="220" height="458" alt="mmexport1777980735411" src="https://github.com/user-attachments/assets/7ecd932e-50c6-48b4-a59e-d40f963565db" />

### 下拉刷新、上拉加载更多
使用swiperefreshlayout实现，下拉时显示出刷新动画，并调用viewModel的刷新方法完成实际刷新；下拉加载更多也是通过自定义viewModel的方法，将过往的news与现有newsList进行拼接，展示到UI层

<img width="220" height="458" alt="mmexport1777980771897" src="https://github.com/user-attachments/assets/2080c8d2-2142-49cb-a2a2-fdcc7689c670" />

### 详情页左滑切换新闻
还是vp2实现，实际上只能加载三篇，然后就划不动了。实际跳转页面的list数量和提交给adapter的数量不一样，内部逻辑有点问题。

<img width="220" height="458" alt="mmexport1777980804693" src="https://github.com/user-attachments/assets/e2e1fdf7-2793-454a-b24d-aab8019c50c1" />

### 查看短评
<img width="220" height="478" alt="mmexport1777980832265" src="https://github.com/user-attachments/assets/cafdd69e-91fb-43a8-9592-c6aeb0b5cbf4" />

### 分享
隐式跳转，把内容（链接）准备好，调用系统的分享面板

<img width="220" height="463" alt="mmexport1777980858393" src="https://github.com/user-attachments/assets/2a611efc-4de0-44fd-8895-d70162b8f12c" />



## 总结
仅用于学习与仿写练习，数据来源知乎api。
