package com.asiainfo.hqquery.entity.old;

import java.util.Arrays;
import java.util.List;

public class TopWholeObj {
	private List<TopreplyDisInfo> replyDisInfo;

    public List<TopreplyDisInfo> getReplyDisInfo() {
        return replyDisInfo;
    }

    public void setReplyDisInfo(List<TopreplyDisInfo> replyDisInfo) {
        this.replyDisInfo = replyDisInfo;
    }
	
	@Override
	public String toString() {
		return "TopWholeObj [replyDisInfo=" + Arrays.toString(replyDisInfo.toArray()) + "]";
	}
	
}
