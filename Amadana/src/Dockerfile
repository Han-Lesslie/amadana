#基础镜像
FROM java:8
#维护者
MAINTAINER han
LABEL version="1.0" \
      description="images of amadana" \
      company="amadana"

# 将jar包拷贝到容器的根目录下
COPY amadana.jar /amadana.jar
RUN echo "<<<<======== start to build image ========>>>>"
#工作目录
WORKDIR /amadana
#挂载目录
VOLUME /amadana
#暴露端口
EXPOSE 8089
#执行启动命令
ENTRYPOINT ["java","-jar","/amadana.jar"]
