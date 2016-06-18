# light-security

## 基本概念
1. 资源（权限）可以指一个URL，一个按钮，总之某样东西需要得到许可才能访问或操作的都称为（保护）资源。而用户是否能够访问或操作该资源，是看用户是否持有这个资源（也就是用户是否有权限）。
2. 会话（Session），任何客户端与服务端连接后都会产生一个会话，从会话中我们可以得到用户ID，请求凭证等信息。

## 基本使用
这里将教你如何使用安全模块，登录，登出，获取会话。

### 登录

```java

	@Autowire
	private SessionManager sessionManager;

	public void login(String username, String password) {
  		SecurityToken token = new UsernamePasswordToken(username, password);
  		Data data = sessionManager.login(token);
  		String accessToken = data.getAccessToken();
  		String userId = data.getId();
  		Object myData = data.getObj();
	}

```

登录成功后，你将可以从返回值中获得一个请求凭证`accessToken`，将这个凭证返回给客户端，客户端访问其他资源时要将这个凭证发回给服务端，服务端使用这个凭证验证客户端是否有权限进行访问操作。

### 登出

```java

	Session s = sessionManager.getSession();
	s.logout();

```

### 会话

```java

	Session s = sessionManager.getSession();
	if(s.isLogin()) {
		String userId = s.getId();
	} else {
		throw new Exception("你还未登录。")
	}

```

不管用户是否已经登录，你都可以从管理器中获得一个会话，从会话中客户判断用户处于什么情况。注意，这不同于J2EE的HttpSession，你无法将你的数据存储到session中，如果有此需求请存到原生的HttpSession。

## 深入理解
在基本使用中给出了样例代码，要使这些代码能够正常工作，你需要给出一些配置和实现。如权限资源在哪里定义，登录逻辑处理在哪里定义，会话管理器（SessionManager）怎么获取等等。

### 配置

```java
@Configuration
public class SecurityConfig {
  @Bean
  public SessionCache sessionCache() throws IOException {
    //生产环境大用户数建议用缓存服务器（RedisSessionCache）
    return new MemorySessionCache();
  }

  @Bean
  public UriResourceDataSource uriResourceDataSource() {
    //创建保护资源的数据源
    UriResourceDataSource ds = new UriResourceDataSource();
    List<UriResource> resources = ...;
    ds.setResources(resources);
    return ds;
  }

  @Bean
  public SessionManager sessionManager(LoginHandler handler, SessionCache sessionCache) throws IOException {
    SessionManager manager = new SessionManager("my_project:session:", sessionCache);
    manager.setTimeout(3600 * 72);
    manager.setRealm(handler);
    return manager;
	}

	//权限过滤器
	@Bean(name = "authFilter")
	public SecurityFilterFactoryBean authFilter(
    SessionManager userManager,
    UriResourceDataSource dataSource,
    SecurityAuthcFailHandler failHandler) throws IOException {
    return new SecurityFilterFactoryBean(userManager, dataSource, failHandler);
	}
}
```

配置SecurityFilterFactoryBean到Filter列表中。

```java
@Override
protected Filter[] getServletFilters() {
  DelegatingFilterProxy authFilter = new DelegatingFilterProxy("authFilter");
  authFilter.setTargetFilterLifecycle(true);
  return new Filter[] {authFilter };
}
```

所有有关安全模块的配置都在`SecurityConfig.java`类中，这个类是一个spring的配置类。

我们可以看到里面有一个`sessionManager`的方法，这个方法上有一个`@Bean`的注解，表示在这里我定义了一个`SessionManager`的实例，你可以在其他地方通过`@Autowire`或`@Resource`注解来获取到这个管理器，这些都是spring的知识，不再多说。

除了会话管理器，里面还定义了权限验证器（过滤器）、缓存实现、资源数据源等。

数据源的创建需由用户自己定义，框架并不关心你的保护资源以什么格式存储，存在哪里，你只要将这些资源交给框架即可。需要强调的是过滤器只处理URL形式的保护资源，所以你需要将URL的资源交给框架，

#####流程图
<img src="files/security.png" style="width:600px;"/>

从浏览器发出请求后，会经过`SecurityFilterFactoryBean`，这是一个由spring包装过的过滤器，验证逻辑都在这个过滤器中，这个过滤器只处理URL类型的资源。首先判断URL请求的资源是否是一个静态文件，如果是的话就交给容器（例如Tomcat）本身处理，建议搭建前置服务器（例如Nginx）来专门处理这些静态文件。

如果URL请求的资源不是静态文件，那么从这个URL上找到匹配的资源，`UriResourceDataSource`是一个URL数据源，你的数据源可以是数据库，也可以是配置文件。

遍历返回匹配到的资源（如果没有则表示URL不是一个保护资源，验证通过），首先判断这个资源是否是受保护的（需要验证），如果不需要直接验证通过，如果需要验证，则从`SessionManager`中获取`Session`，判断用户是否登录，如果未登录则交给`SecurityAuthcFailHandler`处理错误，如果已经登录，则判断用户是否拥有这个资源ID（登录的时候，我们为用户分配了对应角色的资源ID集合），如果拥有则验证通过，否则交给`SecurityAuthcFailHandler`处理。

```java

public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	HttpServletRequest req = (HttpServletRequest)request;
  HttpServletResponse resp = (HttpServletResponse)response;
	Session s = null;

	//静态资源检查
	if(staticResources != null && staticResources.size() > 0) {
    	String path = req.getRequestURI();
      for(String r : staticResources) {
	        if(path.startsWith(r)) {
    	    	//如果是静态资源则不需要做后面的验证
        	    chain.doFilter(req, resp);
            	return;
          }
	    }
  }
  Session s = sessionManager.getSession();
  UriResource uri = dataSource.match(req);
  if(uri != null) {
      if(uri.isVerify()) {
          if(s.isLogin()) {
              if(s.hasResource(uri)) {
                  //用户拥有这个资源权限，更新token过期时间
                  sessionManager.expire(s.getToken());
                  chain.doFilter(req, resp);
                  return;
              } else {
                  handler.onFail(req, resp, SecurityAuthcFailHandler.UNAUTHORIZATION);
              }
          } else {
              //未登录
              handler.onFail(req, resp, SecurityAuthcFailHandler.NOT_LOGIN);
              return;
          }
      } else {
          //无需权限验证的资源
          chain.doFilter(req, resp);
          return;
      }
  }

	chain.doFilter(req, resp);
}

```

#####请求凭证
前面说过登录成功后会得到一个请求凭证（AccessToken），请求受保护的资源都应该带上这个凭证，那么服务端是如何从请求中获取到这个凭证呢，请参考`com.jxs.security.TokenReader.java`。

1. 从头信息获取，如果你的请求头中有`X-User-Token`，则从这里获取。
2. 如果头信息中没有，则从查询参数中获取`?_token=xxxx`。
