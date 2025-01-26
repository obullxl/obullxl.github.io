+++
slug = "2025012501"
date = "2025-01-25"
lastmod = "2025-01-25"
title = "新春“码”启 | Cocos 研发微信小游戏，总体方案与场景构建（第3天）"
description = "新春开发微信小游戏的第3天，继续使用Cocos进行研发。先是讲述总体设计方案，虽创意未明但有初步思路，包括关卡模式、时间限制、复活机制等。接着详细展示基础框架研发成果，如开始场景和游戏场景（关卡一）的开发过程，包括创建场景、画布、立方体、材质，以及按钮的功能实现和场景切换等，最后总结虽创意未确定但已完成整体流程搭建的部分工作……"
image = "11.jpg"
tags = [ "AI", "Cocos", "小游戏" ]
categories = [ "人工智能" ]
+++

今天是实施新春小游戏计划的第3天，前面2天主要是入门和了解，我们对微信小游戏、Cocos编辑器有了初步认识：

+ 第1天：[新春“码”启 | 0 基础开发微信小游戏，Cocos 游戏引擎 + AI 辅助编程（第1天）](https://mp.weixin.qq.com/s/TlgNKvGYMuGMmU0dIBPn4A)
+ 第2天：

接下来，我们开始实战研发小游戏了：

+ 设计小游戏的总体方案，包括场景、场景内容等
+ 开发小游戏基础框架，跑通整个游戏链路，细节部分后续逐步完善补充

# 总体设计方案

截止目前，老牛同学还是没有确定小游戏的创意，大模型在这个点像像失灵了一样，一直无法达到我的要求。

虽然创意无法明确，但是老牛同学尝试玩了几款受欢迎的微信小游戏，然后结合自己的经验，设计这款小游戏的方案如下：

![总体设计方案](11.jpg)

老牛同学的设计思路有以下几点：

+ 游戏以通关模式体现，至少有1个关卡，关卡由易到难
+ 明确每个关卡的通关时间，时间结束或者工具耗尽（如：子弹、生命等），通关失败
+ 若通关成功，则继续下一个关卡；若通过失败，还可以选择复活，复活需要玩家参与互动（如：看广告、关注等操作）

由于这个游戏禁止杀戮、战争等场景，因此通关就不能是子弹这些！

# 研发基础框架

接下来，开始用Cocos研发基础框架，串联整个游戏流程，对于不明确的部分，暂时跳过。比如，我们通过点击“成功”和“失败”这2个按钮，分别代表通过成功和通关失败。

今天核心目标完成前面3个场景：

## 场景一：开始场景

开始场景表达的核心内容：角色设定、背景介绍、任务说明，已经进入游戏或者退出游戏的操作按钮。

在“资源管理器”面板中，在`assets`中右键新建目录`scenes`，用户存放小游戏场景内容，我们新建一个名为`SceneStart`的场景并双击打开它。

由于小游戏主要是在手机中运行，因此我们设置一下小游戏的宽度和高度：

+ 打开项目设置面板，路径：“项目” - “项目设置”
+ 在“项目数据”配置项中，调整参数：“设计宽度”为`720`（像素），“设计高度”为`1280`（像素），同时勾选“适配屏幕宽度”和“适配屏幕高度”这2个复选框

![项目设置](21.jpg)

我们在“场景一”中，创建一个“开始游戏”按钮，点击按钮则进入“场景二”：

1. 在“层级管理器”中创建画布：右键 - “创建” - “UI组件” - “Canvas (画布)”，并重命名为“CvsStart”
2. 在“场景编辑器”中，切换到“2D”视图，点击“3D”或者按下`2`快捷键
3. 在“层级管理器”中创建按钮：“Canvas”右键 - “创建” - “2D对象” - “Sprite (精灵)”
4. 在精灵的“属性检测器”中，调整精灵的大小、背景和组件：
  - 大小：“Content Size”中，宽`600`（像素），高`700`（像素）
  - 背景：“Sprite Frame”中，选择“default_panel”；“type”中，选择“SLICED”
  - 增加“cc.Widget”组件，并设置为“水平居中”、“垂直居中”
5. 在“层级管理器”中创建按钮：“Canvas”右键 - “创建” - “UI组件” - “Button (按钮)”，并重命名为“BtnStart”
6. 在按钮的“属性检测器”中，调整按钮大小和文案（“Label”的内容）

![场景一布局](22.jpg)

接下来，对“开始游戏”按钮增加点击事件，点击则切换到“场景二”界面：

+ 在“资源管理器”面板中，在`assets`中右键新建目录`scripts`，用户存放小游戏TypeScript脚本文件，我们新建一个名为`BtnStart`的脚本并双击打开它。
+ 在`BtnStart`的脚本内容如下：

```typescript
import { _decorator, Component, director, Node } from 'cc';
const { ccclass, property } = _decorator;

@ccclass('BtnStart')
export class BtnStart extends Component {
    start() {
        this.node.on(Node.EventType.TOUCH_END, this.toScene1, this);
    }

    protected onDestroy(): void {
        this.node.off(Node.EventType.TOUCH_END, this.toScene1, this);
    }

    private toScene1() {
        director.loadScene('SceneLevel1');
    }
}
```

+ 我们用`director.loadScene`方法切换不同的场景，从脚本可以看出：点击开始按钮，则切换到`SceneLevel1`场景。
+ 接下来，在“层级管理器”中选择“BtnStart”按钮，在“属性检查器”中，绑定点击事件：
  - 点击事件：设置“Click Events”为`1`，即代表只有1个事件
  - 点击函数：选择“BtnStart”节点，选择“BtnStart”脚本，最后选择“toScene1”函数

最终，我们运行“场景一”，就可以看到场景切换效果了：

![场景切换](23.jpg)


## 场景二：游戏场景（关卡一）

在“资源管理器”面板中，在`assets`中右键新建目录`scenes`，用户存放小游戏场景内容，我们新建一个名为`SceneLevel1`的场景并双击打开它。

然后，在“层级管理器”中创建画布：右键 - “创建” - “3D对象” - “Cube (立方体)”，并命名为`cube`

为了使“场景二”中“立方体”的效果更明确，我们给他创建红色材质：

+ 在“资源管理器”面板中，在`assets`中右键新建目录`materials`，用户存放小游戏节点的材质
+ 在`materials`创建材质：右键 - “创建” - “材质”，并命名为`cube`
+ 在`cube`材质的“属性检测器”中，设置“Albedo”颜色为红色（或者：其他颜色，只要看起来有效果即可），并报错材质
+ 拖动`cube`材质到`cube`立方体上，可以看到，立方体变成了红色

![材质设置](31.jpg)

# 总结

今天的分享就先到这儿，在小游戏的创意还没有明确之前，我们就先逐步搭建小游戏的整体流程。

---

Transformers 框架序列：

<small>[01.包和对象加载中的设计巧思与实用技巧](https://mp.weixin.qq.com/s/lAAIfl0YJRNrppp5-Vuusw)</small>

<small>[02.AutoModel 初始化及 Qwen2.5 模型加载全流程](https://mp.weixin.qq.com/s/WIbbrkf1HjVC1CtBNcU8Ow)</small>

<small>[03.Qwen2.5 大模型的 AutoTokenizer 技术细节](https://mp.weixin.qq.com/s/Shg30uUFByM0tKTi0rETfg)</small>

<small>[04.Qwen2.5/GPT 分词流程与 BPE 分词算法技术细节详解](https://mp.weixin.qq.com/s/GnoHXsIYKYFU1Xo4u5sE1w)</small>

<small>[05.嵌入（Embedding）机制和 Word2Vec 实战](https://mp.weixin.qq.com/s/qL9vpmNIM1eO9_lQq7QwlA)</small>

<small>[06.位置嵌入（Positional Embedding）](https://mp.weixin.qq.com/s/B0__TRnlI7zgwn0OhguvXA)</small>

Pipeline NLP 任务序列：

<small>[零·概述](https://mp.weixin.qq.com/s/FR4384AZV2FE2xtweSh9bA) 丨 [01.文本转音频](https://mp.weixin.qq.com/s/uN2BFIOxDFEh4T-W7tsPbg) 丨 [02.文本分类](https://mp.weixin.qq.com/s/9ccEDNfeGNf_Q9pO0Usg2w) 丨 [03.词元分类和命名实体识别](https://mp.weixin.qq.com/s/r2uFCwPZaMeDL_eiQsEmIQ) 丨 [04.问答](https://mp.weixin.qq.com/s/vOLVxRircw5wM1_rCqoAfg) 丨 [05.表格问答](https://mp.weixin.qq.com/s/Q0fWdw3ACVzQFldBScZ2Fw) | [06.填充蒙版](https://mp.weixin.qq.com/s/hMFCgYovHPVFOjOoihaUHw)</small>

往期推荐文章：

<small>[Cline 免费插件 + Qwen2.5 大模型，零经验也能开发“对联王”微信小程序](https://mp.weixin.qq.com/s/F-CUuaZwmqt6X7QkI_IrVA)</small>

<small>[使用Cursor + Qwen2.5 大模型 零经验研发微信小程序：自由构建个性化节拍器应用实战](https://mp.weixin.qq.com/s/vraegr_5AJG7bPo6mBgvbQ)</small>

<small>[Bolt.new 用一句话快速构建全栈应用：本地部署与应用实战（Ollama/Qwen2.5 等）](https://mp.weixin.qq.com/s/Mq8CvZKdpokbj3mK-h_SAQ)</small>

<small>[基于 Qwen2.5-Coder 模型和 CrewAI 多智能体框架，实现智能编程系统的实战教程](https://mp.weixin.qq.com/s/8f3xna9TRmxMDaY_cQhy8Q)</small>

<small>[vLLM CPU 和 GPU 模式署和推理 Qwen2 等大语言模型详细教程](https://mp.weixin.qq.com/s/KM-Z6FtVfaySewRTmvEc6w)</small>

<small>[基于 Qwen2/Lllama3 等大模型，部署团队私有化 RAG 知识库系统的详细教程（Docker+AnythingLLM）](https://mp.weixin.qq.com/s/PpY3k3kReKfQdeOJyrB6aw)</small>

<small>[使用 Llama3/Qwen2 等开源大模型，部署团队私有化 Code Copilot 和使用教程](https://mp.weixin.qq.com/s/vt1EXVWtwm6ltZVYtB4-Tg)</small>

<small>[基于 Qwen2 大模型微调技术详细教程（LoRA 参数高效微调和 SwanLab 可视化监控）](https://mp.weixin.qq.com/s/eq6K8_s9uX459OeUcRPEug)</small>

<small>[ChatTTS 长音频合成和本地部署 2 种方式，让你的“儿童绘本”发声的实战教程](https://mp.weixin.qq.com/s/9ldLuh3YLvx8oWvwnrSGUA)</small>

![微信公众号：老牛同学](https://ntopic.cn/WX-21.png)
