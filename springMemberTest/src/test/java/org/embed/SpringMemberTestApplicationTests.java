package org.embed;

import java.util.List;

import org.embed.domain.MemberDTO;
import org.embed.mapper.MemberMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class SpringMemberTestApplicationTests {
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Test
	@DisplayName("맴버 입력")
	public void testInsertMember() throws Exception {
		MemberDTO member = new MemberDTO();
		member.setId("manmade");
		member.setPassword("4567");
		member.setName("왕왕이");
		member.setGender("man");
		member.setBirth("2005/09/05");
		member.setPhone("010-3845-4567");
		member.setAddress("부산진구 부산동");
		member.setMail("manmadeking@naver.com");
		
		int result = memberMapper.insertMember(member);
		System.out.println("맴버 입력(성공-1 실패-0) : "+result);
	}
	
	@Test
	@DisplayName("맴버 목록")
	public void testSelectAll() throws Exception{
		List<MemberDTO> memberList = memberMapper.selectMemberList();
		if(!memberList.isEmpty()) {
			for(MemberDTO member : memberList) {
				System.out.println("===========START LIST ITEM=============");
				System.out.println(member.getId());
				System.out.println(member.getPassword());
				System.out.println(member.getName());
				System.out.println(member.getBirth());
				System.out.println(member.getGender());
				System.out.println(member.getPhone());
				System.out.println(member.getAddress());
				System.out.println(member.getMail());
				System.out.println(member.getCreatedAt());
				System.out.println("===========END LIST ITEM=============");
			}
		}
	}
	
	@Test
	@DisplayName("맴버 상세")
	public void testMemberDetail() throws Exception {
		MemberDTO member = memberMapper.memberDetail("madman");
		try {
			String memberJson = new ObjectMapper().writeValueAsString(member);
			System.out.println("===========START DETAIL ITEM=============");
			System.out.print(memberJson);
			System.out.println("===========END DETAIL ITEM=============");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("맴버 수정")
	public void testUpdateMember() throws Exception {
		MemberDTO member = new MemberDTO();
		
		member.setId("manmade");
		member.setPassword("7894");
		member.setName("강왕이");
		member.setGender("woman");
		member.setBirth("2005/09/09");
		member.setPhone("010-3845-4567");
		member.setAddress("부산진구 부산동");
		member.setMail("manmadeking@naver.com");
		
		int result = memberMapper.updateMember(member);
		System.out.println("맴버 수정(성공-1 실패-0) : "+result);
	}
	
	@Test
	@DisplayName("맴버 삭제")
	public void testDeleteMember() throws Exception {
		String id = "manmade";
		int result = memberMapper.deleteMember(id);
		
		System.out.println("맴버 삭제(성공-1 실패-0) : "+result);
	}
}
