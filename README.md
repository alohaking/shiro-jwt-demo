### shiro+jwt+springboot demo

一、首次登录
```shell
curl -X POST -H "Content-Type:application/json" -d '{"username":"admin","password":"123456"}' http://localhost:8080/login -i
```

二、携带token认证
上面请求返回的header中有token信息，下次请求携带token访问，记得把下面的token换掉
```
curl -H "Content-Type:application/json" -H "x-auth-token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NTIzMDI2NDAsImlhdCI6MTU1MjI5OTA0MCwidXNlcm5hbWUiOiJhZG1pbiJ9.JKVPDSbrPOvk7xv_VQGnQ5y3PenK6NDcqAKLdmwSja8" -i http://localhost:8080/admin/roles
```

三、角色和权限控制
token是会刷新的，前端需要注意每次请求都要看一下response的header中是否有token，如果有，需要重置前端保存的token
```
curl -H "Content-Type:application/json" -H "x-auth-token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NTIzMDI2NDAsImlhdCI6MTU1MjI5OTA0MCwidXNlcm5hbWUiOiJhZG1pbiJ9.JKVPDSbrPOvk7xv_VQGnQ5y3PenK6NDcqAKLdmwSja8" -i http://localhost:8080/articles/list
curl -H "Content-Type:application/json" -H "x-auth-token: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NTIzMDI2NDAsImlhdCI6MTU1MjI5OTA0MCwidXNlcm5hbWUiOiJhZG1pbiJ9.JKVPDSbrPOvk7xv_VQGnQ5y3PenK6NDcqAKLdmwSja8" -i http://localhost:8080/admin/me

```