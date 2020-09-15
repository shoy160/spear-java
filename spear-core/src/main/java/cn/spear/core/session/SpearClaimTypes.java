package cn.spear.core.session;

/**
 * Claim Types
 *
 * @author shay
 * @date 2020/9/15
 */
public interface SpearClaimTypes {
    String HEADER_USER_AGENT = "User-Agent";
    String HEADER_REFERER = "Referer";
    String HEADER_FORWARD = "X-Forwarded-For";
    String HEADER_REAL_IP = "X-Real-IP";
    String HEADER_TENANT_ID = "Tenant-Id";
    String HEADER_TRACE_ID = "Trace-Id";

    String HEADER_USER_ID = "User-Id";
    String HEADER_USER_NAME = "User-Name";
    String HEADER_ROLE = "User-Role";
}
