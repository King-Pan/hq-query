package com.asiainfo.hqquery.entity.old;

public class TopreplyDisInfo {
	private String groupValueName;
	private String groupValue;
        private String groupTotalFlow;
        private String groupRecordCount;
        
    public String getGroupValueName() {
        return groupValueName;
    }

    public void setGroupValueName(String groupValueName) {
        this.groupValueName = groupValueName;
    }

    public String getGroupValue() {
        return groupValue;
    }

    public void setGroupValue(String groupValue) {
        this.groupValue = groupValue;
    }

    public String getGroupTotalFlow() {
        return groupTotalFlow;
    }

    public void setGroupTotalFlow(String groupTotalFlow) {
        this.groupTotalFlow = groupTotalFlow;
    }

    public String getGroupRecordCount() {
        return groupRecordCount;
    }

    public void setGroupRecordCount(String groupRecordCount) {
        this.groupRecordCount = groupRecordCount;
    }
        
	
	@Override
	public String toString() {
		return "TopreplyDisInfo [groupValueName=" + groupValueName + ", groupValue=" + groupValue + ", groupTotalFlow=" + groupTotalFlow +", groupRecordCount=" + groupRecordCount +"]";
	}
	
}
