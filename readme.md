# Hikvision API
- 使用說明: https://hackmd.io/VnAxveqLRESrgE3dFwnqbg
- History:
    - 0406 - first version
    - 0414 - 更新預覽Preview API
    - 0420 - 更新預覽/回放API input新增expand參數
        (設定解析度、FPS)
- maven package:
    - `mvn clean compile assembly:single`
    - `mvn package`
    - `mvn install dependency:copy-dependencies`
- maven package choose environment(env, staging, prod)
    - `mvn package -Dmaven.test.skip=true -P dev`
