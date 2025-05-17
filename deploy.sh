#!/bin/bash

# ===== 사용자 설정 =====
DOCKER_USER="chaewonjeong"
IMAGE_NAME="bongjangboo-backend"

# ===== 태그 자동 생성 =====
DATE_TAG=$(date +%Y%m%d-%H%M)
GIT_COMMIT=$(git rev-parse --short HEAD)

# 기본 태그 (main 기준)
TAG_MAIN="main-latest"
TAG_DATE="build-${DATE_TAG}"
TAG_COMMIT="commit-${GIT_COMMIT}"

# ===== 빌더 준비 =====
echo "🔧 Buildx 빌더 확인 중..."
docker buildx inspect multi-builder >/dev/null 2>&1 || docker buildx create --name multi-builder --use
docker buildx use multi-builder

# ===== 멀티 플랫폼 빌드 및 푸시 =====
echo "🚀 Docker 이미지 빌드 및 푸시 시작..."
if docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -t ${DOCKER_USER}/${IMAGE_NAME}:${TAG_MAIN} \
  -t ${DOCKER_USER}/${IMAGE_NAME}:${TAG_DATE} \
  -t ${DOCKER_USER}/${IMAGE_NAME}:${TAG_COMMIT} \
  --push .; then

  echo ""
  echo "✅ Docker 이미지가 성공적으로 푸시되었습니다:"
  echo "   - ${DOCKER_USER}/${IMAGE_NAME}:${TAG_MAIN}"
  echo "   - ${DOCKER_USER}/${IMAGE_NAME}:${TAG_DATE}"
  echo "   - ${DOCKER_USER}/${IMAGE_NAME}:${TAG_COMMIT}"

else
  echo "❌ Docker 이미지 빌드 또는 푸시에 실패했습니다. 로그를 확인하세요."
  exit 1
fi

