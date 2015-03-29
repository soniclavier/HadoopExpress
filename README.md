# HadoopExpress

ML Server dev setup stpes :

a) checkout
  1. cd to any base folder 
  2. git init hadoopexpress
  3. git remote add -f origin https://github.com/soniclavier/HadoopExpress.git
  4. git config core.sparseCheckout true
  5. echo "MlServer" >> .git/info/sparse-checkout
  6. git pull origin master

b) eclipse setup
  1. select file > import > maven > existing maven projects 
  2. select MlServer as the root directory

c) committing a change
  1. git add <changed file>
  2. git commit
