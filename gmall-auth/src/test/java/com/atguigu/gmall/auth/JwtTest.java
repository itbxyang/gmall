package com.atguigu.gmall.auth;

import com.atguigu.core.utils.JwtUtils;
import com.atguigu.core.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
	private static final String pubKeyPath = "D:\\JetBrains\\IdeaProjects\\Microservices\\gmall_project\\rsa\\rsa.pub";

    private static final String priKeyPath = "D:\\JetBrains\\IdeaProjects\\Microservices\\gmall_project\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "11");
        map.put("username", "liuyan");
        // 生成token
        String token = JwtUtils.generateToken(map, privateKey, 1);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6IjExIiwidXNlcm5hbWUiOiJsaXV5YW4iLCJleHAiOjE1ODYwOTA0ODl9.dS99kuxxuE-MTM5YFqlveBcc__Kwfx-DmgnOgKA8nNOy5OpELMT5OA2p0MgO40dr6wOcpkNOejgKZt44ZR842h1NNT0-rSz4SNshULd8c8tE3KbF_xWrXzuIHFPbX0mGWh4gIqwFluSyb6u09m7hEPue3q3sUBfSu6tNvs0U81tKFQL4Lp7vkMVNiWv5zZ2H8KxCodsPZPGCIVgxATRdQ5wAabDn1I3RgU7ckXYjpLkFElP6Fto8HkJPkaMwmZleHOa9PfjyCqCRRS_1Rv0CaIk_as3aIGp4OBuPsgcB2633hPKiSyd-wG-HLDNRwXgB8oADhGu_ZI8SszYcBA2-pw";

        // 解析token
        Map<String, Object> map = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + map.get("id"));
        System.out.println("userName: " + map.get("username"));
    }
}