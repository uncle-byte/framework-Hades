package com.framework.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpUtil Tester. 
 * 
 * @author chenhaipeng 
 * @since <pre>十一月 16, 2016</pre> 
 * @version 1.0 
*/ 
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class HttpUtilTest { 

    @Before
    public void before() throws Exception { 
    } 

    @After
    public void after() throws Exception { 
    }
     
    
    /**
	 * Method: doGet(String url)
	 */ 
    @Test
    public void testDoGet() throws Exception { 
        //TODO: Test goes here... 
    } 
    
    /**
	 * Method: doPost(String apiUrl) pass
	 */ 
    @Test
    public void testDoPost() throws Exception {
		Long start = System.currentTimeMillis();
		File file = new File("/Users/hai/temp/test.txt");
		Map fileMap = new HashMap();
		fileMap.put("file",file);
		String result = HttpUtil.doPost("http://localhost:8080/fileUpload",new HashMap<>(),fileMap);
		System.out.println(System.currentTimeMillis() -start /1000);
		System.out.println(result);
	}
    
    /**
	 * Method: doPostSSL(String apiUrl, Map<String, String> params)
	 */ 
    @Test
    public void testDoPostSSL() throws Exception { 
        //TODO: Test goes here... 
    } 
    
    /**
	 * Method: doGetSSL(String url, Map<String, String> params)
	 */ 
    @Test
    public void testDoGetSSL() throws Exception { 
        //TODO: Test goes here... 
    } 
    
    /**
	 * Method: createHeaders(Map<String, String> map)
	 */ 
    @Test
    public void testCreateHeaders() throws Exception { 
        //TODO: Test goes here... 
    } 


     /**
	 * Method: doGetParam(Map<String, String> bean)
	 */ 
    @Test
    public void testDoGetParam() throws Exception { 
        //TODO: Test goes here... 
    }

     /**
	 * Method: getResponse(String apiUrl, String httpStr, CloseableHttpResponse response)
	 */ 
    @Test
    public void testGetResponse() throws Exception { 
        //TODO: Test goes here... 
    }

     /**
	 * Method: getPostConfig(String apiUrl, String json, CloseableHttpClient httpClient, String httpStr, HttpPost httpPost, CloseableHttpResponse response)
	 */ 
    @Test
    public void testGetPostConfig() throws Exception { 
        //TODO: Test goes here... 
    }

     /**
	 * Method: createSSLHttpClient()
	 */ 
    @Test
    public void testCreateSSLHttpClient() throws Exception { 
        //TODO: Test goes here... 
    }
} 
