package com.willow.advancednc.service;


import com.willow.advancednc.model.Question;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    private static final String SOLR_URL = "http://127.0.0.1:8983/solr/wenda1";
    private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();
    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";

    public List<Question> searchQuestion(String keyword,int offset,int count,String hlPre,String hlPos)throws Exception{
        List<Question> questionList = new ArrayList<>();
        SolrQuery query = new SolrQuery(keyword);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlPre);
        query.setHighlightSimplePost(hlPos);
        query.set("hl.fl",QUESTION_TITLE_FIELD+","+QUESTION_CONTENT_FIELD);
        QueryResponse queryResponse = client.query(query);
        for(Map.Entry<String, Map<String,List<String>>> entry : queryResponse.getHighlighting().entrySet()){
            Question question = new Question();
            question.setId(Integer.parseInt(entry.getKey()));
            if(entry.getValue().containsKey(QUESTION_CONTENT_FIELD)){
                List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if(contentList.size()>0){
                    question.setContent(contentList.get(0));
                }
            }
            if(entry.getValue().containsKey(QUESTION_TITLE_FIELD)){
                List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
                if(titleList.size()>0){
                    question.setTitle(titleList.get(0));
                }
            }
            questionList.add(question);
        }
        return questionList;
    }

    public boolean indexQuestion(int qid,String title,String content)throws Exception{
        SolrInputDocument solrInputDocument = new SolrInputDocument();
        solrInputDocument.setField("id",qid);
        solrInputDocument.setField(QUESTION_TITLE_FIELD,title);
        solrInputDocument.setField(QUESTION_CONTENT_FIELD,content);
        UpdateResponse updateResponse = client.add(solrInputDocument,1000);
        return updateResponse != null && updateResponse.getStatus() == 0;
    }


}
