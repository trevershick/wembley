package com.railinc.r2dq.sso;

public enum SsoRoleType {
	DataSteward("R2DQSTEWARD"), Support("R2DQSUPPORT"), AppAdmin("R2DQAPPADM"),Super("R2DQSUPER");
	
	
	private String roleId;

	SsoRoleType(String roleId) {
		this.roleId = roleId;
	}
	public String roleId() {
		return this.roleId;
	}
}
