#!/bin/bash -xeu

export GITHUB_TOKEN=${GITHUB_TOKEN_PSW}
export GITHUB_ORG=zeebe-io
export GITHUB_REPO=zeebe

curl -sL https://github.com/aktau/github-release/releases/download/v0.7.2/linux-amd64-github-release.tar.bz2 | tar xjvf - --strip 3
GITHUB_RELEASE=${PWD}/github-release
CHANGELOG=`cat /tmp/CHANGELOG`

${GITHUB_RELEASE} release --user ${GITHUB_ORG} --repo ${GITHUB_REPO} --tag ${RELEASE_VERSION} --draft --name "Zeebe ${RELEASE_VERSION}" --description "${CHANGELOG}"

function upload {
  pushd ${1}

  local artifact=${2}
  local checksum=${artifact}.sha1sum

  sha1sum ${artifact} > ${checksum}

  ${GITHUB_RELEASE} upload --user ${GITHUB_ORG} --repo ${GITHUB_REPO} --tag ${RELEASE_VERSION} --name "${artifact}" --file "${artifact}"
  ${GITHUB_RELEASE} upload --user ${GITHUB_ORG} --repo ${GITHUB_REPO} --tag ${RELEASE_VERSION} --name "${checksum}" --file "${checksum}"

  popd
}

upload dist/target zeebe-distribution-${RELEASE_VERSION}.tar.gz
upload dist/target zeebe-distribution-${RELEASE_VERSION}.zip
upload clients/zbctl/dist zbctl
upload clients/zbctl/dist zbctl.exe
upload clients/zbctl/dist zbctl.darwin
