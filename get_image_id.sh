#!/bin/bash

# 获取docker images的输出，并提取第一个包含"mixding-nacos-proxy"的行
output=$(docker images | grep ssc |grep amd| head -n 1)

# 使用awk命令提取IMAGE ID字段，并输出
image_id=$(echo "$output" | awk '{print $3}')

echo $image_id