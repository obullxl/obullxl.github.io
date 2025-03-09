edge-tts

>pip install edge-tts
>pip install --upgrade edge-tts


>edge-tts --list-voices
Name                               Gender    ContentCategories      VoicePersonalities
---------------------------------  --------  ---------------------  --------------------------------------
zh-CN-XiaoxiaoNeural               Female    News, Novel            Warm
zh-CN-XiaoyiNeural                 Female    Cartoon, Novel         Lively
zh-CN-YunjianNeural                Male      Sports, Novel          Passion
zh-CN-YunxiNeural                  Male      Novel                  Lively, Sunshine
zh-CN-YunxiaNeural                 Male      Cartoon, Novel         Cute
zh-CN-YunyangNeural                Male      News                   Professional, Reliable
zh-CN-liaoning-XiaobeiNeural       Female    Dialect                Humorous
zh-CN-shaanxi-XiaoniNeural         Female    Dialect                Bright


>edge-playback -t "胡来" -v zh-CN-XiaoxiaoNeural --rate=-50% --volume=+50%
>edge-playback -t "胡来" -v zh-CN-YunyangNeural --rate=-50% --volume=+50%

usage: edge-tts [-h] [-t TEXT] [-f FILE] [-v VOICE] [-l] [--rate RATE] [--volume VOLUME] [--pitch PITCH]
                [--words-in-cue WORDS_IN_CUE] [--write-media WRITE_MEDIA] [--write-subtitles WRITE_SUBTITLES]
                [--proxy PROXY]

可以使用--rate、--volume和--pitch选项调整生成语音的语速、音量和音调：


>edge-tts --text "Hello, world!" --write-media hello.mp3 --write-subtitles hello.vtt


```python
import edge_tts

async def main():
communicate = edge_tts.Communicate(text="Hello, world!", voice="zh-CN-XiaoxiaoNeural")
await communicate.save("hello.mp3")

import asyncio
asyncio.run(main())
```


```python
import edge_tts
import asyncio

async def generate_audio(text, voice, output_file):
    communicate = edge_tts.Communicate(text, voice)
    await communicate.save(output_file)

asyncio.run(generate_audio("你好，世界！", "zh-CN-XiaoxiaoNeural", "output.mp3"))
```

```python
voices = edge_tts.list_voices()
print([v["Name"] for v in voices if "zh-CN" in v["Name"]])
```

```python
communicate = edge_tts.Communicate(
    text="示例文本",
    voice="zh-CN-YunxiNeural",
    rate="-30%",    # 语速降低30%
    volume="+20%",  # 音量提高20%
)
```

```python
async def generate_with_subtitles():
    communicate = edge_tts.Communicate(text, voice)
    submaker = edge_tts.SubMaker()
    async for chunk in communicate.stream():
        if chunk["type"] == "WordBoundary":
            submaker.create_sub(chunk)
    with open("output.vtt", "w") as f:
        f.write(submaker.generate_subs())
```


