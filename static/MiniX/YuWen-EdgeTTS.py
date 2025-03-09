import json
import os
import sys
import asyncio
import edge_tts

async def generate_audio(term, output_file):
    temp_file = output_file + '.tmp'
    try:
        communicate = edge_tts.Communicate(
            text=term,
            voice='zh-CN-XiaoxiaoNeural',
            rate='-50%',
            volume='+50%'
        )
        await communicate.save(temp_file)
        
        if not os.path.exists(temp_file):
            raise Exception("临时文件未生成")
        if os.path.getsize(temp_file) < 1024:
            raise Exception("音频文件过小，可能生成失败")
            
        os.replace(temp_file, output_file)
        return True
    except Exception as e:
        if os.path.exists(temp_file):
            try:
                os.remove(temp_file)
            except:
                pass
        print(f"生成失败：{term}，错误：{str(e)}")
        return False
    finally:
        if os.path.exists(temp_file):
            try:
                os.remove(temp_file)
            except:
                pass

async def main():
    if len(sys.argv) < 2:
        print("用法：python script.py <JSON文件路径>")
        return

    json_file = sys.argv[1]
    
    try:
        with open(json_file, 'r', encoding='utf-8') as f:
            data = json.load(f)
    except FileNotFoundError:
        print(f"错误：文件 {json_file} 不存在")
        return
    except json.JSONDecodeError:
        print(f"错误：文件 {json_file} 包含无效的JSON格式")
        return

    try:
        output_dir = str(data['useGrade']['id'])
    except KeyError:
        print("错误：JSON中缺少 useGrade.id 字段")
        return

    all_terms = []
    for module in data.get('moduleList', []):
        for unit in module.get('unitList', []):
            for word in unit.get('wordList', []):
                all_terms.extend(word.get('termList', []))
                all_terms.extend(word.get('extTermList', []))

    seen = set()
    unique_terms = [t for t in all_terms if not (t in seen or seen.add(t))]

    os.makedirs(output_dir, exist_ok=True)

    for term in unique_terms:
        if not term.strip():
            continue
            
        output_path = os.path.join(output_dir, f"{term}.mp3")
        
        # 改进后的存在性检查（新增文件大小验证）
        if os.path.exists(output_path):
            if os.path.getsize(output_path) > 1024:  # 验证文件大小
                print(f"有效文件已存在，跳过：{output_path}")
                continue
            else:
                print(f"检测到损坏文件，重新生成：{output_path}")
        
        print(f"正在生成：{term}")
        success = await generate_audio(term, output_path)
        if not success:
            if os.path.exists(output_path):
                try:
                    os.remove(output_path)
                except:
                    pass

if __name__ == '__main__':
    asyncio.run(main())
