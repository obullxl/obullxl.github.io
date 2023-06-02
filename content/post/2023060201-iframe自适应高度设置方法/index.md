+++
slug = "2023060201"
date = "2023-06-02"
lastmod = "2023-06-02"
title = "iframe自适应高度设置方法"
description = "为了更好运维和隔离影响，一般会把网站的公用模块抽取独立出来（如博客中常见的评论系统），通过iframe的方式集成到主页面。iframe集成的最大问题是页面的一致性保障，如样式风格一致、iframe内容高度能自适应（不能出现多个滚动条）等……"
#image = "images/01.jpg"
tags = [ "博客空间", "iframe", "自适应" ]
categories = [ "专业技术" ]
+++

## 思路说明
- iframe的宽度伸张铺满**100%**
- 主页面加载完成之后，注册定时任务：每隔**250毫秒** 去检测一下iframe的高度是否变化，并重新调整iframe高度（如提交评论，iframe的高度会变高，此时主页面会自动调整）

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no">
    <title>ntopic.cn</title>
    <style type="text/css">
        .iframe-wrap {
            position: relative;
            box-sizing: border-box;
            width: 100%;
            max-width: unset;
            margin-left: auto;
            margin-right: auto;
        }

        .iframe {
            color-scheme: light;
            position: absolute;
            left: 0;
            right: 0;
            width: 1px;
            min-width: 100%;
            max-width: 100%;
            height: 100%;
            border: 0;
        }
    </style>
    <script type="text/javascript">
        function onWindowLoad() {
            window.setInterval("reInitIframe()", 250);
        }

        function reInitIframe() {
            const iframe = document.getElementById("iframe");
            try {
                const newHeight = iframe.contentWindow.document.documentElement.scrollHeight;
              	iframe.style.height = newHeight + "px";
                document.getElementById("iframeWrap").style.height = newHeight + "px";
            } catch (e) {
            }
        }
    </script>
</head>
<body onload="onWindowLoad();">
<div id="iframeWrap" class="iframe-wrap" style="height: 269px;">
    <iframe id="iframe" class="frame" src="https://ntopic.cn" loading="lazy" style="width:100%;" frameborder="0" scrolling="no"></iframe>
</div>
</body>
</html>
```
