package com.ecm.service;

import com.ecm.model.Evidence_Body;
import com.ecm.model.Evidence_Document;
import com.ecm.model.Evidence_Head;
import com.ecm.model.Text;
import com.ecm.util.ImportXMLUtil;

import java.util.List;

public interface TextService {

    Text getText(int cid);
    Text findTextByCid(int cid);
    Text updateText(Text text);


}
