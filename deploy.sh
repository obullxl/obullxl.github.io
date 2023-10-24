sudo rm -rf ./public
sudo ln -s ../obullxl-gitee/public ./public
sudo hugo -D --config config-GiteeIO.yaml
cd ../obullxl-gitee
git add --all
git commit -m 'Deploy public'
git push

open -a '/Applications/Microsoft Edge.app' https://gitee.com/obullxl/obullxl/pages


