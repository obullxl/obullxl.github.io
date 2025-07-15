rmdir /s /q .\public
hugo -D --config config-GiteeIO.yaml
xcopy .\public\* ..\me-blog\ /s /y
cd ../
cd me-blog
git add --all
git commit -m 'Deploy public'
git push

open -a '/Applications/Microsoft Edge.app' https://console.cloud.tencent.com/edgeone/pages/project/pages-gyexz14urain/deployments
