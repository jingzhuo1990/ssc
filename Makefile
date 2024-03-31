IMG ?= ssc:0.0.1
IMG_amd64 ?= ssc:0.0.1-amd64
IMG_arm64 ?= ssc:0.0.1-arm64
DOCKER_REPO ?= zwdd-ops-registry.cn-hangzhou.cr.aliyuncs.com

package:
	mvn clean package

#暂时不用
docker_build:
	docker buildx build --no-cache --platform=linux/amd64,linux/arm64 --push -f APP-META/docker-config/dockerfile -t ${IMG} .

#用这个
docker_local:
	docker buildx build --no-cache --platform=linux/arm64 --load -f APP-META/docker-config/dockerfile -t ${IMG_arm64} .
	docker buildx build --no-cache --platform=linux/amd64 --load -f APP-META/docker-config/dockerfile -t ${IMG_amd64} .


docker_push:
	@image_id=$$(bash get_image_id.sh); \
	docker tag $$image_id $(DOCKER_REPO)/zeroplus/${IMG}
	docker push $(DOCKER_REPO)/zeroplus/${IMG}

docker_run:
	docker run -d -p 8080:8080 ${IMG_arm64}

#云上
#docker network create local-mysql
#docker run -itd --name mysql-test --network local-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:5.7
#docker pull zwdd-ops-registry.cn-hangzhou.cr.aliyuncs.com/zeroplus/ssc:0.0.1
#docker run -d -p 8085:8085 --name ssc --network local-mysql zwdd-ops-registry.cn-hangzhou.cr.aliyuncs.com/zeroplus/ssc:0.0.1

#本地
#-Dspring.profiles.active=local