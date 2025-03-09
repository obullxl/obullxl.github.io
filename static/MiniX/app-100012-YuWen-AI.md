下面是一篇小学课文内容的JSON配置，配置项说明如下：
1. wordList 是生字列表的配置，每个配置项：
 - word 代表具体单个中文生字，wordPinYin 是对应的拼音内容
 - termList 是word生字的常见词语列表，termPinYinList 是词语列表的拼音
2. sentenceList 是包含生字的句子，目的是让小学生更容易记住生字。

请根据目前JSON配置，完成以下工作：
1. 补充 wordPinYin 生字拼音，补充 termPinYinList 常见词语拼音。
2. 参考 termList 词语列表的形式，额外补充3个常见词语，放入到 extTermList 配置项中，extTermPinYinList 则为额外词语的拼音。
3. 参考 sentenceList 内容形式和格式，额外补充5个常见句子，放入到 extSentenceList 配置项中。

要求：
1. 拼音需要增加对应的声调。
2. 配置项每个汉字的拼音之间，使用英文空格分隔。
3. 严格按照JSON格式返回，禁止返回其他任何内容。

```json
{
      "title": "第二单元",
      "object": "阅读",
      "unitList": [{
  "page": "",
  "title": "热爱中国共产党",
  "titlePinYin": "",
  "checkTermCount": 4,
  "wordList": [
    {
      "word": "共",
      "wordPinYin": "",
      "termList": ["一共", "共同", "生死与共"],
      "termPinYinList": [],
      "extTermList": [],
      "extTermPinYinList": []
    },
    {
      "word": "产",
      "wordPinYin": "",
      "termList": ["生产", "出产", "土特产"],
      "termPinYinList": [],
      "extTermList": [],
      "extTermPinYinList": []
    },
    {
      "word": "党",
      "wordPinYin": "",
      "termList": ["党员", "党章", "共产党"],
      "termPinYinList": [],
      "extTermList": [],
      "extTermPinYinList": []
    },
    {
      "word": "太",
      "wordPinYin": "",
      "termList": ["太阳", "太平", "欺人太甚"],
      "termPinYinList": [],
      "extTermList": [],
      "extTermPinYinList": []
    },
    {
      "word": "阳",
      "wordPinYin": "",
      "termList": ["阳光", "夕阳", "阴差阳错"],
      "termPinYinList": [],
      "extTermList": [],
      "extTermPinYinList": []
    },
    {
      "word": "光",
      "wordPinYin": "",
      "termList": ["月光", "阳光", "金光闪闪"],
      "termPinYinList": [],
      "extTermList": [],
      "extTermPinYinList": []
    }
  ],
  "sentenceList": ["我们热爱中国共产党。"],
  "extSentenceList": []
}]
    }
```


























{
  "page": "",
  "title": "姓氏歌",
  "titlePinYin": "",
  "checkTermCount": 4,
  "wordList": [
    {
      "word": "什",
      "wordPinYin": "",
      "termList": ["什么", "为什么", ""],
      "termPinYinList": [],
      "extTermList": [],
      "extTermPinYinList": []
    },
    {
      "word": "么",
      "wordPinYin": "",
      "termList": ["什么", "多么", ""],
      "termPinYinList": [],
      "extTermList": [],
      "extTermPinYinList": []
    },
    {
      "word": "古",
      "wordPinYin": "",
      "termList": ["千古", "古老", "古今中外"],
      "termPinYinList": [],
      "extTermList": [],
      "extTermPinYinList": []
    },
    {
      "word": "胡",
      "wordPinYin": "",
      "termList": ["胡子", "二胡", "胡思乱想"],
      "termPinYinList": [],
      "extTermList": [],
      "extTermPinYinList": []
    },
    {
      "word": "双",
      "wordPinYin": "",
      "termList": ["双人", "双手", "举世无双"],
      "termPinYinList": [],
      "extTermList": [],
      "extTermPinYinList": []
    },
    {
      "word": "言",
      "wordPinYin": "",
      "termList": ["名言", "语言", "自言自语"],
      "termPinYinList": [],
      "extTermList": [],
      "extTermPinYinList": []
    }
  ],
  "sentenceList": ["你姓什么？我姓王"],
  "extSentenceList": []
}







下面是一篇小学课文内容的JSON文件的配置内容样例，文件名为`app-100012-YuWen.json`，配置项说明如下：
1. moduleList 是模块列表，每个元素代表一个模块；每个模块包含多个单元列表，由 unitList 表示；每个单元包含多个生字和生词列表，由 wordList 表示。
2. wordList 是生字列表的配置，每个配置项：
 - word 代表具体单个中文生字，wordPinYin 是对应的拼音内容
 - termList 是word生字的常见词语列表，termPinYinList 是词语列表的拼音
 - extTermList 是word生字的扩展的词语列表，extTermPinYinList 是扩展词语列表的拼音
3. sentenceList 是包含生字的句子，目的是让小学生更容易记住生字。

请根据JSON文件配置内容，写一个Python程序，实现功能：
1. 解析JSON文件，读取 termList 和 extTermList 词语列表；
2. 使用edge-tts库，对每个词语生成mp3格式音频;生成的mp3文件存储路径为当前目录下的`100012`子目录，mp3文件名为`{词语}.mp3`；
3. 调用edge-tts的参数要求：
 - 声音使用'zh-CN-XiaoxiaoNeural'
 - 语速为'-50%'
 - 音量为'+50%'
 - 不需要字幕


