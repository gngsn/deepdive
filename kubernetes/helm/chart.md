## Charts

Helm uses a packaging format called charts. A chart is a collection of files that describe a related set of Kubernetes resources. A single chart might be used to deploy something simple, like a memcached pod, or something complex, like a full web app stack with HTTP servers, databases, caches, and so on.
Charts are created as files laid out in a particular directory tree. They can be packaged into versioned archives to be deployed.
If you want to download and look at the files for a published chart, without installing it, you can do so with helm pull chartrepo/chartname.
This document explains the chart format, and provides basic guidance for building charts with Helm.

<br/>

### The Chart File Structure
하나의 Chart는 하나의 디렉터리에 일종의 파일 묶음 (Collection of files) 입니다. 해당 디렉터리 이름이 바로 Chart의 이름이 됩니다 (버전 정보 제외). 가령, wordpress 디렉터리를 살펴보면 아래와 같이 wordpress/ 디렉터리 하위에 Chart들이 구성되어 있습니다.

<br/>

Helm은 다음과 같은 구조의 디렉터리 구성을 가집니다.

```
wordpress/
  Chart.yaml          # chart 정보를 갖는 하나의 YAML file
  LICENSE             # OPTIONAL: chart의 license 정보를 갖는 텍스트 파일
  README.md           # OPTIONAL: README 파일
  values.yaml         # 해당 chart에서 사용할 기본 환경설정 값들 (default configuration values)
  values.schema.json  # OPTIONAL: values.yaml 파일 구조를 제약하기 위한 A JSON Schema
  charts/             # 해당 차트에 의존하는 차트들을 보관하는 directory
  crds/               # Custom 자원 정의 모음
  templates/          # 템플릿이 values와 결합될 때,
                      # 유효한 쿠버네티스 manifest 파일들을 생성할 템플릿들의 디렉터리
  templates/NOTES.txt # OPTIONAL: 간단한 사용법을 다룬 텍스트 파일
```

Helm은 `charts/`, `crds/`, `templates/` 디렉터리 명과 위 목록의 파일 명을 미리 예약하여 사용하고 있습니다. 다른 이름들은 그 자체로 남습니다.

<br/>

### The Chart.yaml File

`Chart.yaml` 파일은 Chart를 위해 필수로 정의되어야 합니다.
해당 파일은 아래와 같은 필드들이 포함됩니다.

<pre>
<b>apiVersion</b>: The chart API version (required)
<b>name</b>: The name of the chart (required)
<b>version</b>: A SemVer 2 version (required)
<b>kubeVersion</b>: A SemVer range of compatible Kubernetes versions (optional)
<b>description</b>: A single-sentence description of this project (optional)
<b>type</b>: The type of the chart (optional)
<b>keywords</b>:
  - A list of keywords about this project (optional)
<b>home</b>: The URL of this projects home page (optional)
<b>sources</b>:
  - A list of URLs to source code for this project (optional)
<b>dependencies</b>: # A list of the chart requirements (optional)
  - <b>name</b>: The name of the chart (nginx)
    <b>version</b>: The version of the chart ("1.2.3")
    <b>repository</b>: (optional) The repository URL ("https://example.com/charts") or alias ("@repo-name")
    <b>condition</b>: (optional) A yaml path that resolves to a boolean, used for enabling/disabling charts (e.g. subchart1.enabled )
    <b>tags</b>: # (optional)
      - Tags can be used to group charts for enabling/disabling together
    <b>import-values</b>: # (optional)
      - ImportValues holds the mapping of source values to parent key to be imported. Each item can be a string or pair of child/parent sublist items.
    <b>alias</b>: (optional) Alias to be used for the chart. Useful when you have to add the same chart multiple times
<b>maintainers</b>: # (optional)
  - <b>name</b>: The maintainers name (required for each maintainer)
    <b>email</b>: The maintainers email (optional for each maintainer)
    <b>url</b>: A URL for the maintainer (optional for each maintainer)
<b>icon</b>: A URL to an SVG or PNG image to be used as an icon (optional).
<b>appVersion</b>: The version of the app that this contains (optional). Needn't be SemVer. Quotes recommended.
<b>deprecated</b>: Whether this chart is deprecated (optional, boolean)
<b>annotations</b>:
  <b>example</b>: A list of annotations keyed by name (optional).
</pre>

`v3.3.2` 부터, 추가적인 필드들을 기입할 수 없게 됩니다. 추천하는 방식은 custom metadata를 annotations에 작성하는 것입니다.

<br/>

