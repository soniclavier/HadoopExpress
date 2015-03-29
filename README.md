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

c) starting the server
  1. from dir 'MlServer', run 'mvn clean package'
  2. sh target/bin/main
  3. check 127.0.0.1:8484 or localhost:8484

d) committing a change
  1. add any IDE file to be ignored into .gitignore file
  2. git pull origin master
  3. git add .
  4. git commit 
  5. git push 
