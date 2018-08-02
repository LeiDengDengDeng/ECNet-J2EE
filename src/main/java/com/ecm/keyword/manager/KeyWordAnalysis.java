package com.ecm.keyword.manager;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyWordAnalysis {

    public String findType(String content){
        if(isHowMuch(content))
            return "how much";
        else if(isWhat(content))
            return "what";
        else if(isWhen(content))
            return "when";
        else if(isWhere(content))
            return "where";
        else if(isWho(content))
            return "who";
        else
            return null;
    }

    public boolean isHowMuch(String content){
        boolean res = false;
        Pattern pattern = Pattern.compile("\\d+([\\u4e00-\\u9fa5]{0,1}元)"); //|\\d+英镑
        Matcher matcher = pattern.matcher(content);
        res = matcher.find();
        return res;
    }

    public boolean isWhen(String content){
        boolean res = false;
        Pattern pattern = Pattern.compile(
                "\\d+年\\d+月\\d+日\\d+时\\d+分|"
                        + "\\d+年\\d+月\\d+日\\d+时|"
                        + "\\d+年\\d+月\\d+日|"
                        + "\\d+年\\d+月|"
                        + "\\d+月\\d+日|"
                        +"\\d+日|"
                        + "\\d+时\\d+分|"
                        + "\\d+时");
        Matcher matcher = pattern.matcher(content);
        res = matcher.find();
        return res;
    }

    //表示地点前的介词
    private ArrayList<String> preps = new ArrayList<String>(Arrays.asList("在","于","至","往","从","沿"));

    public boolean isWhere(String content){

        Result result = NlpAnalysis.parse(content);
        List<Term> originTerms = result.getTerms();
        int p = 0;
        while (p < originTerms.size()){
            Term term = originTerms.get(p);
            p ++;
            //找到地点词
            if (term.getNatureStr().equals("s")){
                return true;
            }
            //找到对应介词,表示之后的词可能是地点
            else if (term.getNatureStr().equals("p") && preps.contains(term.getName())){
                String where = "";
                while (p < originTerms.size()){
                    Term innerTerm = originTerms.get(p);
                    //连接名词、地点词,用于组成一个地点
                    if (innerTerm.getNatureStr().startsWith("n") || innerTerm.getNatureStr().equals("s")){
                        where += innerTerm.getName();
                        p ++;
                        continue;
                    }
                    break;
                }

                if(where.length()>0){
                    //测试备选短语的词性
                    Result placeResult = NlpAnalysis.parse(where);
                    //如果备选短语为一个单词，则不可以为名称词、名词性惯用语、名词性语素
                    if(placeResult.size()==1){
                        String nature = placeResult.get(0).getNatureStr();
                        if(!nature.startsWith("nr") && !("nl").equals(nature) && !("ng").equals(nature)){
                           return true;
                        }
                    }else{
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isWho(String content){
        Result parserResult = NlpAnalysis.parse(content);
        Iterator<Term> it = parserResult.iterator();
        while(it.hasNext()){
            Term t = it.next();
            String nature = t.getNatureStr();
            String who = t.getRealName();
            //名称应该不止一个字，且词性为名称词
            if(who.length()>1 && nature.startsWith("nr")){
                return true;
            }
        }
        return false;
    }

    public boolean isWhat(String content){
        //用只提取书名的方式先试一下
        Pattern pattern = Pattern.compile("\\《(.*?)\\》");
        Matcher match = pattern.matcher(content);
        while(match.find()) {
            return true;
        }
        if(content.contains("(")||content.contains("（")){
            content.replace('(',' ');
            content.replace('（',' ');
            content.replace('）',' ');
            content.replace(')',' ');
        }
        try{
            CoNLLSentence sentence = HanLP.parseDependency(content);
            // 遍历语义依存关系,如果发现带宾语的关系则提取宾语并完善该宾语（利用定中关系来完善）
            Map<String,String> describeLink = new HashMap<String, String>();
            for (CoNLLWord word : sentence)
            {
                if(word.DEPREL.equals("定中关系")){
                    describeLink.put(word.HEAD.LEMMA,word.LEMMA);
                }
                if(word.DEPREL.equals("动宾关系")||word.DEPREL.equals("介宾关系")){
                    String tmp = KeyWordCalculator.findDescriptions(describeLink,word.LEMMA);
                    //测试备选短语的词性
                    if (tmp.equals("放在")){
                        continue;
                    }
                    Result result = NlpAnalysis.parse(tmp);
                    //如果备选短语为一个单词，则不可以为名称词、地点词或动词
                    if(result.size()==1){
                        String nature = result.get(0).getNatureStr();
                        if(!nature.startsWith("nr") && !nature.startsWith("nt") && !nature.startsWith("ns")
                                && !("s").equals(nature) && !nature.startsWith("v")){
                            return true;
                        }
                    }else{
                        return true;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return false;
    }
}
