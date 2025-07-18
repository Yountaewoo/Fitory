## 🚀 Create or Get User API

### Endpoint
`POST /users`

### Description
사용자 ID(`LoginMemberId`)를 기반으로 사용자를 조회하고, 존재하지 않을 경우 새로 생성합니다. 이미 존재하는 경우 해당 사용자 정보를 반환합니다.

### Request Header
| Name          | Type   | Required | Description                                     |
|---------------|--------|-----------|-------------------------------------------------|
| LoginMemberId | String | ✅       | 인증된 사용자 고유 식별자 (커스텀 어노테이션 또는 인증 헤더에서 주입) |

### Request Body
```json
{
  "name": "홍길동",
  "height": 175.5,
  "gender": "Man"
}
```
| Name  | Type    | Required | Description               |
|---------|---------|-----------|---------------------------|
| name    | String | ✅       | 사용자 이름                    |
| height | Double | ✅       | 사용자 키 (cm)                |
| gender | String | ✅       | 사용자 성별 (`Man` 또는 `Woman`) |

### Response
#### 성공 (200 OK)
```json
{
  "id": "user-uuid",
  "name": "홍길동",
  "height": 175.5,
  "gender": "Man"
}
```
| Name  | Type          | Description        |
|----------|---------------|--------------------|
| id      | String        | 사용자 고유 ID    |
| name    | String        | 사용자 이름        |
| height | Double        | 사용자 키 (cm)   |
| gender | Gender (Enum) | 사용자 성별      |

### Example
```http
POST /users
Header: LoginMemberId: abc-123-uuid
Content-Type: application/json

{
  "name": "홍길동",
  "height": 175.5,
  "gender": "Man"
}
```
#### Response
```json
{
  "id": "abc-123-uuid",
  "name": "홍길동",
  "height": 175.5,
  "gender": "Man"
}
```

### ⚠️ Notes
- `LoginMemberId`는 인증 로직에서 주입되며, 클라이언트가 직접 지정하지 않습니다.
- 이미 존재하는 사용자는 `Request Body`를 무시하고 기존 정보를 반환합니다.
