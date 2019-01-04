package com.asiainfo.hqquery.entity.old;

import java.util.List;

public class DetailWholeObj {
	private List<DetailreplyDisInfo> replyDisInfo;

    public List<DetailreplyDisInfo> getReplyDisInfo() {
        return replyDisInfo;
    }

    public void setReplyDisInfo(List<DetailreplyDisInfo> replyDisInfo) {
        this.replyDisInfo = replyDisInfo;
    }
        

	

	@Override
	public String toString() {
		return "DetailWholeObj [replyDisInfo=" + replyDisInfo + "]";
	}
	
}