程序可以正常工作了，现在请新增功能：
1. JSON文件名（如：`app-100012-YuWen.json`）支持输入
2. 单词mp3文件的目录，由JSON文件的`useGrade`属性对象的`id`属性获取
3. 如果某个词语的mp3格式音频文件已经存在，则忽略这个词语，继续生成下一个词语的mp3格式音频文件

```json
{
  "version": "1.0.1",
  "subject": "语文",
  "useGrade": {
    "id": 100012,
    "text": "一年级（下）"
  },
  "moduleList": [
    {
      "title": "第一单元",
      "object": "识字",
      "unitList": [
        {
          "page": "",
          "title": "春夏秋冬",
          "titlePinYin": "chūn xià qiū dōng",
          "checkTermCount": 4,
          "wordList": [
            {
              "word": "春",
              "wordPinYin": "chūn",
              "termList": ["春日", "春节", "春雨如油"],
              "termPinYinList": ["chūn rì", "chūn jié", "chūn yǔ rú yóu"],
              "extTermList": ["春风拂面", "春暖花开", "春华秋实"],
              "extTermPinYinList": ["chūn fēng fú miàn", "chūn nuǎn huā kāi", "chūn huá qiū shí"]
            },
            {
              "word": "冬",
              "wordPinYin": "dōng",
              "termList": ["冬天", "冬日", "寒冬腊月"],
              "termPinYinList": ["dōng tiān", "dōng rì", "hán dōng là yuè"],
              "extTermList": ["冬去春来", "冬眠动物", "冬雪皑皑"],
              "extTermPinYinList": ["dōng qù chūn lái", "dōng mián dòng wù", "dōng xuě ái ái"]
            },
            {
              "word": "吹",
              "wordPinYin": "chuī",
              "termList": ["吹风", "吹牛", "风吹雨打"],
              "termPinYinList": ["chuī fēng", "chuī niú", "fēng chuī yǔ dǎ"],
              "extTermList": ["吹气球", "吹喇叭", "吹毛求疵"],
              "extTermPinYinList": ["chuī qì qiú", "chuī lǎ ba", "chuī máo qiú cī"]
            },
            {
              "word": "花",
              "wordPinYin": "huā",
              "termList": ["花草", "花朵", "五花八门"],
              "termPinYinList": ["huā cǎo", "huā duǒ", "wǔ huā bā mén"],
              "extTermList": ["花香四溢", "花红柳绿", "百花争艳"],
              "extTermPinYinList": ["huā xiāng sì yì", "huā hóng liǔ lǜ", "bǎi huā zhēng yàn"]
            },
            {
              "word": "飞",
              "wordPinYin": "fēi",
              "termList": ["飞虫", "飞机", "龙飞凤舞"],
              "termPinYinList": ["fēi chóng", "fēi jī", "lóng fēi fèng wǔ"],
              "extTermList": ["飞黄腾达", "飞檐走壁", "一飞冲天"],
              "extTermPinYinList": ["fēi huáng téng dá", "fēi yán zǒu bì", "yī fēi chōng tiān"]
            },
            {
              "word": "入",
              "wordPinYin": "rù",
              "termList": ["加入", "出入", "入木三分"],
              "termPinYinList": ["jiā rù", "chū rù", "rù mù sān fēn"],
              "extTermList": ["入门基础", "深入浅出", "不入虎穴", "焉得虎子"],
              "extTermPinYinList": ["rù mén jī chǔ", "shēn rù qiǎn chū", "bù rù hǔ xué", "yān dé hǔ zǐ"]
            }
          ],
          "sentenceList": ["春天到了，花儿红了，草儿绿了。"],
          "extSentenceList": [
            "冬天来了，北风呼啸，雪花飘落。",
            "风吹过田野，带来了阵阵清香。",
            "花儿在阳光下绽放，蜜蜂和蝴蝶翩翩起舞。",
            "小鸟在天空中自由自在地飞翔。",
            "小明加入了学校的足球队，每天练习踢球。"
          ]
        }
      ]
    }
  ]
}
```


现在，我们有了这些词语mp3音频文件，我们就可以根据JSON配置文件，为小学生编写一个“词语默写”的小程序页面了。
页面功能描述如下：
1. 支持用户选择难度等级，从低到高分别为：基础，适中，拓展
2. 支持用户选择模块（选择项展示模块属性`title`）和模块单元（选择项展示单元对象的属性`title`），或者选择全部单元（即该模块下所有的单元）
3. 支持用户设置默写的词语数量，默认为`16`个
4. 选择完毕，点击开始默写按钮，小程序开始执行：
 - 根据选择的难度等级，随机挑选词语列表，当词语数据不足设置的数量时，则使用全部词语
   - 基础：只挑选`termList`中2个字的词语
   - 适中：只挑选`termList`中的词语，包括2个字和多个字词语
   - 拓展：挑选`termList`和`extTermList`中的词语
 - 对于随机挑选的词语列表中的每个词语，播放让学生模型
   - 每个词语重复播放3次，每次间隔3秒钟
   - 每个词语播放完成之后，暂停5秒钟
 - 学生可以随时暂停自动播放，或者点击下一个词语，直接播放下一个词语
5. 小程序的基本配置已经搭建好：
 - Page名为`dictation-yuwen`
 - JSON配置文件和mp3音频文件的远程服务器地址为：`http://ntopic.local`
 - JSON配置文件的远程相对地址为：`./MiniX/app-100012-YuWen.json`
 - 每个词语的mp3文件远程相对地址格式为：`./MiniX/${useGrade.id}/{词语}.mp3`
6. 要求：尽量使用"weui-wxss"框架的“2.6.21”版本，保持页面风格一致性




















