+++
slug = "2024060901"
date = "2024-06-08"
lastmod = "2024-06-08"
title = "ChatTTS 对话文本转语音开源模型的部署教程和使用方法（建议收藏）"
description = "ChatTTS是一款专为对话场景设计的支持中英文的文本转语音（TTS）模型，能够生成高质量、自然流畅的对话语音，同时还支持笑声、停顿等韵律特征，超越了大部分TTS开源模型……"
image = "00.jpg"
tags = [ "AI工具", "ChatTTS", "大模型", "TTS" ]
categories = [ "人工智能" ]
+++

**ChatTTS**（Chat Text To Speech）是专为对话场景设计的文本生成语音(TTS)模型，特别适用于大型语言模型(**LLM**)助手的对话任务，以及诸如对话式音频和视频介绍等应用。它支持中文和英文，还可以穿插笑声、说话间的停顿、以及语气词等，听起来很真实自然，在语音合成中表现出高质量和自然度（ChatTTS团队声称：**突破开源天花板**）。

同时，**ChatTTS**模型文件总大小**1.1GB**左右，常用的个人笔记本电脑均可部署，因此涉及到文本转语音场景，均可以自己操作转换了！

## ChatTTS特点

由于ChatTTS以下极具吸引人的特点，使得它一经推出就成为了爆款：

+ **多语言支持**：ChatTTS的一个关键特性是支持多种语言，包括英语和中文。这使其能够为广泛用户群提供服务，并克服语言障碍。
+ **大规模数据训练**：ChatTTS使用了大量数据进行训练，大约有1000万小时的中文和英文数据。这样的大规模训练使其声音合成质量高，听起来自然。
+ **对话任务兼容性**：ChatTTS很适合处理通常分配给大型语言模型LLMs的对话任务。它可以为对话生成响应，并在集成到各种应用和服务时提供更自然流畅的互动体验。
+ **开源计划**：ChatTTS团队目前开源一个经过训练的基础模型。
+ **控制和安全性**：ChatTTS致力于提高模型的可控性，添加水印，并将其与LLMs集成。这些努力确保了模型的安全性和可靠性。
+ **易用性**：ChatTTS为用户提供了易于使用的体验。它只需要文本信息作为输入，就可以生成相应的语音文件。这样的简单性使其方便有语音合成需求的用户。

## 下载ChatTTS模型文件

因最大模型文件超过**900MB**，为了防止使用Git无法直接下载到本地，我们通过**git-lfs**工具包下载：

```shell
brew install git-lfs
```

通过Git复制模型文件到笔记本电脑（文件夹：`ChatTTS-Model`）：

```shell
git lfs install
git clone https://www.modelscope.cn/pzc163/chatTTS.git ChatTTS-Model
```

如果因网络不佳等原因，下载中断，我们可以通过以下命令在中断后继续下载：

```shell
git lfs pull
```

![ChatTTS模型文件列表](01.jpg)

## 安装ChatTTS依赖包列表

下载**ChatTTS**官网GitHub源码：

```
git clone https://gitcode.com/2noise/ChatTTS.git ChatTTS
```

进入源码目录，批量安装**Python依赖包**：

```
pip install -r requirements.txt
```

**特别注意**：如果下载过程中，若出现找不到`torch`的**2.1.0**版本错误，请修改`requirements.txt`文件，把`torch`的版本修改为**2.2.2**后再次执行安装：

![torch版本找不到](02.jpg)

Python依赖包列表`requirements.txt`文件如下，我们也可以手工一个一个的进行安装，无需下载整个源码（注意：`torch`的版本号为**2.2.2**）：

```plaintext
omegaconf~=2.3.0
torch~=2.2.2
tqdm
einops
vector_quantize_pytorch
transformers~=4.41.1
vocos
IPython
```

## ChatTTS中文文本转音频文件

**特别注意**：经老牛同学的验证，**ChatTTS**官网的样例代码API已经过时，无法直接运行，特别是`chat.load_models`方法入参是错误的，下面是老牛同学通过阅读API入参且验证的可执行代码。

```python
# ChatTTS-01.py

import ChatTTS
import torch
import torchaudio

# 第一步下载的ChatTTS模型文件目录，请按照实际情况替换
MODEL_PATH = '/Users/obullxl/PythonSpace/ChatTTS-Model'

# 初始化并加载模型，特别注意加载模型参数，官网样例代码已经过时，请使用老牛同学验证代码
chat = ChatTTS.Chat()
chat.load_models(
    vocos_config_path=f'{MODEL_PATH}/config/vocos.yaml',
    vocos_ckpt_path=f'{MODEL_PATH}/asset/Vocos.pt',
    gpt_config_path=f'{MODEL_PATH}/config/gpt.yaml',
    gpt_ckpt_path=f'{MODEL_PATH}/asset/GPT.pt',
    decoder_config_path=f'{MODEL_PATH}/config/decoder.yaml',
    decoder_ckpt_path=f'{MODEL_PATH}/asset/Decoder.pt',
    tokenizer_path=f'{MODEL_PATH}/asset/tokenizer.pt',
)

# 需要转化为音频的文本内容
text = '大家好，我是老牛，微信公众号：老牛同学。很高兴与您相遇，专注于编程技术、大模型及人工智能等相关技术分享，欢迎关注和转发，让我们共同启程智慧之旅！'

# 文本转为音频
wavs = chat.infer(text, use_decoder=True)

# 保存音频文件到本地文件（采样率为24000Hz）
torchaudio.save("./output/output-01.wav", torch.from_numpy(wavs[0]), 24000)
```

运作Python代码：`python ChatTTS-01.py`

```plaintext
$ python ChatTTS-01.py
WARNING:ChatTTS.utils.gpu_utils:No GPU found, use CPU instead
INFO:ChatTTS.core:use cpu
INFO:ChatTTS.core:vocos loaded.
INFO:ChatTTS.core:gpt loaded.
INFO:ChatTTS.core:decoder loaded.
INFO:ChatTTS.core:tokenizer loaded.
WARNING:ChatTTS.core:dvae not initialized.
INFO:ChatTTS.core:All initialized.
 20%|██████████████████████████▌                    | 76/384 [00:08<00:35,  8.62it/s]
 26%|██████████████████████████████████▌            | 536/2048 [00:48<02:17, 10.98it/s]
```

上述文本转音频程序执行完成，在本地目录生成了`./output/output-01.wav`音频文件，打开该音频文件，就可以听到非常自然流畅的语音了！

我们也可以在文本转换成语音之后，直接播放语音内容：

```python
# …… 其他包引用省略
from IPython.display import Audio

# …… 其他部分代码省略

# 播放生成的音频（autoplay=True 代表自动播放）
Audio(wavs[0], rate=24000, autoplay=True)
```

---

关注本公众号，我们共同学习进步👇🏻👇🏻👇🏻

![微信公众号：老牛同学](https://ntopic.cn/WX-21.png)

---

我的本博客原地址：[https://mp.weixin.qq.com/s/g7lDfnRRGdrHqN7WGMSkAg](https://mp.weixin.qq.com/s/g7lDfnRRGdrHqN7WGMSkAg)

---
