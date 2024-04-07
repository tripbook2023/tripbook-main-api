package com.tripbook.main.block.service;

import java.util.List;

import com.tripbook.main.block.dto.RequestBlock;
import com.tripbook.main.block.dto.ResponseBlock;
import com.tripbook.main.member.dto.ResponseMember;

//사용자 차단 기능 개발
public interface BlockService {
	//사용자 차단 (여러명 가능)
	public ResponseBlock.ResultInfo insertBlocks(RequestBlock requestBlock);

	//사용자 차단해제(여러명 가능)
	public ResponseBlock.ResultInfo deleteBlocks(RequestBlock requestBlock);

	//차단 목록 조회
	public List<ResponseMember.MemberSimpleDto> selectBlocks(RequestBlock requestBlock);
}
