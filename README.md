# WiFi Signal Capturer

Collect WiFi signal strength for indoor positioning. Write to csv file in Download folder.

## Usage

Need fine location permission, WLAN permission, storage permission, and location service must be turned on.

For higher speed of data collection, please on developer mode, and set Wi-Fi scan throttling to **false**.

During the course of data collection, please make sure the app is in the foreground and the screen is kept on throughout the process.

## Pitfalls

* `write_timestamp` is the time when the data is written to the csv file, not the time when the data is collected. The granularity of the timestamp is 1 second.
* `timestamp` is the time when the data is collected, the granularity is higher but it is the time start from the device boot, not the time start from the data collection.
* In most cases, the `write_timestamp` is enough for the data anlysis, as it is already doing the aggregation job.
* Currently the permissions are not requested automatically, please grant the permissions manually.

## TODO

* [ ] Automatically request permissions.

---

收集 WiFi 信号强度，用于室内定位。写入到 Download 文件夹下的 csv 内。

## 使用方法

需要精确定位权限、WLAN 权限、存储权限，以及开启位置服务。

为了提高数据采集速度，请打开开发者模式，并将 Wi-Fi 扫描节流设置为 **false**。

在数据采集过程中，请确保应用在前台运行，并且屏幕保持常亮。

## 坑

* `write_timestamp` 是数据写入 csv 文件的时间，而不是数据采集的时间。时间粒度为 1 秒。
* `timestamp` 是数据采集的时间，粒度更高，但是是从设备开机开始计时，而不是从数据采集开始计时。
* 在大多数情况下，`write_timestamp` 已经足够用于数据分析，因为它已经做了聚合的工作。
* 目前权限没有自动申请，请手动授权。