#### ✔️ apiVersion

최소 Helm 3 를 필요로하는 Helm Chart를 사용하기 위해선 apiVersion 필드는 v2 이어야 합니다.
이전 Helm 버전을 지원하는 Chart는 v1 로 설정된 apiVersion 를 가지고 아직까지 Helm 3를 통해 설치할 수 있습니다.
버전 v1 에서 v2 로 바꾸기:
- dependencies 필드: 종속성 정의
    - v1 차트를 위한 독립된 requirements.yaml 파일에 위치( Chart Dependencies 보기).
- type 필드: 어플리케이션과 라이브러리 차트를 식별( ChartTypes 보기)

<br/>

#### ✔️ appVersion
appVersion 필드는 version 필드와 아무런 관계가 없습니다. 
appVersion 은 Application의 버전을 명시합니다.

가령, drupal chart 에 appVersion: "8.2.1" 가 적혀있다면, 이는 해당 차트가 포함한 Drupal의 버전이 8.2.1 임을 나타냅니다. 이 필드는 정보만 제공하고, 차트 버전 계산에 영향이 없습니다.
 버전을 따옴표로 감싸는 것을 매우 권장하는데, YAML 파서가 버전 번호를 string으로 다루게 하기 때문입니다. 따옴표 없이 남기면 몇몇의 경우에 파싱 문제가 생길 수 있습니다. 가령, YAML은 1.0 을 소숫점으로, 1234e10 같은 깃 커밋 SHA를 과학적 기수법으로 해석할 수 있습니다.
현재 Helm v3.5.0에서는, helm create는 기본값으로 appVersion 필드를 따옴표로 감쌉니다.

<br/>

#### ✔️ kubeVersion
(optional) kubeVersion 버전은 Kubernetes 버전을 SemVer 제약에 따라 정의할 수 있습니다.
Helm은 Chart를 내려 받을 때 버전 제약이 유효한지 검증하고, 만약 클러스터가 지원하지 않는 Kubernetes 버전을 실행하려 하면 실패시킵니다.
Version 제약은, 아래와 같이, 공백 문자열로 구분되며 비교 연산자로 구성됩니다.

```
>= 1.13.0 < 1.15.0
```

혹은, OR || 연산자를 통해 결합시킬 수도 있습니다.

```
>= 1.13.0 < 1.14.0 || >= 1.14.1 < 1.15.0
```

위 예제에서는 1.14.0 버전을 제외 시키며, 특정 버전에서 발견되는 버그가 발견될 때를 고려해보면, 왜 이러한 구문이 필요한 지 이해할 수 있을 것입니다.
버전 제약 연산자들 = != > < >= <= 외에도 아래와 같은 약칭 표기가 지원됩니다.

- `-` hyphen 은 폐구간 범위를 표기: `1.1 - 2.3.4` is equivalent to `>= 1.1 <= 2.3.4`.
- `x`, `X`, `*` 는 wildcard: `1.2.x` is equivalent to `>= 1.2.0 < 1.3.0`.
- `~` tilde는 PATCH 변경을 허용하는 범위: `~1.2.3` is equivalent to `>= 1.2.3 < 1.3.0`.
- `^` caret은 MINOR 변경을 허용하는 범위: `^1.2.3` is equivalent to `>= 1.2.3 < 2.0.0`.

<br/>

For a detailed explanation of supported semver constraints see Masterminds/semver.

```
FYI. Semantic Versioning 2.0.0: MAJOR.MINOR.PATCH
- MAJOR version when you make incompatible API changes
- MINOR version when you add functionality in a backward compatible manner
- PATCH version when you make backward compatible bug fixes
```

Additional labels for pre-release and build metadata are available as extensions to the MAJOR.MINOR.PATCH format.

<br/>

#### Chart Types
type 필드는 Chart의 타입을 정의하는데, 타입은 두 종류가 있습니다: application 혹은 library. Application은 Default 값이며, 완전한 동작을 가능하게 하는 표준 Chart입니다. library chart 는 Chart Builder를 위한 utilities 혹은 functions 를 제공합니다. library 타입의 chart는 Application과 다르게 설치 가능하지가 않고 보통 리소스 객체를 포함하지 않습니다.

**Note**: Application chart는 타입을 library 로 수정하여 library chart로 사용할 수 있습니다. Chart는 모든 유틸리티와 함수 기능을 활용할 수 있는 상태의 library chart로 렌더링됩니다. 그러나, 차트의 모든 리소스 객체는 레더링되지 않습니다.