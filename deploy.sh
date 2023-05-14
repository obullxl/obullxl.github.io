sudo rm -rf ./public
sudo ln -s ../obullxl-gitee/public ./public
sudo hugo -D
cd ../obullxl-gitee
git add --all
git commit -m 'Deploy public'
git push



