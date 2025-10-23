package org.embed.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.embed.domain.MemberDTO;

import java.util.List;

@Mapper
public interface MemberMapper {
	List<MemberDTO> selectMemberList() throws Exception;
	int insertMember(MemberDTO member) throws Exception;
	int updateMember(MemberDTO member) throws Exception;
	int deleteMember(String id) throws Exception;
	MemberDTO memberDetail(String id) throws Exception;
}
