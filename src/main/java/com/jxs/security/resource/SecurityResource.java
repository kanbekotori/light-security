package com.jxs.security.resource;

/**
 * 受保护的访问资源。
 * @author jiangxingshang
 */
public interface SecurityResource {

    /**
     * 资源唯一标识。
     * @return
     */
    String getId();

    /**
     * 资源内容。
     * @return
     */
    String getContent();
}
