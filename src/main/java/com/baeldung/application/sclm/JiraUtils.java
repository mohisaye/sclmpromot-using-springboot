package com.baeldung.application.sclm;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;

/**
 * Created by m_sayekooie on 2/19/2020.
 */
public class JiraUtils {
    final static String jiraUser = "*****";
    final static String jiraPass = "****";
    final static String confPass = "****";
    final static String jiraServerAddress = "url to jira";
    final static String confluenceServerAddress = "url to confluence";

    public boolean addAttachmentToIssue(String issueKey, String fullfilename) {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            String user = jiraUser;
            String pass = jiraPass;
            String authStr = user + ":" + pass;
            String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));
            HttpPost httppost = new HttpPost("http://" + jiraServerAddress + "/rest/api/latest/issue/" + issueKey + "/attachments");
            httppost.setHeader("X-Atlassian-Token", "nocheck");
            httppost.setHeader("Authorization", "Basic " + encoding);
            File fileToUpload = new File(fullfilename);
            FileBody fileBody = new FileBody(fileToUpload);
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addPart("file", fileBody)
                    .build();
            httppost.setEntity(entity);
            String mess = "executing request " + httppost.getRequestLine();
//        logger.info(mess);
            response = httpclient.execute(httppost);
        } catch (Exception e) {
            e.getCause().printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (response.getStatusLine().getStatusCode() == 200)
            return true;
        else
            return false;

    }

    public void addPageAndContent(String result, String formNumber, int parentPage) {
        HttpResponse response = null;
        try {
            JSONObject newPage = defineConfluencePage(formNumber, result, "LPR");
            createConfluencePageViaPost(newPage);
        } catch (Exception e) {
            e.getCause().printStackTrace();
        }
    }

    public  JSONObject defineConfluencePage(String pageTitle, String wikiEntryText, String pageSpace) throws Exception {


        //This would be the command in Python (similar to the example
        //in the Confluence example:
        //
        //curl -u <username>:<password> -X POST -H 'Content-Type: application/json' -d'{
        // "type":"page",
        // "title":"My Awesome Page",
        //// "ancestors":[{"id":9994246}],
        // "space":{"key":"JOUR"},
        // "body":
        //        {"storage":
        //                   {"value":"<h1>Things That Are Awesome</h1><ul><li>Birds</li><li>Mammals</li><li>Decapods</li></ul>",
        //                    "representation":"storage"}
        //        },
       //// "metadata":
        ////             {"labels":[
        ////                        {"prefix":"global",
        ////                        "name":"journal"},
       ////                        {"prefix":"global",
       ////                        "name":"awesome_stuff"}
       ////                       ]
       ////             }
        // }'
        // http://localhost:8080/confluence/rest/api/content/ | python -mjson.tool

        JSONObject newPage = new JSONObject();

        // "type":"page",
        // "title":"My Awesome Page"
        newPage.put("type", "page");
        newPage.put("title", pageTitle);

        // "ancestors":[{"id":9994246}],
//        if (parentPageId != 0) {
//            JSONObject parentPage = new JSONObject();
//            parentPage.put("id", parentPageId);
//            JSONArray parentPageArray = new JSONArray();
//            parentPageArray.add(parentPage);
//            newPage.put("ancestors", parentPageArray);
//        }
        // "space":{"key":"JOUR"},
        JSONObject spaceOb = new JSONObject();
        spaceOb.put("key", pageSpace);
        newPage.put("space", spaceOb);

        // "body":
        //        {"storage":
        //                   {"value":"<p><h1>Things That Are Awesome</h1><ul><li>Birds</li><li>Mammals</li><li>Decapods</li></ul></p>",
        //                    "representation":"storage"}
        //        },
        JSONObject jsonObjects = new JSONObject();
        jsonObjects.put("value", wikiEntryText);
        jsonObjects.put("representation", "storage");

        JSONObject storageObject = new JSONObject();
        storageObject.put("storage", jsonObjects);
        newPage.put("body", storageObject);

        //LABELS
        // "metadata":
        //             {"labels":[
        //                        {"prefix":"global",
        //                        "name":"journal"},
        //                        {"prefix":"global",
        //                        "name":"awesome_stuff"}
        //                       ]
        //             }
//        JSONObject prefixJsonObject1 = new JSONObject();
//        prefixJsonObject1.put("prefix", "global");
//        prefixJsonObject1.put("name", "journal");
//        JSONObject prefixJsonObject2 = new JSONObject();
//        prefixJsonObject2.put("prefix", "global");
//        prefixJsonObject2.put("name", label);
//        JSONArray prefixArray = new JSONArray();
//        prefixArray.add(prefixJsonObject1);
//        prefixArray.add(prefixJsonObject2);
//        JSONObject labelsObject = new JSONObject();
//        labelsObject.put("labels", prefixArray);
//        newPage.put("metadata", labelsObject);

        return newPage;
    }

    public  void createConfluencePageViaPost(JSONObject newPage) throws Exception {
        HttpClient client = new DefaultHttpClient();

        // Send update request
        HttpEntity pageEntity = null;

        try {
            //2016-12-18 - StirlingCrow: Left off here.  Was finally able to get the post command to work
            //I can begin testing adding more data to the value stuff (see above)
            String user = jiraUser;
            String pass = confPass;
            String authStr = user + ":" + pass;
            String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));
            HttpPost httppost = new HttpPost(confluenceServerAddress + "/rest/api/content/");
            httppost.setHeader("X-Atlassian-Token", "nocheck");
            httppost.setHeader("Authorization", "Basic " + encoding);
//            HttpPost postPageRequest = new HttpPost(createContentRestUrl());

            StringEntity entity = new StringEntity(newPage.toString(), ContentType.APPLICATION_JSON);
            httppost.setEntity(entity);

            HttpResponse postPageResponse = client.execute(httppost);
            pageEntity = postPageResponse.getEntity();

            System.out.println("Push Page Request returned " + postPageResponse.getStatusLine().toString());
            System.out.println("");
            System.out.println(IOUtils.toString(pageEntity.getContent()));
        } finally {
            EntityUtils.consume(pageEntity);
        }
    }
}
