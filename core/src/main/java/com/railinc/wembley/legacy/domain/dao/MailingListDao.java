package com.railinc.wembley.legacy.domain.dao;

import java.util.List;

import com.railinc.wembley.legacysvc.domain.MailingListVo;
import com.railinc.wembley.legacysvc.domain.MailingListsVo;

public interface MailingListDao {
	MailingListsVo getMailingLists();

	List<String> getMailingListIds();
	
	MailingListsVo getMailingList( String application, String type );

	void createMailingList(MailingListVo vo);

	MailingListVo getMailingList(String key);

	void deleteMailingList(String mailingListKey);

	void updateMailingList(String key, MailingListVo vo);
	
	MailingListVo getMailingListByShortName(String shortName);
}
