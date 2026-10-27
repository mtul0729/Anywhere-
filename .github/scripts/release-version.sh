#!/usr/bin/env bash
set -euo pipefail

usage() {
  echo "usage: $0 <tag>" >&2
  exit 2
}

[[ $# -eq 1 ]] || usage

tag=$1
if [[ $tag =~ ^v([0-9]+)\.([0-9]+)\.([0-9]+)(-alpha\.([0-9]+))?$ ]]; then
  major=${BASH_REMATCH[1]}
  minor=${BASH_REMATCH[2]}
  patch=${BASH_REMATCH[3]}
  alpha=${BASH_REMATCH[5]:-}
else
  echo "invalid release tag: $tag" >&2
  echo "expected vMAJOR.MINOR.PATCH or vMAJOR.MINOR.PATCH-alpha.N" >&2
  exit 1
fi

if (( minor > 99 || patch > 99 )); then
  echo "minor and patch versions must be between 0 and 99" >&2
  exit 1
fi

base=$((major * 1000000 + minor * 10000 + patch * 100))
if [[ -n $alpha ]]; then
  if (( alpha < 1 || alpha > 98 )); then
    echo "alpha number must be between 1 and 98" >&2
    exit 1
  fi
  version_code=$((base + alpha))
  prerelease=true
else
  version_code=$((base + 99))
  prerelease=false
fi

if (( version_code > 2100000000 )); then
  echo "versionCode $version_code exceeds Android's maximum" >&2
  exit 1
fi

echo "version_name=${tag#v}"
echo "version_code=$version_code"
echo "prerelease=$prerelease"
echo "version_major=$major"
echo "version_minor=$minor"
echo "version_patch=$patch"
echo "version_alpha=$alpha"
