package com.aestheticsw.jobkeywords.domain.indeed;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "results")
public class ResultList {

    @XmlElement(name = "result")
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }
    
}
