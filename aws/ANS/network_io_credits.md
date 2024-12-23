# Network I/O Credits

기준 대역폭보다 적은 네트워크 대역폭을 사용할 때마다 네트워크 I/O 크레딧을 얻음

기준 대역폭 이상의 네트워크가 필요할 때, 기준 이상을 제공하는 '버스트' 기능

버스트 대역폭은 공유 리소스이므로 인스턴스에 사용 가능한 크레딧이 있더라도 인스턴스 버스트는 가능한 범위에서 최대한 지원

<small>(EBS의 T Family와 비슷)</small>

기준 I/O와 추가된 버스트 I/O 사용량에 대해서만 비용을 지불하면 되므로 컴퓨팅 비용이 절감

- R4 및 C5와 같은 인스턴스 패밀리는 네트워크 I/O 크레딧 메커니즘을 사용
- 대부분의 애플리케이션은 **요청 피크가 되는 순간에 일관된 네트워크 성능을 띄지 않음**
- 이러한 인스턴스는 피크 요구 시점에서 기준 네트워크 성능을 초과하는 성능을 제공
- 네트워크 I/O 크레딧 메커니즘을 지원하는 인스턴스의 성능 벤치마크를 수행하기 전에 누적된 네트워크 크레딧을 고려해야 함

<br>

### Use cases for high performance networks

- **HPC(High Performance Computing) Workload**
  - HPC에서 클러스터 배치 그룹(cluster placement groups)을 사용하면 긴밀히 연결된 IO 집약적 및 스토리지 집약적 작업을 위해 저지연, 고대역폭 네트워크에 접근할 수 있음.
  - 더 빠른 Amazon EBS IO를 위해 Amazon EBS 최적화 인스턴스와 고성능을 위한 프로비저닝된 IOPS 볼륨 사용 권장.
- **Real Time Media**
  - VoIP 및 RTP, RTMP를 사용하는 미디어 스트리밍과 같은 애플리케이션에 적합.
  - 향상된 네트워킹은 패킷 손실과 지터를 줄이고 부드러운 패킷 전달을 제공.
- Data Processing, Backup, On-prem Data Transfer, etc
