**这个是springboot+maven（jdk1.8）多模块，模块打包方式是jar，一次性的通过github actions部署到多个herokuApp上的列子下面是.github\workflows/main.yml文件内容（其中appdir填写的是你子模块所在的目录）**

```yml
name: Deploy

on:
  push:
    branches:
      - master

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: akhileshns/heroku-deploy@v3.6.8
        with:
          heroku_api_key: ${{secrets.HEROKU_API_KEY}}
          heroku_app_name: your heroku app name
          heroku_email: your email addr
          appdir: module1
      - uses: akhileshns/heroku-deploy@v3.6.8
        with:
          heroku_api_key: ${{secrets.HEROKU_API_KEY}}
          heroku_app_name: your heroku app name
          heroku_email: your email addr
          appdir: module2
```

**需要注意以下几点：**

- 首先在你要提交中github仓库中Settings->Secrets->New repository secret添加heroku设置中Account settings->API Key。

- 还需要在多模块中子模块的pom.xml文件中会引入字节的parent,这个会导致在导入时候提示找不到这个包，需要在子模块的pom.xml文件中利用<relativePath>标签去指定父模块的pom.xml位置，但是我试了好多次在里面指出父模块中的pom.xml不起作用，网上看了别人解释，需要将父模块的pom.xml文件copy到子模块的根目录下（将父模块的pom.xml改为parent_pom.xml），然后在<relativePath>标签中指出这个文件所在的位置。

  ```xml
  <parent>
          <artifactId>actionTest</artifactId>
          <groupId>com.tigerff</groupId>
          <version>1.0-SNAPSHOT</version>
          <relativePath>parent_pom.xml</relativePath>
  	    <!--指出父模块中pom.xml文件所在的位置-->
      </parent>
  ```

- 还有就是在对子模块进行打包的时候要指出其启动类的所在位置：

  ```xml
  <build>
          <plugins>
              <plugin>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-maven-plugin</artifactId>
                  <version>2.3.4.RELEASE</version>
                  <!--填写你自己需要的版本号-->
                  <configuration>
                      <mainClass>module1.Module1Application</mainClass>
                      <!--启动类所在的全包名-->
                  </configuration>
                  <executions>
                      <execution>
                          <goals>
                              <goal>repackage</goal>
                          </goals>
                      </execution>
                  </executions>
  
              </plugin>
          </plugins>
      </build>
  ```

- 不要忘了，对heroku部署，需要添加Profile文件，内容如下（这个内容不用改，只要你是用jar打包方式去部署到heroku）：

  ```
  web: java $JAVA_OPTS -Dserver.port=$PORT -jar target/*.jar
  ```

  