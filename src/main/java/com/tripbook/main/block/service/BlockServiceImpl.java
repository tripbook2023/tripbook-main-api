package com.tripbook.main.block.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.util.Asserts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.tripbook.main.block.dto.RequestBlock;
import com.tripbook.main.block.dto.ResponseBlock;
import com.tripbook.main.block.entity.Block;
import com.tripbook.main.block.repository.BlockRepository;
import com.tripbook.main.member.dto.ResponseMember;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {
	private final BlockRepository blockRepository;
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public ResponseBlock.ResultInfo insertBlocks(RequestBlock requestBlock) {
		validationMembers(requestBlock);
		//자기자신은 차단할 수 없음
		Asserts.check(!requestBlock.getTargetIdList().contains(requestBlock.getMember().getId()), "ContainsMemberId");
		List<Block> blocks = bindingBlockList(requestBlock);
		List<Block> resultBlock = blockRepository.saveAll(blocks);
		//blocks 사이즈와 결과 Block의 사이즈는 항상같음
		Asserts.check(blocks.size() == resultBlock.size(), "DeleteBlock_IsNothingDelete");
		return ResponseBlock.ResultInfo.builder()
			.message(List.of("SUCCESS"))
			.status(HttpStatus.OK)
			.build();
	}

	@Override
	@Transactional
	public ResponseBlock.ResultInfo deleteBlocks(RequestBlock requestBlock) {
		validationMembers(requestBlock);
		//자기자신은 삭제할 수 없음
		Asserts.check(!requestBlock.getTargetIdList().contains(requestBlock.getMember().getId()), "ContainsMemberId");
		Long result = blockRepository.deleteAllByTargetIdInAndMemberId(requestBlock.getTargetIdList(),
			requestBlock.getMember());
		//아무것도 삭제하지 못하면 잘못된 요청
		Asserts.check(result > 0, "DeleteBlock_IsNothingDelete");
		return ResponseBlock.ResultInfo.builder()
			.message(List.of("SUCCESS"))
			.status(HttpStatus.OK)
			.build();
	}

	@Override
	public List<ResponseMember.MemberSimpleDto> selectBlocks(RequestBlock requestBlock) {
		List<Block> blockByMemberId = blockRepository.findBlockByMemberId(requestBlock.getMember());
		List<Long> blockIds = blockByMemberId.stream().map(Block::getTargetId).collect(Collectors.toList());
		List<Member> memberList = memberRepository.findAllById(blockIds);
		return memberList.stream()
			.map(Member::toSimpleDto)
			.collect(Collectors.toList());
	}

	private List<Block> bindingBlockList(RequestBlock requestBlock) {
		Assert.notNull(requestBlock, "RequestBlock is required!!");
		List<Block> resultBlocks = new ArrayList<>();
		requestBlock.getTargetIdList().forEach(item -> resultBlocks.add(new Block(requestBlock.getMember(), item)));
		return resultBlocks;
	}

	private void validationMembers(RequestBlock requestBlock) {
		Long memberCount = memberRepository.countAllByIdIn(requestBlock.getTargetIdList());
		Asserts.check(memberCount == requestBlock.getTargetIdList().size(), "NotFound_Member");
	}

}
