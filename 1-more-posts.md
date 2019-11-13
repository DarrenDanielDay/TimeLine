## 获取更多POST

---

### 功能描述

初次加载返回特定（5条）条数的POST、或者获取某时间点之后的所有的POST（时间倒序，最新发布的POST在最前面）

### 请求地址

```GET https://ecnuer996.cn/timeline/more-posts```

### 请求参数

属性      |   类型 | 说明  
:----------:|:---------:|:---------:
time   | string  | 可选，格式为“yyyy-MM-dd hh:mm:ss”<br>未设置将返回最新的五条POST，<br>设置后将返回指定时间之前的所有POST

### 返回值

**Object**
返回的JSON数据包

属性       | 类型       | 说明  
:----------:|:---------:|:---------: 
posts     | array     | POST列表

数组posts中元素的属性

属性       | 类型       | 说明  
:----------:|:---------:|:---------: 
nickname     | string     | 用户名
avatar | string | 用户头像URL
postId | number | POST的ID
postTime | string | POST发布时间
content | string | POST内容
images | array | POST附加的图片列表

数组images中每个元素为一个字符串，是对应图片的URL

## 刷新操作

---

### 功能描述

点击刷新操作，后台随机生成若干POST并返回至多十条的最新POST

### 请求地址

```GET https://ecnuer996.cn/timeline/refresh```

### 请求参数

属性      |   类型 | 说明  
:----------:|:---------:|:---------:
time   | string  | 前端列表中最新POST的发布时间，格式为“yyyy-MM-dd hh:mm:ss”

### 返回值

**Object**
返回的JSON数据包

属性       | 类型       | 说明  
:----------:|:---------:|:---------: 
posts     | array     | POST列表

数组posts中元素的属性

属性       | 类型       | 说明  
:----------:|:---------:|:---------: 
nickname     | string     | 用户名
avatar | string | 用户头像URL
postId | number | POST的ID
postTime | string | POST发布时间
content | string | POST内容
images | array | POST附加的图片列表

数组images中每个元素为一个字符串，是对应图片的URL