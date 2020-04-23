# Hikvision API
1. 取得回放URL(By code)
2. 取得回放URL(By name)
3. 取得所有IPCamera資訊列表

1. 取得所有IPCamera資訊列表
    - URL
        - 10.109.6.148:8081/v1/api/ipcamera/code/
    - GET
    - 必填參數
        - 無
    - 回傳參數
        - 執行狀態

![](https://i.imgur.com/cNdDEGO.jpg)



2. 取得回放URL(By Name)
    - URL
        - 10.109.6.148:8081/v1/api/ipcamera/replayurl/name
    - POST
    - 必填參數
        - cameraName(string)
        - beginTime(string)
        - endTime(string)
    - 回傳參數
        - replayUrl(string)
        
![](https://i.imgur.com/D0TemAp.jpg)



3. 取得回放URL(By code)
    - URL
        - 10.109.6.148:8081/v1/api/ipcamera/replayurl/code
    - POST
    - 必填參數
        - cameraIndexCode(string)
        - beginTime(string)
        - endTime(string)
    - 回傳參數
        - replayUrl(string)
        
![](https://i.imgur.com/ZZmJjDy.jpg)

test
