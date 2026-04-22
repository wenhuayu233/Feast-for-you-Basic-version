# Feast Web 测试需求追踪

## 范围
- 模块：`feast-web`
- 目标能力：收藏能力与认证能力

## 需求与测试映射

### RQ-001 收藏前必须校验菜品有效
- 描述：当菜品不存在或已下架时，收藏应失败并给出明确信息。
- 测试类型：单元测试
- 用例：`FavoriteServiceImplTest.addFavorite_shouldThrowWhenDishNotExists`
- 通过标准：抛出 `IllegalArgumentException`，消息为 `菜品不存在或已下架`。

### RQ-002 不允许重复收藏
- 描述：用户重复收藏同一菜品时应被拒绝。
- 测试类型：单元测试
- 用例：`FavoriteServiceImplTest.addFavorite_shouldThrowWhenAlreadyFavorited`
- 通过标准：抛出 `IllegalArgumentException`，消息为 `已收藏`。

### RQ-003 合法收藏应落库
- 描述：当菜品有效且未收藏时，应创建收藏记录。
- 测试类型：单元测试
- 用例：`FavoriteServiceImplTest.addFavorite_shouldSaveFavoriteWhenValid`
- 通过标准：调用保存逻辑，且记录包含 `dishId/userId/createdTime`。

### RQ-004 认证接口返回统一结构
- 描述：注册接口应返回成功码、成功消息、用户信息与 token。
- 测试类型：集成测试（Web Slice）
- 用例：`AuthControllerIntegrationTest.register_shouldReturnSuccessResultAndCookie`
- 通过标准：HTTP 200；`conde=20000`；包含 `Set-Cookie`。

### RQ-005 登录接口异常场景可区分
- 描述：账号密码错误与账号不可用需返回不同业务码。
- 测试类型：集成测试（Web Slice）
- 用例：
  - `AuthControllerIntegrationTest.login_shouldReturnLoginErrorWhenCredentialInvalid`
  - `AuthControllerIntegrationTest.login_shouldReturnAccessErrorWhenAccountDisabled`
- 通过标准：HTTP 200；分别返回 `conde=20002` 与 `conde=20003`。

### RQ-006 登录成功链路（系统上下文）
- 描述：在完整 SpringBoot 系统上下文中，登录接口应返回业务成功结果和 token。
- 测试类型：系统测试（SpringBoot 上下文）
- 用例：`AuthControllerSystemTest.login_shouldReturnSuccessInSystemContext`
- 通过标准：HTTP 200；`conde=20000`；返回 token 与用户 profile 字段。
